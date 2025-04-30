package com.example.backend.controller;

import com.example.backend.dao.RoleDAO;
import com.example.backend.dao.UserDAO;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.sql.Timestamp;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private UserDAO userDAO;
    @MockBean
    private RoleDAO roleDAO;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() throws Exception {
        User user = new User(1, "testuser", "password123", "test@example.com"
                , "Test User", new Timestamp(System.currentTimeMillis()), true);
        Role customerRole = new Role("CUSTOMER");

        customerRole.setId(1);
        when(userDAO.existsByUsername(user.getUsername())).thenReturn(false);
        doNothing().when(userDAO).registerUser(user);
        when(userDAO.findByUsername(user.getUsername())).thenReturn(user);
        when(roleDAO.findByName("CUSTOMER")).thenReturn(customerRole);

        doNothing().when(roleDAO).assignRoleToUser(user.getId(), customerRole.getId());
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("User registered successfully")));
    }

    @Test
    void testLoginSuccess() throws Exception {
        User user = new User(1, "loginuser", null, "test@example.com"
                , "Test User", new Timestamp(System.currentTimeMillis()), true);

        // Use BCryptPasswordEncoder to hash the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String correctPasswordHash = passwordEncoder.encode("password123"); // Hash the correct password
        user.setPasswordHash(correctPasswordHash);

        System.out.println("admin: " + passwordEncoder.encode("admin"));
        System.out.println("john_doe: " + passwordEncoder.encode("john_doe"));

        when(userDAO.findByUsername(any())).thenReturn(user);

        // try login
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"loginuser\", \"password\": \"password123\"}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testLoginFailure() throws Exception {
        User user = new User(1, "loginuser", null, "test@example.com"
                , "Test User", new Timestamp(System.currentTimeMillis()), true);

        // Use BCryptPasswordEncoder to hash the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String correctPasswordHash = passwordEncoder.encode("wrongPassword");
        user.setPasswordHash(correctPasswordHash);

        when(userDAO.findByUsername(any())).thenReturn(user);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"loginuser\", \"password\": \"password123\"}"))
            .andExpect(status().isUnauthorized());
    }
}
