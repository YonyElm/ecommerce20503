package com.example.backend.controller;

import com.example.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final AdminService adminService;

    @Autowired
    public UsersController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUsers(@RequestHeader("userId") int userId) {
        return adminService.getUsers(userId);
    }

    @PutMapping("/activate")
    public ResponseEntity<Map<String, Object>> updateUserRole(
            @RequestHeader("userId") int performerUserId,
            @RequestBody Map<String, String> request) {

        String action = request.get("action");
        boolean actionFlag = Boolean.parseBoolean(request.get("action"));
        String targetUserId = request.get("targetUserId");
        if (action == null || targetUserId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        int targetUserIdInt = Integer.parseInt(targetUserId);

        return adminService.userActivation(performerUserId, targetUserIdInt, actionFlag);
    }

}
