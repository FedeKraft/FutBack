package com.example.demo.domains.auth.controller;

import com.example.demo.domains.auth.dto.LoginUserDTO;
import com.example.demo.domains.auth.dto.RegisterUserDTO;
import com.example.demo.domains.auth.dto.TokenDTO;
import com.example.demo.domains.auth.service.AuthService;
import com.example.demo.domains.auth.utils.JWTvalidator;
import com.example.demo.domains.user.model.Notification;
import com.example.demo.domains.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTvalidator jwtValidator;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;


    }
    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterUserDTO registerUserDTO) {
       return new ResponseEntity<>(authService.register(registerUserDTO), HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginUserDTO loginUserDTO) {
        return new ResponseEntity<>(authService.login(loginUserDTO), HttpStatus.OK);
    }
    @GetMapping("/auth/profile")
    public ResponseEntity<RegisterUserDTO> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtValidator.getID(token);
            RegisterUserDTO user = authService.getUserProfile(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            // Token validation failed
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/auth/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtValidator.getID(token);
            List<User> users = authService.getAllUsers(userId);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            // Token validation failed
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/api/notifications")
    public List<Notification> getNotifications(@RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtValidator.getID(token);
            return authService.getNotificationsByUserId(userId);
        } catch (Exception e) {
            // Token validation failed
            throw new RuntimeException("Unauthorized");
        }
    }

}