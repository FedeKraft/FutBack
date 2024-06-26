package com.example.demo.domains.auth.service;

import com.example.demo.domains.auth.dto.FormDTO;
import com.example.demo.domains.auth.dto.LoginUserDTO;
import com.example.demo.domains.auth.dto.RegisterUserDTO;
import com.example.demo.domains.auth.dto.TokenDTO;
import com.example.demo.domains.auth.utils.JWTGen;
import com.example.demo.domains.dataBase.model.*;
import com.example.demo.domains.dataBase.repository.FormRepository;
import com.example.demo.domains.dataBase.repository.MatchRepository;
import com.example.demo.domains.dataBase.repository.NotificationRepository;
import com.example.demo.domains.dataBase.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AuthService {
    private final JWTGen jwtGen = new JWTGen();
    @Autowired
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final NotificationRepository notificationRepository;
    private final FormRepository formRepository;
    @Autowired
    private final JavaMailSender mailSender;


    @Autowired
    public AuthService(UserRepository userRepository, MatchRepository matchRepository, NotificationRepository notificationRepository, FormRepository formRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.notificationRepository = notificationRepository;
        this.formRepository = formRepository;
        this.mailSender = mailSender;
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

        return new RegisterUserDTO(user.getName(), user.getPassword(), user.getEmail(), user.getCity(), user.getPlayerAmount(), user.getNumber(), user.getElo(), user.getStars());
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

    public void createMatch(Long fromUserId, Long toUserId) {
        User user1 = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if there is already a pending notification
        Optional<Notification> existingNotification = notificationRepository.findByFromUserAndToUserAndMatch_Status(user1, user2, MatchStatus.PENDING);
        if (existingNotification.isPresent()) {
            throw new RuntimeException("A match request is already pending");
        }

        // Create the match and set its status to PENDING
        Match match = new Match(user1, user2, MatchStatus.PENDING);
        Match savedMatch = matchRepository.save(match); // Save the match and generate its ID

        // Create a notification for user2 and associate it with the match
        Notification notification = new Notification();
        notification.setFromUser(user1);
        notification.setToUser(user2);
        notification.setMessage("El usuario " + user1.getName() + " ha iniciado un match contigo");
        notification.setResponded(false);
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
        notification.setResponded(false);
        notification.setMatch(match);
        notificationRepository.save(notification); // Save the notification

        // Create a notification for both users to inform the match result
        Notification notification1 = new Notification();
        notification1.setFromUser(match.getFromUser());
        notification1.setToUser(match.getToUser());
        notification1.setMessage("Informar el resultado del partido contra " + notification1.getFromUser().getName());
        notification.setResponded(false);
        notification1.setMatch(match);
        notificationRepository.save(notification1); // Save the notification

        Notification notification2 = new Notification();
        notification2.setFromUser(match.getToUser());
        notification2.setToUser(match.getFromUser());
        notification2.setMessage("Informar el resultado del partido contra " + notification2.getFromUser().getName());
        notification.setResponded(false);
        notification2.setMatch(match);
        notificationRepository.save(notification2); // Save the notification
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
        notification.setResponded(false);
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

        return new RegisterUserDTO(user.getName(), user.getPassword(), user.getEmail(), user.getCity(), user.getPlayerAmount(), user.getNumber(), user.getElo(), user.getStars());
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

    public void createForm(FormDTO formDTO) {
        Notification notification = notificationRepository.findById(formDTO.fromNotificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        User currentUser = notification.getToUser();
        Match match = notification.getMatch();
        User user1 = match.getFromUser();
        User user2 = match.getToUser();

        if (currentUser == user1 && match.getFromUserForm() == null) {
            Form form = new Form(formDTO.goalsInFavor, formDTO.goalsAgainst, formDTO.fairPlay, formDTO.comment);
            form.setUser(currentUser);
            formRepository.save(form);
            match.setFromUserForm(form);
            matchRepository.save(match);
            notification.setResponded(true);
            notificationRepository.save(notification);
        }
        if (currentUser == user2 && match.getToUserForm() == null) {
            Form form = new Form(formDTO.goalsInFavor, formDTO.goalsAgainst, formDTO.fairPlay, formDTO.comment);
            form.setUser(currentUser);
            formRepository.save(form);
            match.setToUserForm(form);
            matchRepository.save(match);
            notification.setResponded(true);
            notificationRepository.save(notification);
        }

        Form form1 = match.getFromUserForm();
        Form form2 = match.getToUserForm();

        if (form1 != null && form2 != null) {
            if (form1.getGoalsInFavor() == form1.getGoalsAgainst() && form2.getGoalsAgainst() == form2.getGoalsInFavor()) {
                int newElo1 = user1.updateElo(user2.getElo(), 0.5);
                int newElo2 = user2.updateElo(user1.getElo(), 0.5);
                user1.setElo(newElo1);
                user2.setElo(newElo2);
                userRepository.save(user1);
                userRepository.save(user2);
            }
            if (form1.getGoalsInFavor() > form1.getGoalsAgainst() && form2.getGoalsInFavor() < form2.getGoalsAgainst()) {
                int newElo1 = user1.updateElo(user2.getElo(), 1);
                int newElo2 = user2.updateElo(user1.getElo(), 0);
                user1.setElo(newElo1);
                user2.setElo(newElo2);
                userRepository.save(user1);
                userRepository.save(user2);
            }
            if (form1.getGoalsInFavor() < form1.getGoalsAgainst() && form2.getGoalsInFavor() > form2.getGoalsAgainst()) {
                int newElo1 = user1.updateElo(user2.getElo(), 0);
                int newElo2 = user2.updateElo(user1.getElo(), 1);
                user1.setElo(newElo1);
                user2.setElo(newElo2);
                userRepository.save(user1);
                userRepository.save(user2);
            }
            List<Match> matches1 = matchRepository.findByFromUserId(user1.getId());
            matches1.addAll(matchRepository.findByToUserId(user1.getId()));

            List<Match> matches2 = matchRepository.findByFromUserId(user2.getId());
            matches2.addAll(matchRepository.findByToUserId(user2.getId()));

            int totalMatches1 = matches1.size();
            int totalMatches2 = matches2.size();
            user1.updateStars(form1.getFairPlay(), totalMatches1 + 1);
            user2.updateStars(form2.getFairPlay(), totalMatches2 + 1);
            userRepository.save(user1);
            userRepository.save(user2);
        }
    }

    public void cancelMatch(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setResponded(true);
        notificationRepository.save(notification);
    }

    public List<User> getRanking() {
        return userRepository.findAllByOrderByEloDesc();
    }

    public List<Match> getMatchHistory(Long userId) {
            List<Match> matches = matchRepository.findByFromUserId(userId);
            matches.addAll(matchRepository.findByToUserId(userId));
            List<Match> acceptedMatches = new ArrayList<>();
            for(Match match: matches){
                if(match.getStatus()== MatchStatus.ACCEPTED && match.getFromUserForm()!=null && match.getToUserForm()!=null){
                    acceptedMatches.add(match);
                }
            }
            return acceptedMatches;
        }

    public List<Form> getIncidents(Long id) {
        List<Match> matches = matchRepository.findByFromUserId(id);
        matches.addAll(matchRepository.findByToUserId(id));
        List<Form> forms = new ArrayList<>();
        for(Match match: matches){
            if(match.getStatus() == MatchStatus.ACCEPTED && match.getFromUserForm() != null && match.getToUserForm() != null){
                if (Objects.equals(match.getFromUser().getId(), id)) {
                    forms.add(match.getToUserForm());
                } else {
                    forms.add(match.getFromUserForm());
                }
            }
        }
        return forms;
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("futmatchdevelopers@gmail.com");
        message.setTo(email);
        message.setSubject("Restablecimiento de contraseña");
        message.setText("Para restablecer tu contraseña, haz clic en el siguiente enlace: "
                + "http://localhost:3000/resetPassword?token=" + resetToken);
        mailSender.send(message);

        return resetToken;
    }

    public String resetPassword(String resetToken, String newPassword) {
        User user = userRepository.findByResetToken(resetToken);
        if (user == null) {
            throw new RuntimeException("Invalid reset token");
        }

        // Actualizar la contraseña del usuario
        user.setPassword(newPassword);
        userRepository.save(user);

        return "Password reset successfully";
    }
}
