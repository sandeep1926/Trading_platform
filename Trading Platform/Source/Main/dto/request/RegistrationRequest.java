package com.supersection.trading.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    private String fullName;

    @Email(message = "Email format is not valid")
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Invalid username format")
    private String username;

    @NotEmpty(message = "Phone number is required")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotEmpty(message = "Region code is required")
    @NotBlank(message = "Region code is required")
    private String region;

    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;

}