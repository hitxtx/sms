package com.example.ms.service;

import com.example.ms.model.User;

import java.util.Optional;

public interface UserService {

    void increaseFailedCount(User user);

    void resetFailedCount(Long userId);

    void lockAccount(User user);

    boolean unlockWhenLockExpired(User user);

    Optional<User> getByUsername(String username);
}
