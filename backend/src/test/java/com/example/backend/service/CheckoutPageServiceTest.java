package com.example.backend.service;

import com.example.backend.dao.AddressDAO;
import com.example.backend.dao.PaymentDAO;
import com.example.backend.model.Address;
import com.example.backend.model.Payment;
import com.example.backend.viewModel.CheckoutPageViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutPageServiceTest {
    @Mock
    private AddressDAO addressDAO;
    @Mock
    private PaymentDAO paymentDAO;
    @InjectMocks
    private CheckoutPageService checkoutPageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCheckoutPageDataByUserId() {
        int userId = 1;
        List<Address> addresses = List.of(new Address());
        List<Payment> payments = List.of(new Payment());
        when(addressDAO.findByUser_IdAndIsActive(userId, true)).thenReturn(addresses);
        when(paymentDAO.findByUser_IdAndIsActive(userId, true)).thenReturn(payments);
        CheckoutPageViewModel result = checkoutPageService.getCheckoutPageDataByUserId(userId);
        assertEquals(addresses, result.getShippingAddressList());
        assertEquals(payments, result.getPaymentMethodList());
    }
}
