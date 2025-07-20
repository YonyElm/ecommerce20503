package com.example.backend.dao;

import com.example.backend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressDAO extends JpaRepository<Address, Integer> {
    List<Address> findByUser_IdAndIsActive(int userId, Boolean isActive);
}