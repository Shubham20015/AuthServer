package com.api.auth_server.controller;

import com.api.auth_server.dto.AuthRequest;
import com.api.auth_server.dto.AuthResponse;
import com.api.auth_server.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> addNewUser(@RequestBody @Valid @NotNull AuthRequest request) {
        if (authService.findByUsername(request.getUsername())) {
            AuthResponse authResponse = AuthResponse.builder()
                    .message("Username is already taken")
                    .timestamp(Instant.now())
                    .build();
            return ResponseEntity.badRequest().body(authResponse);
        }
        String token = authService.saveUser(request);
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(token)
                .message("User added to the system")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> getToken(@RequestBody @Valid @NotNull AuthRequest request) {
        AuthResponse authResponse = authService.generateToken(request);

        if (authResponse.getAccessToken() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        } else {
            authResponse = AuthResponse.builder()
                    .message("Invalid username or password")
                    .timestamp(Instant.now())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validateToken(@RequestParam("token") @NotNull String token) {
        boolean isValidToken = authService.validateToken(token);
        AuthResponse authResponse = AuthResponse.builder()
                .message(isValidToken ? "Token is valid" : "Invalid token")
                .timestamp(Instant.now())
                .build();
        return isValidToken ?
                ResponseEntity.ok(authResponse) :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
    }
}
