package com.example.backend.controller;

import com.example.backend.service.CheckoutPageService;
import com.example.backend.viewModel.CheckoutPageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutPageService checkoutPageService;

    @Autowired
    public CheckoutController(CheckoutPageService checkoutPageService) {
        this.checkoutPageService = checkoutPageService;
    }

    @GetMapping
    public ResponseEntity<CheckoutPageViewModel> getCheckoutPageData(@RequestHeader("userId") int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(checkoutPageService.getCheckoutPageDataByUserId(userId));
    }
}