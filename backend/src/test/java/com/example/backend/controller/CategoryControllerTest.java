package com.example.backend.controller;

import com.example.backend.model.Category;
import com.example.backend.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAllCategories() throws Exception {
        when(adminService.getAllCategories()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testCreateCategory() throws Exception {
        int userId = 1;
        String categoryName = "Books";
        Map<String, String> body = Map.of("categoryName", categoryName);
        ApiResponse<Category> apiResponse = new ApiResponse<>(true, null, null);
        ResponseEntity<ApiResponse<Category>> responseEntity = ResponseEntity.ok(apiResponse);
        when(adminService.createCategory(userId, categoryName)).thenReturn(responseEntity);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testUpdateCategory() throws Exception {
        int userId = 1;
        int categoryId = 2;
        String categoryName = "Updated";
        Map<String, String> body = Map.of("categoryName", categoryName);
        ApiResponse<Category> apiResponse = new ApiResponse<>(true, null, null);
        ResponseEntity<ApiResponse<Category>> responseEntity = ResponseEntity.ok(apiResponse);
        when(adminService.updateCategory(userId, categoryId, categoryName)).thenReturn(responseEntity);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/" + categoryId)
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testDeleteCategory() throws Exception {
        int userId = 1;
        int categoryId = 2;
        ResponseEntity<ApiResponse<Category>> responseEntity = ResponseEntity.ok().build();
        when(adminService.deleteCategory(userId, categoryId)).thenReturn(responseEntity);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/" + categoryId)
                .header("userId", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
