package com.example.demo.domains.user.repository;

import com.example.demo.domains.user.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
}
