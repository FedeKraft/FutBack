package com.example.demo.domains.user.repository;

import com.example.demo.domains.user.model.Match;
import com.example.demo.domains.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByToUser(User user);
}