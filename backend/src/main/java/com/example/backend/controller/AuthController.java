package com.example.backend.controller;

import com.example.backend.dao.RoleDAO;
import com.example.backend.model.Role;
import com.example.backend.utils.PasswordEncoderUtil;
import com.example.backend.dao.UserDAO;
import com.example.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.backend.utils.JwtUtil.getUserHighestPermissions;

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

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        if (userDAO.existsByEmail(user.getEmail())) {
            response.put("message", "Email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        user.setPasswordHash(PasswordEncoderUtil.encode(user.getPasswordHash()));
        User dbUser = userDAO.save(user);

        int userId = dbUser.getId();
        Role customerRole = roleDAO.findByName("CUSTOMER");
        roleDAO.assignRoleToUser(userId, customerRole.getId());
        response.put("message", "User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginData) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<User> optionalUser = userDAO.findByEmailAndIsActiveTrue(loginData.get("email"));
            if (optionalUser.isEmpty()) {
                response.put("message", "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = optionalUser.get();

            if (PasswordEncoderUtil.matches(loginData.get("password"), user.getPasswordHash())) {
                // Fetch roles of the user
                List<Role> userRoles = roleDAO.getUserRoles(user.getId());
                String token = getUserHighestPermissions(userRoles, user);
                response.put("token", token);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("message", "Wrong password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
