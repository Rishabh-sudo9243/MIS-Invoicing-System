package com.example.MIS.Invoicing.System.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MIS.Invoicing.System.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
}