package com.example.backend.controller;

import com.example.backend.model.Address;
import com.example.backend.model.Payment;
import com.example.backend.service.UserSettingsPageService;
import com.example.backend.viewModel.UserSettingsPageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-settings")
public class UserSettingsController {

    private final UserSettingsPageService userSettingsService;

    @Autowired
    public UserSettingsController(UserSettingsPageService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserSettingsPageViewModel>> getUserSettings(@RequestHeader("userId") int userId) {
        return userSettingsService.getUserSettings(userId);
    }

    @PutMapping("/profile/name")
    public ResponseEntity<Map<String, String>> updateUserProfile(
            @RequestHeader("userId") int userId,
            @RequestBody Map<String, String> request) {

        String fullName = request.get("fullName");
        Map<String, String> profile = new HashMap<>();

        if (fullName != null) {
            userSettingsService.updateUserProfile(userId, fullName);
            profile.put("fullName", fullName);
            return ResponseEntity.status(HttpStatus.OK).body(profile);
        }
        profile.put("error", "Invalid fullName");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(profile);
    }

    @PutMapping("/profile/role")
    public ResponseEntity<Map<String, String>> updateUserRole(
            @RequestHeader("userId") int performerUserId,
            @RequestBody Map<String, String> request) {

        String roleName = request.get("roleName");
        String targetUserId = request.get("targetUserId");
        if (roleName == null || targetUserId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        int targetUserIdInt = Integer.parseInt(targetUserId);

        return userSettingsService.updateUserRole(performerUserId, targetUserIdInt, roleName);
    }

    // --- Address Endpoints

    @PostMapping("/addresses")
    public ResponseEntity<?> addAddress(
            @RequestHeader("userId") int userId,
            @RequestBody Map<String, Object> request) {
        return addOrUpdateAddress(userId, request);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<?> updateAddress(
            @RequestHeader("userId") int userId,
            @PathVariable("addressId") int addressId,
            @RequestBody Map<String, Object> request) {
        request.put("id", addressId);
        return addOrUpdateAddress(userId, request);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(
            @RequestHeader("userId") int userId,
            @PathVariable("addressId") int addressId) {
        userSettingsService.deleteAddress(userId, addressId);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted address");
    }

    private ResponseEntity<?> addOrUpdateAddress(
            @RequestHeader("userId") int userId,
            @RequestBody Map<String, Object> request) {

        Integer addressId = request.containsKey("id") && request.get("id") != null
                ? Integer.valueOf(request.get("id").toString())
                : null;
        String fullName = (String) request.get("fullName");
        String addressLine1 = (String) request.get("addressLine1");
        String addressLine2 = (String) request.get("addressLine2");
        String city = (String) request.get("city");
        String postalCode = (String) request.get("postalCode");
        String country = (String) request.get("country");
        String phoneNumber = (String) request.get("phoneNumber");

        if (fullName == null || fullName.isBlank()
                || addressLine1 == null || addressLine1.isBlank()
                || city == null || city.isBlank()
                || postalCode == null || postalCode.isBlank()
                || country == null || country.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields");
        }

        try {
            Address address = userSettingsService.addOrUpdateAddress(
                    userId,
                    addressId,
                    fullName,
                    addressLine1,
                    addressLine2,
                    city,
                    postalCode,
                    country,
                    phoneNumber
            );
            return ResponseEntity.status(addressId == null ? HttpStatus.CREATED : HttpStatus.OK).body(address);
        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (RuntimeException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // --- Payment Method Endpoints

    @PostMapping("/payments")
    public ResponseEntity<?> addPaymentMethod(
            @RequestHeader("userId") int userId,
            @RequestBody Map<String, Object> request) {
        return addOrUpdatePaymentMethod(userId, request);
    }

    @PutMapping("/payments/{paymentId}")
    public ResponseEntity<?> updatePaymentMethod(
            @RequestHeader("userId") int userId,
            @PathVariable("paymentId") int paymentId,
            @RequestBody Map<String, Object> request) {
        request.put("id", paymentId);
        return addOrUpdatePaymentMethod(userId, request);
    }

    private ResponseEntity<?> addOrUpdatePaymentMethod(
            @RequestHeader("userId") int userId,
            @RequestBody Map<String, Object> request) {
        try {

            Integer paymentId = request.containsKey("id") && request.get("id") != null
                    ? Integer.valueOf(request.get("id").toString())
                    : null;
            String paymentMethodName = (String) request.get("name");

            if (paymentMethodName == null || paymentMethodName.isBlank()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields");
            }

            Payment paymentMethod = userSettingsService.addOrUpdatePaymentMethod(userId, paymentId, paymentMethodName);
            return ResponseEntity.status(paymentId == null ? HttpStatus.CREATED : HttpStatus.OK).body(paymentMethod);
        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<?> deletePaymentMethod(
            @RequestHeader("userId") int userId,
            @PathVariable("paymentId") int paymentId) {
        userSettingsService.deletePaymentMethod(userId, paymentId);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted payment method");
    }

}