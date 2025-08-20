package com.supersection.trading.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.supersection.trading.entity.User;
import com.supersection.trading.exception.DuplicateResourceException;
import com.supersection.trading.exception.PhoneNumberFormatException;
import com.supersection.trading.repository.UserRepository;
import com.supersection.trading.utility.PhoneNumberUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(User user) {
        // Check uniqueness of email, username
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email is already taken");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("Username is already taken");
        }

        // Generate a username if not provided
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            user.setUsername(generateUsername());
        }
        // Encoded password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Format phone number according to region
        try {
            String formattedNumber = PhoneNumberUtils.formatPhoneNumber(user.getPhoneNumber(), user.getRegion());
            user.setPhoneNumber(formattedNumber);
        } catch (NumberParseException e) {
            throw new PhoneNumberFormatException("Error formatting phone number", e);
        }
        // Check uniqueness of phone number
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number is already taken");
        }

        // Save user to the repository
        return userRepository.save(user);
    }

    private String generateUsername() {
        String generatedUsername = "user";
        boolean isUnique = false;

        while (!isUnique) {
            int randomNumber = new Random().nextInt(99999);
            generatedUsername += randomNumber;
            // Check if the username already exists in the database
            if (!userRepository.existsByUsername(generatedUsername)) {
                isUnique = true;
            }
        }
        return generatedUsername;
    }
}