package com.example.backend.controller;

import com.example.backend.service.CheckoutPageService;
import com.example.backend.service.PlaceOrderService;
import com.example.backend.viewModel.CheckoutPageViewModel;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckoutPageService checkoutPageService;

    @MockBean
    private PlaceOrderService orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetCheckoutPageData() throws Exception {
        int userId = 1;
        CheckoutPageViewModel viewModel = new CheckoutPageViewModel();
        when(checkoutPageService.getCheckoutPageDataByUserId(userId)).thenReturn(viewModel);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/checkout")
                .header("userId", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void testPlaceCartOrder() throws Exception {
        int userId = 1;
        Map<String, String> request = Map.of("addressId", "2", "paymentId", "3");
        // No exception thrown, should return 201 CREATED
        org.mockito.Mockito.doNothing().when(orderService).placeCartOrder(userId, 2, 3);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/cart")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    void testPlaceBuyItNowOrder() throws Exception {
        int userId = 1;
        Map<String, String> request = Map.of(
                "addressId", "2",
                "paymentId", "3",
                "productId", "4",
                "quantity", "1"
        );
        org.mockito.Mockito.doNothing().when(orderService).placeBuyItNowOrder(userId, 2, 3, 4, 1);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/checkout/buyitnow")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
