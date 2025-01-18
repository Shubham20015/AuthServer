package com.api.auth_server.service;

import com.api.auth_server.dto.AuthRequest;
import com.api.auth_server.dto.AuthResponse;

public interface AuthService {
    String saveUser(AuthRequest authRequest);
    boolean findByUsername(String username);
    AuthResponse generateToken(AuthRequest authRequest);
    boolean validateToken(String token);
}
