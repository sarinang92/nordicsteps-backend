package com.myproject.service;

import com.myproject.model.User;
import com.myproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.myproject.dto.PasswordChangeDTO;
import com.myproject.dto.UserLoginResponseDTO;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Handles user login logic
    public UserLoginResponseDTO login(String email, String rawPassword) {
        // Look up user by email, throw error if not found
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Invalid email or password"));

        // Check if the entered password matches the hashed one in DB
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return new UserLoginResponseDTO(user.getUserId(), user.getEmail(), "Login successful");
    }

    // Handles password change logic
    public void changePassword(PasswordChangeDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the old password matches before changing
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password");
        }

        // Update the password with the newly encoded one
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

}
