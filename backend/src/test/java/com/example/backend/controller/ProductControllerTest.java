package com.example.backend.controller;

import com.example.backend.model.Product;
import com.example.backend.service.DetailPageService;
import com.example.backend.service.HomeProductsService;
import com.example.backend.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private HomeProductsService homeProductsService;

    @MockBean
    private DetailPageService detailPageService;

    @MockBean
    private StoreService storeService;

    private Product validProduct;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        validProduct = new Product();
        validProduct.setId(1);
        validProduct.setName("Test Product");
        validProduct.setDescription("A demo item");
        validProduct.setPrice(BigDecimal.valueOf(20.5));
        validProduct.setIsActive(true);
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(homeProductsService.getAllProducts())
                .thenReturn(Collections.singletonList(validProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void testCreateProductInvalid() throws Exception {
        Product invalid = new Product(0, "", null, BigDecimal.valueOf(-10), null, null, null, null, null);

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
