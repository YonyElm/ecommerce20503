package com.example.backend.controller;

import com.example.backend.service.CheckoutPageService;
import com.example.backend.service.PlaceOrderService;
import com.example.backend.viewModel.CheckoutPageViewModel;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutPageService checkoutPageService;
    private final PlaceOrderService orderService;

    @Autowired
    public CheckoutController(CheckoutPageService checkoutPageService, PlaceOrderService orderService) {
        this.checkoutPageService = checkoutPageService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<CheckoutPageViewModel> getCheckoutPageData(@RequestHeader("userId") int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(checkoutPageService.getCheckoutPageDataByUserId(userId));
    }

    @PostMapping("/cart")
    public ResponseEntity<String> placeCartOrder(@RequestHeader("userId") int userId, @RequestBody Map<String,String> request) {
        try {
            int addressId = request.get("addressId") != null ? Integer.parseInt(request.get("addressId")) : -1;
            int paymentId = request.get("paymentId") != null ? Integer.parseInt(request.get("paymentId")) : -1;

            if (addressId < 0 || paymentId < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid address or payment method");
            }
            orderService.placeCartOrder(userId, addressId, paymentId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/buyitnow")
    public ResponseEntity<String> placeBuyItNowOrder(@RequestHeader("userId") int userId, @RequestBody Map<String,String> request) {
        try {
            int addressId = request.get("addressId") != null ? Integer.parseInt(request.get("addressId")) : -1;
            int paymentId = request.get("paymentId") != null ? Integer.parseInt(request.get("paymentId")) : -1;
            int productId = request.get("productId") != null ? Integer.parseInt(request.get("productId")) : -1;
            int quantity = request.get("quantity") != null ? Integer.parseInt(request.get("quantity")) : -1;

            if (addressId < 0 || paymentId < 0 || productId < 0 || quantity < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid address or payment method or product details");
            }
            orderService.placeBuyItNowOrder(userId, addressId, paymentId, productId, quantity);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}