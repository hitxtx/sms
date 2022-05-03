package com.example.ms.repository;

import com.example.ms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.failedCount = ?1 WHERE u.id = ?2")
    int updateFailedCount(Integer failedCount, Long userId);

}
