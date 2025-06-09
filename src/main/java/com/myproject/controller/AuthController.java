package com.myproject.controller;

import com.myproject.dto.LoginRequestDTO;
import com.myproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
        authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok("Login successful");
    }
}
