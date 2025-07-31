package com.example.backend.service;

import com.example.backend.dao.*;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.backend.utils.JwtUtil.getUserHighestPermissions;

@Service
public class AdminService {


    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    public AdminService(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getUsers(int userId) {
        Map<String, Object> resultMap = new HashMap<>();

        Optional<User> userOptional = userDAO.findById(userId);
        if (userOptional.isEmpty()) {
            logger.warn("User not found");
            resultMap.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        }

        List<Role> roles = roleDAO.getUserRoles(userId);
        boolean isAdmin = roles.stream().anyMatch(role -> Role.RoleName.ADMIN.equals(role.getRoleName()));
        List<User> allUsers;

        if (isAdmin) {
            allUsers = userDAO.findAll();
            List<Role.RoleName> usersRoles = new ArrayList<>();
            // Filter out admin users
            List<User> users = allUsers.stream()
                    .filter(user -> {
                        List<Role> userRoles = roleDAO.getUserRoles(user.getId());
                        if (userRoles.stream().noneMatch(role -> Role.RoleName.ADMIN.equals(role.getRoleName()))){
                            if (userRoles.stream().noneMatch(role -> Role.RoleName.SELLER.equals(role.getRoleName()))) {
                                usersRoles.add(Role.RoleName.CUSTOMER);
                            } else {
                                usersRoles.add(Role.RoleName.SELLER);
                            }

                            return true;
                        } else {
                            return false;
                        }
                    })
                    .toList();


            resultMap.put("users", users);
            resultMap.put("roleNames", usersRoles);
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } else {
            logger.warn("Non-Admin user tried access to users list {}:", userId);
            resultMap.put("message", "User is not Admin, cant perform this operation");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resultMap);
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> userActivation(int performingUserId,
                                                              int targetUserId,
                                                              boolean action) {
        Map<String, Object> response = new HashMap<>();
        List<Role> performerRoles = roleDAO.getUserRoles(performingUserId);
        if (performerRoles == null || performerRoles.isEmpty()) {
            response.put("message", "Performing user not found with id: " + performingUserId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        Optional<User> optionalTargetUser = userDAO.findById(targetUserId);
        if (optionalTargetUser.isEmpty()) {
            response.put("message", "Target user not found with id: " + targetUserId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<Role.RoleName> performerRoleNames = performerRoles.stream().map(Role::getRoleName).toList();
        boolean isAdmin = performerRoleNames.contains(Role.RoleName.ADMIN);
        boolean selfAction = performingUserId == targetUserId;
        if ((!selfAction && !isAdmin) || (selfAction && isAdmin)) {
            response.put("message", "User is not allowed to change activation settings");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        User targetUser = optionalTargetUser.get();
        targetUser.setIsActive(action);
        userDAO.save(targetUser);
        response.put("data", action);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
