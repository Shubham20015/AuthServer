package com.api.auth_server.service;

import com.api.auth_server.dto.AuthRequest;
import com.api.auth_server.dto.AuthResponse;
import com.api.auth_server.entity.UserCredential;
import com.api.auth_server.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String saveUser(AuthRequest authRequest) {
        UserCredential credential = UserCredential.builder()
                                                  .name(authRequest.getUsername())
                                                  .email(authRequest.getEmail())
                                                  .password(passwordEncoder.encode(authRequest.getPassword()))
                                                  .build();
        repository.save(credential);
        return jwtService.generateToken(credential);
    }

    @Override
    public boolean findByUsername(String username) {
        return repository.findByName(username).isPresent();
    }

    @Override
    public AuthResponse generateToken(AuthRequest authRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                ));

        String token = null;
        if (authentication.isAuthenticated()) {
            Optional<UserCredential> userCredential = repository.findByName(authRequest.getUsername());
            token = jwtService.generateToken(userCredential.get());
        }

        return AuthResponse.builder()
                            .accessToken(token)
                            .timestamp(Instant.now())
                            .build();
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
}
