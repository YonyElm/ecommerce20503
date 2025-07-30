package com.example.backend.utils;

import com.example.backend.model.Role;
import com.example.backend.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;

public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

    public static String generateToken(int userId, String email, Role.RoleName userRole) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("roleName", userRole)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public static String getUserHighestPermissions(List<Role> updatedUserRoles, User targetUser) {
        List<Role.RoleName> roleNames = updatedUserRoles.stream()
                .map(Role::getRoleName).toList();
        Role.RoleName userRole;

        if (roleNames.contains(Role.RoleName.ADMIN)) {
            userRole = Role.RoleName.ADMIN;
        } else if (roleNames.contains(Role.RoleName.SELLER)) {
            userRole = Role.RoleName.SELLER;
        } else {
            userRole = Role.RoleName.CUSTOMER;
        }
        return JwtUtil.generateToken(targetUser.getId(), targetUser.getEmail(), userRole);
    }
}
