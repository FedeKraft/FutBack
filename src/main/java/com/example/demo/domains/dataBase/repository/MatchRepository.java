package com.example.demo.domains.dataBase.repository;

import com.example.demo.domains.dataBase.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MatchRepository extends JpaRepository<Match, Long> {
}