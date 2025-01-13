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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody @Valid @NotNull AuthRequest request) {
        if (authService.findByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }
        String response = authService.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> getToken(@RequestBody @Valid @NotNull AuthRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));

        if (authentication.isAuthenticated()) {
            AuthResponse authResponse = AuthResponse.builder()
                                                    .accessToken(authService.generateToken(request.getUsername()))
                                                    .timestamp(Instant.now())
                                                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        } else {
            AuthResponse authResponse = AuthResponse.builder()
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
