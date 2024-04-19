package com.example.demo.domains.auth.service;

import com.example.demo.domains.auth.dto.LoginUserDTO;
import com.example.demo.domains.auth.dto.RegisterUserDTO;
import com.example.demo.domains.auth.dto.TokenDTO;
import com.example.demo.domains.auth.utils.JWTGen;
import com.example.demo.domains.user.model.User;
import com.example.demo.domains.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {
    private final JWTGen jwtGen = new JWTGen();
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    public String register(RegisterUserDTO registerUserDTO) {
        User user = new User(registerUserDTO.name, registerUserDTO.email, registerUserDTO.password, registerUserDTO.localidad, registerUserDTO.playerAmount, registerUserDTO.numero);
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

}
