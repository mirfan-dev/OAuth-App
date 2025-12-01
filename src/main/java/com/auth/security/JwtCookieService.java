package com.auth.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@Getter
@Slf4j
public class JwtCookieService {

    @Value("${security.jwt.refresh-token-cookie-name}")
    private String refreshTokenCookieName;

    @Value("${security.jwt.cookie-http-only}")
    private Boolean cookieHttpOnly;

    @Value("${security.jwt.cookie-secure}")
    private Boolean cookieSecure;

    @Value("${security.jwt.cookie-domain}")
    private String cookieDomain;

    @Value("${security.jwt.cookie-same-site}")
    private String cookieSameSite;


    /**
     * Create and attach Refresh Token Cookie
     */
    public void attachRefreshCookie(
            HttpServletResponse response,
            String value,
            Long maxAge
    ) {
        log.info("Attaching cookie: name={}, maxAge={}", refreshTokenCookieName, maxAge);

        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from(refreshTokenCookieName, value)
                        .sameSite(cookieSameSite)
                        .secure(cookieSecure)
                        .maxAge(maxAge)
                        .httpOnly(cookieHttpOnly)
                        .path("/");

        if (cookieDomain != null && !cookieDomain.isBlank()) {
            builder.domain(cookieDomain);
        }

        ResponseCookie cookie = builder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


    /**
     * Clear Refresh Token Cookie
     */
    public void clearRefreshCookie(HttpServletResponse response) {
        log.info("Clearing refresh token cookie: {}", refreshTokenCookieName);

        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from(refreshTokenCookieName, "")
                        .sameSite(cookieSameSite)
                        .secure(cookieSecure)
                        .maxAge(0)
                        .httpOnly(cookieHttpOnly)
                        .path("/");

        if (cookieDomain != null && !cookieDomain.isBlank()) {
            builder.domain(cookieDomain);
        }

        ResponseCookie cookie = builder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


    /**
     * Set No-Store headers
     */
    public void addNoStoreHeaders(HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");
        response.setHeader("Pragma", "no-cache");
    }
}
