package com.example.MIS.Invoicing.System.repository;

import com.example.MIS.Invoicing.System.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository 
    extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(com.example.MIS.Invoicing.System.entity.User user);
}