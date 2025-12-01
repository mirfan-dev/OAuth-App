package com.auth.service;

import com.auth.dto.UserDto;
import com.auth.payloads.LoginRequest;
import com.auth.payloads.RefreshTokenRequest;
import com.auth.payloads.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    UserDto registerUser(UserDto userDto);

    TokenResponse login(LoginRequest loginRequest, HttpServletResponse response);

    TokenResponse refreshToken(RefreshTokenRequest body, HttpServletResponse response, HttpServletRequest request);

     void logout(HttpServletRequest request,HttpServletResponse response);
}
