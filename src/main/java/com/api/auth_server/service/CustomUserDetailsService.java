package com.api.auth_server.service;

import com.api.auth_server.config.CustomUserDetails;
import com.api.auth_server.entity.UserCredential;
import com.api.auth_server.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> credential = userCredentialRepository.findByName(username);
        return credential.map(CustomUserDetails::new)
                         .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + username));
    }
}
