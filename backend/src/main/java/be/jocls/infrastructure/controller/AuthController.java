package be.jocls.infrastructure.controller;

import be.jocls.application.dto.LoginRequestDTO;
import be.jocls.application.dto.LoginResponseDTO;
import be.jocls.application.service.UserService;
import be.jocls.domain.model.User;
import be.jocls.domain.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (user!=null) {
            return ResponseEntity.ok(new LoginResponseDTO(user.getUserRole().name()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}

