package com.supersection.trading.service;

import com.supersection.trading.custom.CustomUserDetails;
import com.supersection.trading.dto.request.AuthenticationRequest;
import com.supersection.trading.dto.response.AuthResponse;
import com.supersection.trading.entity.User;
import com.supersection.trading.exception.DuplicateResourceException;
import com.supersection.trading.exception.PhoneNumberFormatException;
import com.supersection.trading.exception.RegistrationFailedException;
import com.supersection.trading.utility.JwtUtil;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.supersection.trading.domain.Status.SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthResponse registerUser(User user) {
        // Validate required fields
        List<String> validationErrors = validateRegistrationFields(user);
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(String.join("; ", validationErrors));
        }

        try {
            User savedUser = userService.createUser(user);
            String jwt = jwtUtil.generateToken(savedUser.getUsername(), user.getEmail());

            return new AuthResponse(
                    jwt,
                    savedUser.getUsername(),
                    SUCCESS,
                    "User registered successfully",
                    false
            );
        } catch (DuplicateResourceException e) {
            throw new DuplicateResourceException(e.getMessage(), e);
        } catch (PhoneNumberFormatException e) {
            throw new PhoneNumberFormatException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RegistrationFailedException("User registration failed", e);
        }
    }

    public AuthResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword())
            );
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsernameOrEmail());
            String jwt = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getEmail());

            return new AuthResponse(jwt, userDetails.getUsername(), SUCCESS, "User logged in successfully", false);
        } catch (Exception e) {
            throw new BadCredentialsException("Incorrect username or password");
        }
    }

    private List<String> validateRegistrationFields(User request) {

        final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        final String USERNAME_REGEX = "^[a-zA-Z0-9_-]+$";

        List<String> errors = new ArrayList<>();
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            errors.add("Email is required");
        } else if (!request.getEmail().matches(EMAIL_REGEX)) {
            errors.add("Incorrect email format");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            errors.add("Password is required");
        } else if (request.getPassword().length() < 8) {
            errors.add("Password must be at least 8 characters long");
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isEmpty()) {
            errors.add("Phone number is required");
        }
        if (request.getRegion() == null || request.getRegion().isEmpty()) {
            errors.add("Region code is required");
        }
        if ((request.getUsername() != null && !request.getUsername().isEmpty())
                && !request.getUsername().matches(USERNAME_REGEX)) {
            errors.add("Username can only contain letters, numbers, dashes, and underscores");
        }
        return errors;
    }
}
