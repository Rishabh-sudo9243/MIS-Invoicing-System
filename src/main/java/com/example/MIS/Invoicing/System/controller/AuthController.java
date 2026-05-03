package com.example.MIS.Invoicing.System.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.MIS.Invoicing.System.entity.LoginRequest;
import com.example.MIS.Invoicing.System.entity.User;
import com.example.MIS.Invoicing.System.service.UserService;
import com.example.MIS.Invoicing.System.entity.ResetPasswordRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private com.example.MIS.Invoicing.System.repository.VerificationTokenRepository tokenRepository;

    @GetMapping("/get-token")
    public ResponseEntity<?> getToken() {
        return ResponseEntity.ok(tokenRepository.findAll());
    }

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token) {
        userService.verifyUser(token);
        return ResponseEntity.ok("User verified successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

    String response = userService.login(request.getEmail(), request.getPassword());

    return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok("Password reset email sent. Please check your inbox.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully. You can now login.");
    }

}
