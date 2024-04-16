package com.example.demo.domains.auth.service;


import com.example.demo.domains.auth.dto.LoginUserDTO;
import com.example.demo.domains.auth.dto.RegisterUserDTO;
import com.example.demo.domains.user.model.Token;
import com.example.demo.domains.user.model.User;
import com.example.demo.domains.user.repository.TokenRepository;
import com.example.demo.domains.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;

    }
    public String register(RegisterUserDTO registerUserDTO) {
        User user = new User(registerUserDTO.name, registerUserDTO.email, registerUserDTO.password, registerUserDTO.localidad, registerUserDTO.playerAmount, registerUserDTO.numero);
        userRepository.save(user);
        return "User registered successfully";
    }
    public String login(LoginUserDTO loginUserDTO) {
        User user = userRepository.findByEmail(loginUserDTO.email);
        if (user == null) {
            return "User not found";
        }
        if (user.getPassword().equals(loginUserDTO.password)) {
            // create token (sessionsTable -:: userId, token, expiryDate
            if (user.getPassword().equals(loginUserDTO.password)) {
                //System.out.println("debug");
                // create token
                Token token = new Token();
                //System.out.println("token created");
                token.setToken(UUID.randomUUID().toString());
                //System.out.println("token set");
                token.setUser(user);
                // set expiration date for 1 hour from now
                Date expirationDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
                token.setExpirationDate(expirationDate);
                // save token
                //System.out.println(token.getToken());
                tokenRepository.save(token);
                // return token
                return token.getToken();
            }
            return "Login successful";
        }
        return "Login failed";
    }
}
