package com.auth.payloads;

import com.auth.dto.UserDto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        Long expiredTime,
        String tokenType,
        UserDto user
) {

    public static TokenResponse bearer(
            String accessToken,
            String refreshToken,
            Long expiredTime,
            UserDto user
    ) {
        return new TokenResponse(
                accessToken,
                refreshToken,
                expiredTime,
                "Bearer",
                user
        );
    }
}
