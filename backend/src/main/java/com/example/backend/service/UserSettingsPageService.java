package com.example.backend.service;

import com.example.backend.dao.AddressDAO;
import com.example.backend.dao.PaymentDAO;
import com.example.backend.dao.UserDAO;
import com.example.backend.model.Address;
import com.example.backend.model.Payment;
import com.example.backend.model.User;
import com.example.backend.viewModel.UserSettingsPageViewModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSettingsPageService {

    private final UserDAO userDAO;
    private final AddressDAO addressDAO;
    private final PaymentDAO paymentDAO;

    @Autowired
    public UserSettingsPageService(UserDAO userDAO, AddressDAO addressDAO, PaymentDAO paymentDAO) {
        this.userDAO = userDAO;
        this.addressDAO = addressDAO;
        this.paymentDAO = paymentDAO;
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

        UserSettingsPageViewModel viewModel = new UserSettingsPageViewModel();
        viewModel.setUser(profileViewModel);
        viewModel.setAddresses(addresses);
        viewModel.setPayments(payments);

        return viewModel;
    }

    @Transactional
    public void updateUserProfile(int userId, String fullName) {
        userDAO.updateFullNameById(userId, fullName);
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