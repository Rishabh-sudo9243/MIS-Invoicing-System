package com.example.MIS.Invoicing.System.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.MIS.Invoicing.System.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}