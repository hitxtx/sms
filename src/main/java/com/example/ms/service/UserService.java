package com.example.ms.service;

import com.example.ms.common.constant.UserConst;
import com.example.ms.model.User;
import com.example.ms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Transactional
@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void increaseFailedCount(User user) {
        int failCount = user.getFailedCount() + 1;
        userRepository.updateFailedCount(failCount, user.getId());
    }

    public void resetFailedCount(Long userId) {
        userRepository.updateFailedCount(UserConst.DEFAULT_FAILED_COUNT, userId);
    }

    public void lockAccount(User user) {
        user.setUnlockFlag(false);
        user.setLockTime(new Date());

        userRepository.save(user);
    }

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

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
