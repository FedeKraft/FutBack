package com.example.demo.domains.dataBase.repository;

import com.example.demo.domains.dataBase.model.User;
import com.example.demo.domains.dataBase.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByCityAndStatus(String city, UserStatus status);
    List<User> findByPlayerAmountAndStatus(String playerAmount, UserStatus status);
    List<User> findAllByOrderByEloDesc();
    User findByResetToken(String resetToken);
}
