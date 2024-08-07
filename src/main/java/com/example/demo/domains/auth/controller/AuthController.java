package com.example.demo.domains.auth.controller;

import com.example.demo.domains.auth.dto.*;
import com.example.demo.domains.auth.service.AuthService;
import com.example.demo.domains.auth.utils.JWTvalidator;
import com.example.demo.domains.dataBase.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTvalidator jwtValidator;


    @Autowired
    public AuthController(AuthService authService, JWTvalidator jwtValidator) {
        this.authService = authService;
        this.jwtValidator = jwtValidator;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterUserDTO registerUserDTO) {
        return new ResponseEntity<>(authService.register(registerUserDTO), HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginUserDTO loginUserDTO) {
        if (authService.login(loginUserDTO) == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(authService.login(loginUserDTO), HttpStatus.OK);
    }

    @PostMapping("/auth/googleLogin")
    public ResponseEntity<?> googleLogin(@RequestBody LoginUserDTO loginUserDTO) {
        if (authService.googleLogin(loginUserDTO) == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(authService.googleLogin(loginUserDTO), HttpStatus.OK);
    }

    @GetMapping("/auth/profile")
    public ResponseEntity<RegisterUserDTO> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtValidator.getID(token);
            RegisterUserDTO user = authService.getUserProfile(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
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
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/api/notifications")
    public List<Notification> getNotifications(@RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtValidator.getID(token);
            return authService.getNotificationsByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Unauthorized");
        }
    }

    @GetMapping("/auth/users/{id}")
    public ResponseEntity<RegisterUserDTO> getUserById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            RegisterUserDTO user = authService.getUserProfile(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/auth/match")
    public ResponseEntity<String> createMatch(@RequestBody MatchDTO matchDTO, @RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            authService.createMatch(matchDTO.fromUserId, matchDTO.toUserId);
            return new ResponseEntity<>("Match iniciado", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/matches/accept")
    public ResponseEntity<String> acceptMatch(@RequestBody Map<String, Long> body, @RequestHeader("Authorization") String token) {
        try {
            Long matchId = body.get("matchId");
            jwtValidator.getID(token);
            authService.acceptMatch(matchId);
            return new ResponseEntity<>("Match aceptado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/matches/reject")
    public ResponseEntity<String> rejectMatch(@RequestBody Map<String, Long> body, @RequestHeader("Authorization") String token) {
        try {
            Long matchId = body.get("matchId");
            jwtValidator.getID(token);
            authService.rejectMatch(matchId);
            return new ResponseEntity<>("Match rechazado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/auth/EditProfile")
    public ResponseEntity<RegisterUserDTO> editUserProfile(@RequestHeader("Authorization") String token, @RequestBody RegisterUserDTO registerUserDTO) {
        try {
            Long userId = jwtValidator.getID(token);
            RegisterUserDTO updatedUser = authService.editUserProfile(userId, registerUserDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/auth/users/toggle-status")
    public ResponseEntity<String> toggleUserStatus(@RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtValidator.getID(token);
            authService.toggleUserStatus(userId);
            return new ResponseEntity<>("User status toggled", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/auth/form")
    public ResponseEntity<String> createForm(@RequestBody FormDTO formDTO, @RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            authService.createForm(formDTO);
            return new ResponseEntity<>("Formulario creado", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/notifications/responded")
    public ResponseEntity<String> cancelMatch(@RequestBody Map<String, Long> body, @RequestHeader("Authorization") String token) {
        try {
            Long notificationId = body.get("notificationId");
            jwtValidator.getID(token);
            authService.cancelMatch(notificationId);
            return new ResponseEntity<>("Match cancelado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/api/ranking")
    public List<User> getRanking(@RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            return authService.getRanking();
        } catch (Exception e) {
            throw new RuntimeException("Unauthorized");
        }
    }

    @GetMapping("/auth/match-history")
    public ResponseEntity<List<Match>> getMatchHistory(@RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtValidator.getID(token);
            List<Match> matchHistory = authService.getMatchHistory(userId);
            return new ResponseEntity<>(matchHistory, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/auth/users/{id}/incidents")
    public ResponseEntity<List<Form>> getUserIncidents(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            List<Form> incidents = authService.getIncidents(id);
            return new ResponseEntity<>(incidents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/auth/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String resetToken = authService.forgotPassword(email);
            return new ResponseEntity<>(resetToken, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> body) {
        try {
            String resetToken = body.get("token");
            String newPassword = body.get("password");
            String message = authService.resetPassword(resetToken, newPassword);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al intentar restablecer la contraseña", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/report")
    public ResponseEntity<String> createReport(@RequestBody ReportDTO reportDTO, @RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            authService.createReport(reportDTO.fromUserId, reportDTO.toUserId, reportDTO.comment);
            return new ResponseEntity<>("Reporte creado", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/auth/reports")
    public ResponseEntity<List<Report>> getAllReports(@RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            List<Report> reports = authService.getAllReports();
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/auth/deleteReport/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            authService.deleteReport(id);
            return new ResponseEntity<>("Reporte eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/auth/winReport/{id}")
    public ResponseEntity<String> winReport(@PathVariable Long id, @RequestBody Map<String, Report> body, @RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            authService.winReport(body.get("report"), id);
            return new ResponseEntity<>("Reporte solucionado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/auth/deleteForms/{id}")
    public ResponseEntity<String> drawReport(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            authService.drawReport(id);
            return new ResponseEntity<>("Reporte eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/auth/suspend/{id}")
    public ResponseEntity<String> suspendUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            authService.suspendUser(id);
            return new ResponseEntity<>("Usuario suspendido", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/auth/suspended")
    public ResponseEntity<List<User>> getSuspendedUsers(@RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            List<User> suspendedUsers = authService.getSuspendedUsers();
            return new ResponseEntity<>(suspendedUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/auth/activate/{id}")
    public ResponseEntity<String> activateUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            jwtValidator.getID(token);
            authService.activateUser(id);
            return new ResponseEntity<>("Usuario activado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}