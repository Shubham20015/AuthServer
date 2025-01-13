package com.api.auth_server.service;

import com.api.auth_server.dto.AuthRequest;

public interface AuthService {
    String saveUser(AuthRequest authRequest);
    boolean findByUsername(String username);
    String generateToken(String username);
    boolean validateToken(String token);
}
