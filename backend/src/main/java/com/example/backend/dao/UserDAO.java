package com.example.backend.dao;

import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    Optional<User> findByEmailAndIsActiveTrue(String email);
    Optional<User> findById(int userId);
    List<User> findAll();
    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.fullName = :fullName WHERE u.id = :userId")
    int updateFullNameById(int userId, String fullName);
}