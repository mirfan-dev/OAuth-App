package com.auth.payloads;

public record LoginRequest(
        String email,
        String password
) {
}
