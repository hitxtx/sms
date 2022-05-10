package com.example.ms.service.impl;

import com.example.ms.component.constant.UserConst;
import com.example.ms.model.User;
import com.example.ms.repository.UserRepository;
import com.example.ms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void increaseFailedCount(User user) {
        int failCount = user.getFailedCount() + 1;
        userRepository.updateFailedCount(failCount, user.getId());
    }

    @Override
    public void resetFailedCount(Long userId) {
        userRepository.updateFailedCount(UserConst.DEFAULT_FAILED_COUNT, userId);
    }

    @Override
    public void lockAccount(User user) {
        user.setUnlockFlag(false);
        user.setLockTime(new Date());

        userRepository.save(user);
    }

    @Override
    public boolean unlockWhenLockExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + UserConst.LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setUnlockFlag(true);
            user.setLockTime(null);
            user.setFailedCount(UserConst.DEFAULT_FAILED_COUNT);

            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
