package com.example.demo.domains.dataBase.repository;

import com.example.demo.domains.dataBase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByCity(String city);
    List<User> findByPlayerAmount(String playerAmount);
}
