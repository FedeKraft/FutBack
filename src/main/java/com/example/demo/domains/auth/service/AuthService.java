package com.example.demo.domains.auth.service;

import com.example.demo.domains.auth.dto.LoginUserDTO;
import com.example.demo.domains.auth.dto.RegisterUserDTO;
import com.example.demo.domains.auth.dto.TokenDTO;
import com.example.demo.domains.auth.utils.JWTGen;
import com.example.demo.domains.dataBase.model.*;
import com.example.demo.domains.dataBase.repository.MatchRepository;
import com.example.demo.domains.dataBase.repository.NotificationRepository;
import com.example.demo.domains.dataBase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
        List<User> users = userRepository.findByPlayerAmountAndStatus(user.getPlayerAmount(), UserStatus.ACTIVE); // Get all users with the same playerAmount that are active
        users.removeIf(u -> !u.getCity().equals(user.getCity()) || u.getId().equals(userId)); // Remove the users not in the same city and the logged-in user from the list
        if (!users.isEmpty()) return users;
        else {
            List<User> usersNearBy = userRepository.findByCityAndStatus(user.getCity(), UserStatus.ACTIVE); // Get all users in the same city that are active
            usersNearBy.removeIf(u -> u.getId().equals(userId)); // Remove the logged-in user from the list
            return usersNearBy;
        }
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByToUser(user);
    }

    public void createMatch(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if there is already a pending notification
        Optional<Notification> existingNotification = notificationRepository.findByFromUserAndToUserAndMatch_Status(user1, user2, MatchStatus.PENDING);
        if (existingNotification.isPresent()) {
            throw new RuntimeException("A match request is already pending");
        }

        // Create the match and set its status to PENDING
        Match match = new Match();
        match.setFromUser(user1);
        match.setToUser(user2);
        match.setStatus(MatchStatus.PENDING);
        Match savedMatch = matchRepository.save(match); // Save the match and generate its ID

        // Create a notification for user2 and associate it with the match
        Notification notification = new Notification();
        notification.setFromUser(user1);
        notification.setToUser(user2);
        notification.setMessage("El usuario " + user1.getName() + " ha iniciado un match contigo");
        notification.setMatch(savedMatch); // Set the saved match
        notificationRepository.save(notification); // Save the notification
    }

    public void acceptMatch(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        // Set match status ACCEPTED
        match.setStatus(MatchStatus.ACCEPTED);

        // Create a notification for the user who requested the match
        Notification notification = new Notification();
        notification.setFromUser(match.getToUser());
        notification.setToUser(match.getFromUser());
        notification.setMessage(match.getToUser().getName() + " ha aceptado tu match");
        notification.setMatch(match);
        notificationRepository.save(notification); // Save the notification
    }

    public void rejectMatch(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        // Set match status REJECTED
        match.setStatus(MatchStatus.REJECTED);

        // Create a notification for the user who requested the match
        Notification notification = new Notification();
        notification.setFromUser(match.getToUser());
        notification.setToUser(match.getFromUser());
        notification.setMessage(match.getToUser().getName() + " ha rechazado tu match");
        notification.setMatch(match);
        notificationRepository.save(notification); // Save the notification
    }

    public RegisterUserDTO editUserProfile(Long userId, RegisterUserDTO registerUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(registerUserDTO.name);
        user.setEmail(registerUserDTO.email);
        user.setPassword(registerUserDTO.password);
        user.setCity(registerUserDTO.city);
        user.setPlayerAmount(registerUserDTO.playerAmount);
        user.setNumber(registerUserDTO.number);

        userRepository.save(user);

        return new RegisterUserDTO(user.getName(), user.getPassword(), user.getEmail(), user.getCity(), user.getPlayerAmount(), user.getNumber());
    }

    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() == UserStatus.ACTIVE) {
            user.setStatus(UserStatus.INACTIVE);
        } else {
            user.setStatus(UserStatus.ACTIVE);
        }

        userRepository.save(user);
    }
}





