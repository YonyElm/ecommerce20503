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
import java.util.Optional;

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
        User user = new User(1, "test@example.com", "password123"
                , "Test User", new Timestamp(System.currentTimeMillis()), true);
        Role customerRole = Role.builder()
                .roleName(Role.RoleName.CUSTOMER)
                .id(1)
                .build();

        when(userDAO.existsByEmail(user.getEmail())).thenReturn(false);
        when(userDAO.save(any())).thenReturn(user);
        when(roleDAO.findByName("CUSTOMER")).thenReturn(customerRole);

        doNothing().when(roleDAO).assignRoleToUser(user.getId(), customerRole.getId());
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("User registered successfully")));
    }

    @Test
    void testLoginSuccess() throws Exception {
        User user = new User(1, "test@example.com", null
                , "Test User", new Timestamp(System.currentTimeMillis()), true);

        // Use BCryptPasswordEncoder to hash the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String correctPasswordHash = passwordEncoder.encode("password123"); // Hash the correct password
        user.setPasswordHash(correctPasswordHash);

        // Mock that email exists
        when(userDAO.findByEmailAndIsActiveTrue(any())).thenReturn(Optional.of(user));

        // try login
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testLoginFailure() throws Exception {
        User user = new User(1, "test@example.com", null
                , "Test User", new Timestamp(System.currentTimeMillis()), true);

        // Use BCryptPasswordEncoder to hash the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String correctPasswordHash = passwordEncoder.encode("wrongPassword");
        user.setPasswordHash(correctPasswordHash);

        // Mock that email exists
        when(userDAO.findByEmailAndIsActiveTrue(any())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
            .andExpect(status().isUnauthorized());
    }
}
