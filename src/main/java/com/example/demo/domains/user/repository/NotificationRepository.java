package com.example.demo.domains.user.repository;


import com.example.demo.domains.user.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}