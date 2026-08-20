package com.bankcore.controller;

import com.bankcore.dto.LoginRequest;
import com.bankcore.model.User;
import com.bankcore.security.JwtService;
import com.bankcore.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;
    private final JwtService jwtService;

    public LoginController(
            LoginService loginService,
            JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        try {
            User user = loginService.login(request);

            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Login successful",
                            "token", token,
                            "tokenType", "Bearer",
                            "expiresIn", 3600,
                            "userId", user.getId(),
                            "username", user.getUsername(),
                            "email", user.getEmail()
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "error",
                            "Invalid username or password"
                    ));
        }
    }
}
