package com.example.backend.service;

import com.example.backend.dao.AddressDAO;
import com.example.backend.dao.PaymentDAO;
import com.example.backend.model.Address;
import com.example.backend.model.Payment;
import com.example.backend.viewModel.CheckoutPageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutPageService {

    private final AddressDAO addressDAO;
    private final PaymentDAO paymentDAO;

    @Autowired
    public CheckoutPageService(AddressDAO addressDAO, PaymentDAO paymentDAO) {
        this.addressDAO = addressDAO;
        this.paymentDAO = paymentDAO;
    }

    public CheckoutPageViewModel getCheckoutPageDataByUserId(int userId) {
        List<Address> addresses = addressDAO.findByUser_IdAndIsActive(userId, true);
        List<Payment> payments = paymentDAO.findByUser_IdAndIsActive(userId, true);

        CheckoutPageViewModel viewModel = new CheckoutPageViewModel();

        viewModel.setShippingAddressList(addresses);
        viewModel.setPaymentMethodList(payments);
        return viewModel;
    }
}