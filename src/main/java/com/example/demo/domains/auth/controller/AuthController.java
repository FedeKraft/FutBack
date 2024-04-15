package com.example.demo.domains.auth.controller;

import com.example.demo.domains.auth.dto.RegisterUserDTO;
import com.example.demo.domains.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterUserDTO registerUserDTO) {
       return new ResponseEntity<>(authService.register(registerUserDTO), HttpStatus.CREATED);
    }
    @PostMapping("/auth/login")
    public ResponseEntity<String> login() {
        return new ResponseEntity<>("Login", HttpStatus.OK);
    }

}