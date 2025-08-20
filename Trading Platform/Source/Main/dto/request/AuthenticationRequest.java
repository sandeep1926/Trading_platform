package com.supersection.trading.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Username or email is required")
    @NotEmpty(message = "Username or email is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @NotEmpty(message = "Password is required")
    private String password;
}
