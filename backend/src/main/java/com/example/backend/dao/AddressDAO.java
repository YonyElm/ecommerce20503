package com.example.backend.dao;

import com.example.backend.model.Address;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressDAO extends JpaRepository<Address, Long> {
    List<Address> findByUserIdAndIsActive(int userId, Boolean isActive);
}