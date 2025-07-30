package com.example.backend.service;

import com.example.backend.dao.AddressDAO;
import com.example.backend.dao.PaymentDAO;
import com.example.backend.dao.RoleDAO;
import com.example.backend.dao.UserDAO;
import com.example.backend.model.Address;
import com.example.backend.model.Payment;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.viewModel.UserSettingsPageViewModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.backend.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.backend.utils.JwtUtil.getUserHighestPermissions;

@Service
public class UserSettingsPageService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final AddressDAO addressDAO;
    private final PaymentDAO paymentDAO;

    @Autowired
    public UserSettingsPageService(UserDAO userDAO, AddressDAO addressDAO, PaymentDAO paymentDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.addressDAO = addressDAO;
        this.paymentDAO = paymentDAO;
        this.roleDAO = roleDAO;
    }

    @Transactional
    public UserSettingsPageViewModel getUserSettings(int userId) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        User profileViewModel = new User();
        profileViewModel.setFullName(user.getFullName());
        profileViewModel.setEmail(user.getEmail());

        List<Address> addresses = addressDAO.findByUser_IdAndIsActive(userId, true);
        List<Payment> payments = paymentDAO.findByUser_IdAndIsActive(userId, true);
        List<Role> userRoles = roleDAO.getUserRoles(userId);
        List<Role.RoleName> userRoleNames = userRoles.stream().map(Role::getRoleName).toList();

        UserSettingsPageViewModel viewModel = new UserSettingsPageViewModel();
        viewModel.setUser(profileViewModel);
        if (userRoleNames.contains(Role.RoleName.ADMIN)) {
            viewModel.setRoleName(Role.RoleName.ADMIN.toString());
        } else if (userRoleNames.contains(Role.RoleName.SELLER)) {
            viewModel.setRoleName(Role.RoleName.SELLER.toString());
        } else {
            viewModel.setRoleName(Role.RoleName.CUSTOMER.toString());
        }
        viewModel.setAddresses(addresses);
        viewModel.setPayments(payments);

        return viewModel;
    }

    @Transactional
    public void updateUserProfile(int userId, String fullName) {
        userDAO.updateFullNameById(userId, fullName);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateUserRole(int performingUserId,
                                                              int targetUserId,
                                                              String roleName) {
        Map<String, String> response = new HashMap<>();
        List<Role> performerRoles = roleDAO.getUserRoles(performingUserId);
        if (performerRoles == null || performerRoles.isEmpty()) {
            response.put("message", "User not found with id: " + performingUserId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        List<Role.RoleName> performerRoleNames = performerRoles.stream().map(Role::getRoleName).toList();
        if (performingUserId != targetUserId && !performerRoleNames.contains(Role.RoleName.ADMIN)) {
            response.put("message", "User is not allowed to change role");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        Role targetRole = roleDAO.findByName(roleName);
        if (targetRole == null) {
            response.put("message", "Role not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<Role> targetUserRoles = roleDAO.getUserRoles(targetUserId);
        if (targetUserRoles == null || targetUserRoles.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (targetUserRoles.contains(targetRole)) {
            response.put("message", "Role already exists");
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(response);
        }

        List<Role.RoleName> allowList = Arrays.asList(Role.RoleName.SELLER, Role.RoleName.CUSTOMER);
        if (allowList.contains(targetRole.getRoleName())) {
            roleDAO.assignRoleToUser(targetUserId, targetRole.getId());

            if (performingUserId == targetUserId) {
                // Generate new token on the spot when User changes his own profile settings
                List<Role> updatedUserRoles = roleDAO.getUserRoles(targetUserId);
                User targetUser = userDAO.findById(targetUserId).orElseThrow(() -> new RuntimeException("User not found"));
                String token = getUserHighestPermissions(updatedUserRoles, targetUser);
                response.put("token", token);
            }
            response.put("roleName", targetRole.getRoleName().toString());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        response.put("message", "User is not allowed to change role");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @Transactional
    public Address addOrUpdateAddress(
            int userId,
            Integer addressId,
            String fullName,
            String addressLine1,
            String addressLine2,
            String city,
            String postalCode,
            String country,
            String phoneNumber
    ) {
        Address address;
        if (addressId != null) {
            // Update existing address
            Optional<Address> optionalAddress = addressDAO.findById(addressId);
            if (optionalAddress.isEmpty() || optionalAddress.get().getUser() == null
                    || optionalAddress.get().getUser().getId() != userId) {
                throw new RuntimeException("Address not found or does not belong to user");
            }
            address = optionalAddress.get();
        } else {
            // Add new address
            address = new Address();
            User user = userDAO.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            address.setUser(user);
            address.setIsActive(true);
        }

        address.setFullName(fullName);
        address.setAddressLine1(addressLine1);
        address.setAddressLine2(addressLine2);
        address.setCity(city);
        address.setPostalCode(postalCode);
        address.setCountry(country);
        address.setPhoneNumber(phoneNumber);

        return addressDAO.save(address);
    }

    @Transactional
    public void deleteAddress(int userId, int addressId) {
        Optional<Address> optionalAddress = addressDAO.findById(addressId);
        if (optionalAddress.isEmpty() || optionalAddress.get().getUser() == null
                || optionalAddress.get().getUser().getId() != userId) {
            throw new RuntimeException("Address not found or does not belong to user");
        }
        Address address = optionalAddress.get();
        address.setIsActive(false);
        addressDAO.save(address);
    }

    // --- Payment methods ---

    @Transactional
    public Payment addOrUpdatePaymentMethod(
            int userId,
            Integer paymentId,
            String paymentMethod
    ) {
        Payment payment;
        if (paymentId != null) {
            // Update existing payment method
            Optional<Payment> optionalPayment = paymentDAO.findById(paymentId);
            if (optionalPayment.isEmpty() || optionalPayment.get().getUser() == null
                    || optionalPayment.get().getUser().getId() != userId) {
                throw new RuntimeException("Payment method not found or does not belong to user");
            }
            payment = optionalPayment.get();
        } else {
            // Add new payment method
            payment = new Payment();
            User user = userDAO.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            payment.setUser(user);
            payment.setIsActive(true);
        }

        payment.setName(paymentMethod);

        return paymentDAO.save(payment);
    }

    @Transactional
    public void deletePaymentMethod(int userId, int paymentId) {
        Optional<Payment> optionalPayment = paymentDAO.findById(paymentId);
        if (optionalPayment.isEmpty() || optionalPayment.get().getUser() == null
                || optionalPayment.get().getUser().getId() != userId) {
            throw new RuntimeException("Payment method not found or does not belong to user");
        }
        Payment payment = optionalPayment.get();
        payment.setIsActive(false);
        paymentDAO.save(payment);
    }
}
