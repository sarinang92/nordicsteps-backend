package com.myproject.controller;

import com.myproject.dto.LoginRequestDTO;
import com.myproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.myproject.model.User;
import com.myproject.dto.PasswordChangeDTO;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO request) {
        User user = authService.login(request.getEmail(), request.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("email", user.getEmail());
        response.put("id", user.getUserId());
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

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
