package com.example.backend.viewModel;

import com.example.backend.model.Address;
import com.example.backend.model.Payment;
import com.example.backend.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserSettingsPageViewModel {
    private User user;
    private String roleName;
    private List<Address> addresses;
    private List<Payment> payments;
}