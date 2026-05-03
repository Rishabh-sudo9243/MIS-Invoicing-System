package com.example.MIS.Invoicing.System.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.MIS.Invoicing.System.entity.Status;
import com.example.MIS.Invoicing.System.entity.User;
import com.example.MIS.Invoicing.System.entity.VerificationToken;
import com.example.MIS.Invoicing.System.repository.UserRepository;
import com.example.MIS.Invoicing.System.repository.VerificationTokenRepository;
import com.example.MIS.Invoicing.System.service.config.JwtUtil;

import java.time.LocalDateTime;
import java.util.UUID;
import com.example.MIS.Invoicing.System.entity.PasswordResetToken;
import com.example.MIS.Invoicing.System.repository.PasswordResetTokenRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public void register(User user) {

        if (user.getRole() == null || user.getRole().isEmpty()) {
            throw new RuntimeException("Role is required");
        }
        String role = user.getRole().toUpperCase();
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new RuntimeException("Invalid role. Must be USER or ADMIN");
        }
        user.setRole(role);

        if (user.getName() == null || user.getName().isEmpty()) {
            throw new RuntimeException("Name is required");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.PENDING);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(24);
        VerificationToken verificationToken = new VerificationToken(token, user, expiry);
        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(user.getEmail(), token);
    }


    public void verifyUser(String token) {

        VerificationToken verificationToken = tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired. Please register again.");
        }

        User user = verificationToken.getUser();
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
    }


    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        if (user.getStatus() != Status.ACTIVE) {
            throw new RuntimeException("Account not verified. Please check your email.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }


    public void forgotPassword(String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("No account found with this email"));

    passwordResetTokenRepository.deleteByUser(user);

    String token = UUID.randomUUID().toString();
    LocalDateTime expiry = LocalDateTime.now().plusHours(1);

    PasswordResetToken resetToken = new PasswordResetToken(token, user, expiry);
    passwordResetTokenRepository.save(resetToken);

    emailService.sendResetPasswordEmail(user.getEmail(), token);
    }


    public void resetPassword(String token, String newPassword) {

    PasswordResetToken resetToken = passwordResetTokenRepository
            .findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

    if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("Reset token has expired. Please request again.");
    }

    if (newPassword == null || newPassword.isEmpty()) {
        throw new RuntimeException("New password cannot be empty");
    }

    if (newPassword.length() < 6) {
        throw new RuntimeException("Password must be at least 6 characters");
    }

    User user = resetToken.getUser();
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    passwordResetTokenRepository.delete(resetToken);
    }
}