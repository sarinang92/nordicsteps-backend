package com.myproject.controller;

import com.myproject.dto.LoginRequestDTO;
import com.myproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.myproject.model.User;
import com.myproject.dto.PasswordChangeDTO;
import com.myproject.dto.UserLoginResponseDTO;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Handles user login
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        UserLoginResponseDTO response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    // Handles password change requests
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO dto) {
        try {
            authService.changePassword(dto);
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


}
