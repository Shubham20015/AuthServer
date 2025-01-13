package com.api.auth_server.service;

import com.api.auth_server.dto.AuthRequest;
import com.api.auth_server.entity.UserCredential;
import com.api.auth_server.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public String saveUser(AuthRequest authRequest) {
        UserCredential credential = UserCredential.builder()
                                                  .name(authRequest.getUsername())
                                                  .password(passwordEncoder.encode(authRequest.getPassword()))
                                                  .build();
        repository.save(credential);
        return "User added to the system";
    }

    @Override
    public boolean findByUsername(String username) {
        return repository.findByName(username).isPresent();
    }

    @Override
    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
}
