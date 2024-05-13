package com.example.demo.domains.dataBase.repository;

import com.example.demo.domains.dataBase.model.MatchStatus;
import com.example.demo.domains.dataBase.model.Notification;
import com.example.demo.domains.dataBase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToUser(User user);

    Optional<Notification> findByFromUserAndToUserAndMatch_Status(User fromUser, User toUser, MatchStatus status);

}