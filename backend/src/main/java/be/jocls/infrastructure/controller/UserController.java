package be.jocls.infrastructure.controller;

import be.jocls.application.dto.UserRegistrationDTO;
import be.jocls.application.service.UserRegistrationService;
import be.jocls.application.service.UserService;
import be.jocls.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        try {
            userRegistrationService.registerUser(userRegistrationDTO);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
