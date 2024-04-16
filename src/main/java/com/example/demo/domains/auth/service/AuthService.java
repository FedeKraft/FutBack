package com.example.demo.domains.auth.service;


import com.example.demo.domains.auth.dto.LoginUserDTO;
import com.example.demo.domains.auth.dto.RegisterUserDTO;
import com.example.demo.domains.user.model.User;
import com.example.demo.domains.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
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
    public String login(RegisterUserDTO registerUserDTO) {
        User user = userRepository.findByName(registerUserDTO.name);
        if (user == null) {
            return "User not found";
        }
        if (user.getPassword().equals(registerUserDTO.password)) {
            return "Login successful";
        }
        return "Login failed";
    }
}
