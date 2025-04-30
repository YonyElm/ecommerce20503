package com.example.backend.controller;

import com.example.backend.dao.RoleDAO;
import com.example.backend.model.Role;
import com.example.backend.utils.JwtUtil;
import com.example.backend.utils.PasswordEncoderUtil;
import com.example.backend.dao.UserDAO;
import com.example.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    @Autowired
    public AuthController(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userDAO.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        user.setPasswordHash(PasswordEncoderUtil.encode(user.getPasswordHash()));
        userDAO.registerUser(user);

        // Assign default CUSTOMER role
        int userId = userDAO.findByUsername(user.getUsername()).getId();
        Role customerRole = roleDAO.findByName("CUSTOMER");
        roleDAO.assignRoleToUser(userId, customerRole.getId());

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            User user = userDAO.findByUsername(loginData.get("username"));
            if (PasswordEncoderUtil.matches(loginData.get("password"), user.getPasswordHash())) {
                // Fetch roles of the user
                List<Role> userRoles = roleDAO.getUserRoles(user.getId());
                List<String> roleNames = userRoles.stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toList());

                // Generate JWT
                String token = JwtUtil.generateToken(user.getId(), user.getUsername(), roleNames);
                return ResponseEntity.ok(Map.of("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }
}
