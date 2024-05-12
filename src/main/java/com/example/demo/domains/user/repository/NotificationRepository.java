package com.example.demo.domains.user.repository;


import com.example.demo.domains.user.model.Match;
import com.example.demo.domains.user.model.Notification;
import com.example.demo.domains.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToUser(User user);

    Optional<Notification> findByMatch(Match match);
}