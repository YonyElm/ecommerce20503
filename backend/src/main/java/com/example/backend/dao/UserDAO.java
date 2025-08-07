package com.example.backend.dao;

import com.example.backend.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    /**
     * Finds an active user by email.
     * @param email The email address to search for
     * @return Optional containing the User if found and active, or empty otherwise
     */
    Optional<User> findByEmailAndIsActiveTrue(String email);

    /**
     * Finds a user by their ID.
     * @param userId The ID of the user
     * @return Optional containing the User if found, or empty otherwise
     */
    Optional<User> findById(int userId);

    /**
     * Finds an active user by their ID.
     * @param userId The ID of the user
     * @return Optional containing the User if found and active, or empty otherwise
     */
    Optional<User> findByIdAndIsActiveTrue(int userId);

    @NonNull
    List<User> findAll();

    /**
     * Checks if a user exists by email.
     * @param email The email address to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Updates the full name of a user by their ID.
     * @param userId The ID of the user
     * @param fullName The new full name to set
     * @return The number of rows updated
     */
    @Modifying
    @Query("UPDATE User u SET u.fullName = :fullName WHERE u.id = :userId")
    int updateFullNameById(int userId, String fullName);
}