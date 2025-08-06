package com.example.backend.viewModel;

import com.example.backend.model.Address;
import com.example.backend.model.Payment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutPageViewModel {
    private List<Address> shippingAddressList;
    private List<Payment> paymentMethodList;
}