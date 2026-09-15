package com.waleed.capstone1.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotEmpty(message = "ID must not be empty")
    private String id;

    @NotEmpty(message = "Username must not be empty")
    @Size(min = 6, message = "Username length must be more than 5 characters")
    private String username;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 7, message = "Password length must be more than 6 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password must have both characters and digits")
    private String password;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Must be a valid email format")
    private String email;

    @NotEmpty(message = "Role must not be empty")
    @Pattern(regexp = "Admin|Customer", message = "Role must be either Admin or Customer")
    private String role;

    @NotNull(message = "Balance must not be empty")
    @Positive(message = "Balance must be a positive number")
    private Double balance;
}