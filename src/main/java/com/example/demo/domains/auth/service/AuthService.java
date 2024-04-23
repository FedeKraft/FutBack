package com.example.demo.domains.auth.service;

import com.example.demo.domains.auth.dto.LoginUserDTO;
import com.example.demo.domains.auth.dto.RegisterUserDTO;
import com.example.demo.domains.auth.dto.TokenDTO;
import com.example.demo.domains.auth.utils.JWTGen;
import com.example.demo.domains.user.model.Match;
import com.example.demo.domains.user.model.Notification;
import com.example.demo.domains.user.model.User;
import com.example.demo.domains.user.repository.MatchRepository;
import com.example.demo.domains.user.repository.NotificationRepository;
import com.example.demo.domains.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {
    private final JWTGen jwtGen = new JWTGen();
    @Autowired
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final NotificationRepository notificationRepository;


    @Autowired
    public AuthService(UserRepository userRepository, MatchRepository matchRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.notificationRepository = notificationRepository;
    }
    public String register(RegisterUserDTO registerUserDTO) {
        User user = new User(registerUserDTO.name, registerUserDTO.email, registerUserDTO.password, registerUserDTO.city, registerUserDTO.playerAmount, registerUserDTO.number);
        userRepository.save(user);
        return "User registered successfully";
    }

    public TokenDTO login(LoginUserDTO loginUserDTO) {
     User user = userRepository.findByEmail(loginUserDTO.email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!user.getPassword().equals(loginUserDTO.password)) {
            throw new RuntimeException("Invalid password");
        }
        return jwtGen.generateToken(user.getId(), "user");//cuando tenga admin se usa user.getRole() asi no son todos user
    }

    public RegisterUserDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new RegisterUserDTO(user.getName(), user.getPassword(), user.getEmail(), user.getCity(), user.getPlayerAmount(), user.getNumber());
    }

    public List<User> getAllUsers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<User> users = userRepository.findByCity(user.getCity());
        users.removeIf(u -> u.getId().equals(userId)); // Remove the logged-in user from the list
        return users;
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Match> matches = matchRepository.findByToUser(user);
        List<Notification> notifications = new ArrayList<>();
        for (Match match : matches) {
            notifications.add(match.getNotification());
        }
        return notifications;
    }
    // En AuthService.java
    public Match createMatch(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Match match = new Match();
        match.setToUser(user1);
        match.setFromUser(user2);

        // Create a notification for user1 and associate it with the match
        Notification notification1 = new Notification();
        notification1.setFromUser(user1); // Set the fromUser
        notification1.setToUser(user2); // Set the toUser
        notification1.setMessage("Has iniciado un match con el usuario " + user2.getName());
        Notification savedNotification1 = notificationRepository.save(notification1);
        match.setNotification(savedNotification1);

        // Create a notification for user2 and associate it with the match
        Notification notification2 = new Notification();
        notification2.setFromUser(user2); // Set the fromUser
        notification2.setToUser(user1); // Set the toUser
        notification2.setMessage("El usuario " + user1.getName() + " ha iniciado un match contigo");
        Notification savedNotification2 = notificationRepository.save(notification2);
        match.setNotification(savedNotification2);

        return matchRepository.save(match);
    }

}



