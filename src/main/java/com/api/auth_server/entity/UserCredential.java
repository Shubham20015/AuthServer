package com.api.auth_server.entity;

import com.api.auth_server.constraint.Password;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_credentials")
public class UserCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name may not be empty")
    @Size(min = 3, max = 40, message = "Name must be between 3 and 40 characters long")
    @Column(unique = true)
    private String name;

    @Password
    private String password;
}
