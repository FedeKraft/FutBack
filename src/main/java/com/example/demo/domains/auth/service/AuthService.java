package com.example.demo.domains.auth.service;

import com.example.demo.domains.auth.dto.LoginUserDTO;
import com.example.demo.domains.auth.dto.RegisterUserDTO;
import com.example.demo.domains.auth.dto.TokenDTO;
import com.example.demo.domains.auth.utils.JWTGen;
import com.example.demo.domains.user.model.Match;
import com.example.demo.domains.user.model.MatchStatus;
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
        List<User> users = userRepository.findByPlayerAmount(user.getPlayerAmount()); // Get all users with the same playerAmount
        users.removeIf(u -> !u.getCity().equals(user.getCity()) || u.getId().equals(userId)); // Remove the users not in the same city and the logged-in user from the list
        if (!users.isEmpty()) return users;
        else {
            List<User> usersNearBy = userRepository.findByCity(user.getCity()); // Get all users in the same city
            usersNearBy.removeIf(u -> u.getId().equals(userId)); // Remove the logged-in user from the list
            return usersNearBy;
        }
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

    public void createMatch(Long user1Id, Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Match match = new Match();
        match.setFromUser(user1);
        match.setToUser(user2);

        // Create a notification for user2 and associate it with the match
        Notification notification = new Notification();
        notification.setFromUser(user1); // Set the fromUser
        notification.setToUser(user2); // Set the toUser
        notification.setMessage("El usuario " + user1.getName() + " ha iniciado un match contigo");
        Notification savedNotification = notificationRepository.save(notification);
        match.setNotification(savedNotification);
        match.setStatus(MatchStatus.PENDING);
        matchRepository.save(match);
    }

    public void acceptMatch(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        match.setStatus(MatchStatus.ACCEPTED);
        matchRepository.save(match);
    }

    public void rejectMatch(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        match.setStatus(MatchStatus.REJECTED);
        matchRepository.save(match);
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
}





