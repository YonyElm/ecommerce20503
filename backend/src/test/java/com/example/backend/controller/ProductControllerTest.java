package com.example.backend.controller;

import com.example.backend.dao.ProductDAO;
import com.example.backend.dao.RoleDAO;
import com.example.backend.model.Category;
import com.example.backend.model.Product;
import com.example.backend.model.Role;
import com.example.backend.viewModel.DetailPageViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private Product validProduct;

    @MockBean
    private RoleDAO roleDAO;
    @MockBean
    private ProductDAO productDAO;
    @MockBean
    private Category category;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        validProduct = new Product(0, null, null, null, category, null, null, true);
        validProduct.setName("Test Product");
        validProduct.setDescription("A demo item");
        validProduct.setPrice(BigDecimal.valueOf(20.5));
    }

    @Test
    void testGetAllProducts() throws Exception {
        Role adminRole = Role.builder()
                .roleName(Role.RoleName.ADMIN)
                .id(1)
                .build();
        when(roleDAO.getUserRoles(1)).thenReturn(Collections.singletonList(adminRole));
        when(productDAO.findByIsActiveTrue()).thenReturn(Collections.singletonList(validProduct));
        mockMvc.perform(get("/api/products")
                        .header("userId", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void testCreateProductInvalid() throws Exception {
        Product invalid = new Product(0, "", null, BigDecimal.valueOf(-10), category, null, null, null);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    void testCreateProductValid() throws Exception {
//        when(productDAO.addProduct(any())).thenReturn(validProduct);
//        mockMvc.perform(post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(validProduct)))
//                .andExpect(status().isCreated())
//                .andExpect(content().string(containsString("Test Product")));
//    }
}
