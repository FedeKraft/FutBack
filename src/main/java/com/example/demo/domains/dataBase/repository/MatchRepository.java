package com.example.demo.domains.dataBase.repository;

import com.example.demo.domains.dataBase.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByFromUserId(Long currentUserId);
    List<Match> findByToUserId(Long currentUserId);
}