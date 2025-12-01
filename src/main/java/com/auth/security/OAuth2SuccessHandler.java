package com.auth.security;


import com.auth.entity.RefreshToken;
import com.auth.entity.User;
import com.auth.enums.Provider;
import com.auth.exception.ResourceNotFoundException;
import com.auth.repository.RefreshTokenRepository;
import com.auth.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtCookieService jwtCookieService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.auth.frontend.success-redirect}")
    private String frontendSuccessUrl;

    @Value("${app.auth.frontend.failure-redirect}")
    private String frontendFailureUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("Successful authentication");
        log.info(authentication.toString());



        OAuth2User oAuth2User= (OAuth2User) authentication.getPrincipal();

        // identify user

        String registrationId= "unknown";

        if (authentication instanceof OAuth2AuthenticationToken token){
            registrationId = token.getAuthorizedClientRegistrationId();
        }

        log.info("registrationId: ",registrationId);
        log.info("user: ", oAuth2User.getAttributes().toString());

        // username
        // user email
        // new usercreate

        User user;
        switch (registrationId){
            case "google" -> {
                String googleId = oAuth2User.getAttributes().getOrDefault("sub", "").toString();
                String email = oAuth2User.getAttributes().getOrDefault("email", "").toString();
                String name = oAuth2User.getAttributes().getOrDefault("name", "").toString();
                String picture = oAuth2User.getAttributes().getOrDefault("picture", "").toString();
                User newUser = User.builder()
                        .email(email)
                        .name(name)
                        .image(picture)
                        .enable(true)
                        .providerId(googleId)
                        .provider(Provider.GOOGLE)
                        .build();

                user=userRepository.findByEmail(email).orElseGet(()->userRepository.save(newUser));
            }

            case "github" -> {
                String githubId = oAuth2User.getAttributes().getOrDefault("id", "").toString();
                String name = oAuth2User.getAttributes().getOrDefault("login", "").toString();
                String picture = oAuth2User.getAttributes().getOrDefault("avatar_url", "").toString();

                String email =(String) oAuth2User.getAttributes().get("email");
                if (email==null){
                    email= name + "@github.com";
                }
                User newUser = User.builder()
                        .email(email)
                        .name(name)
                        .image(picture)
                        .enable(true)
                        .providerId(githubId)
                        .provider(Provider.GITHUB)
                        .build();

                user=userRepository.findByEmail(email).orElseGet(()->userRepository.save(newUser));
            }
            default -> {
                throw new ResourceNotFoundException("Invalid Provider");
            }
        }

        // redirect jwt token to frontend

        // send refresh token to user

        String jti= UUID.randomUUID().toString();

        RefreshToken refreshTokenObj = RefreshToken.builder()
                .jti(jti)
                .revoked(false)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .build();

        refreshTokenRepository.save(refreshTokenObj);

        // Generate Access + Refresh Tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, refreshTokenObj.getJti());

        // use JwtCookieService to attach refresh token in cookie
        jwtCookieService.attachRefreshCookie(response,refreshToken, jwtService.getRefreshTtlSeconds());
        jwtCookieService.addNoStoreHeaders(response);
//        response.getWriter().write("Login Successful");

        response.sendRedirect(frontendSuccessUrl);



    }
}
