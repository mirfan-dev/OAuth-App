package com.auth.service.impl;

import com.auth.dto.UserDto;
import com.auth.entity.RefreshToken;
import com.auth.entity.User;
import com.auth.payloads.LoginRequest;
import com.auth.payloads.RefreshTokenRequest;
import com.auth.payloads.TokenResponse;
import com.auth.repository.RefreshTokenRepository;
import com.auth.repository.UserRepository;
import com.auth.security.JwtCookieService;
import com.auth.security.JwtService;
import com.auth.service.AuthService;
import com.auth.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.channels.ScatteringByteChannel;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private  final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtCookieService jwtCookieService;

    @Override
    public UserDto registerUser(UserDto userDto) {
        //logic
        //verify email
        //verify password
        //default roles
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userService.createUser(userDto);
    }

    @Override
    public TokenResponse login(
            LoginRequest loginRequest,
            HttpServletResponse response
    ) {

        // Authenticate user first
        Authentication authentication = authenticate(loginRequest);

        // Fetch user from DB
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        // Check if user account is disabled
        if (!user.isEnable()) {
            throw new DisabledException("User account is disabled");
        }

        // Save refresh token
        String jti = UUID.randomUUID().toString();

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

        return TokenResponse.bearer(
                accessToken,
                refreshToken,
                jwtService.getAccessTtlSeconds(),
                modelMapper.map(user, UserDto.class)
        );
    }



    private Authentication authenticate(LoginRequest loginRequest) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid username or password");
        } catch (DisabledException ex) {
            throw new DisabledException("User account is disabled");
        } catch (Exception ex) {
            throw new BadCredentialsException("Authentication failed");
        }
    }

    @Override
    public TokenResponse refreshToken(
            RefreshTokenRequest body,
            HttpServletResponse response,
            HttpServletRequest request) {
        String refreshToken=readRefreshTokenFromRequest(body,request)
                .orElseThrow(()-> new BadCredentialsException("Refresh token is missing"));

        if (!jwtService.isRefreshToken(refreshToken)){
            throw new BadCredentialsException("Invalid Refresh Token Type");
        }
        String jti=jwtService.getJti(refreshToken);
        UUID userUuid=jwtService.getUserId(refreshToken);

        RefreshToken storedRefreshToken = refreshTokenRepository.findByJti(jti)
                .orElseThrow(()-> new BadCredentialsException("Invalid Refresh Token"));

        if (storedRefreshToken.getRevoked()){
            throw new BadCredentialsException("Refresh Token is revoked");
        }

        if (storedRefreshToken.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new BadCredentialsException("Refresh Token expired");
        }

        if (!storedRefreshToken.getUser().getId().equals(userUuid)){
            throw new BadCredentialsException("Refresh token does not belong to this user");
        }
        // rotate refresh token
        storedRefreshToken.setRevoked(true);
        String newJti= UUID.randomUUID().toString();
        storedRefreshToken.setReplacedByToken(newJti);
        refreshTokenRepository.save(storedRefreshToken);

        User user=storedRefreshToken.getUser();

        RefreshToken refreshTokenObj = RefreshToken.builder()
                .jti(newJti)
                .revoked(false)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .build();

        refreshTokenRepository.save(refreshTokenObj);

        // Generate Access + Refresh Tokens
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user, refreshTokenObj.getJti());

        // use JwtCookieService to attach refresh token in cookie
        jwtCookieService.attachRefreshCookie(response,newRefreshToken, jwtService.getRefreshTtlSeconds());
        jwtCookieService.addNoStoreHeaders(response);

        return TokenResponse.bearer(
                newAccessToken,
                newRefreshToken,
                jwtService.getAccessTtlSeconds(),
                modelMapper.map(user, UserDto.class)
        );
    }



    // this method will refresh token from request header
    private Optional<String> readRefreshTokenFromRequest(RefreshTokenRequest body, HttpServletRequest request) {

        if (request.getCookies() !=null){
            Optional<String> fromCookie=
                    Arrays.stream(request.getCookies())
                            .filter(cookie -> jwtCookieService.getRefreshTokenCookieName().equals(cookie.getName()))
                            .map(Cookie::getValue)
                            .filter(v->!v.isBlank())
                            .findFirst();

            if (fromCookie.isPresent()){
                return fromCookie;
            }
        }
        // body
        if (body!=null && body.refreshToken() !=null && !body.refreshToken().isBlank()){
            return Optional.of(body.refreshToken());
        }
        // custom header
        String refreshHeader=request.getHeader("X-Refresh-Token");
        if (refreshHeader!=null && !refreshHeader.isBlank())
            return Optional.of(refreshHeader.trim());

        //Authorization = Bearer <Token>
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader !=null && authHeader.regionMatches(true,0,"Bearer ",0,7)){
            String candidate= authHeader.substring(7).trim();
            if (!candidate .isEmpty()){
                try {
                    if(jwtService.isRefreshToken(candidate))
                        return Optional.of(candidate);
                }catch (Exception ignored){

                }
            }
        }
        return Optional.empty();

    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // Read refresh token from cookies
        readRefreshTokenFromRequest(null,request)
                .ifPresent(token -> {
                    try {
                        if (jwtService.isRefreshToken(token)) {
                            String jti = jwtService.getJti(token);

                            refreshTokenRepository.findByJti(jti)
                                    .ifPresent(refreshToken -> {
                                        refreshToken.setRevoked(true);
                                        refreshTokenRepository.save(refreshToken);
                                    });
                        }
                    } catch (JwtException e) {

                        log.warn("Invalid refresh token during logout: {}", e.getMessage());
                    }
                });

        // Clear cookie + headers
        jwtCookieService.clearRefreshCookie(response);
        jwtCookieService.addNoStoreHeaders(response);

        // Clear security context
        SecurityContextHolder.clearContext();
    }



}
