package com.example.ms.service;

import com.example.ms.common.constant.UserConst;
import com.example.ms.model.bo.User;
import com.example.ms.model.dto.SearchParam;
import com.example.ms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<User> search(SearchParam param) {
        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize(), Sort.Direction.ASC, "id");
        String keyword = param.getKeyword();
        if (keyword == null || "".equals(keyword)) {
            return userRepository.findFirst10ByDeletedFlag(false, pageable);
        }
        return userRepository.findFirst10ByDeletedFlagAndUsernameLike(false, "%" + keyword + "%", pageable);
    }

    public User create(User user) throws Exception {
        User oldUser = userRepository.getByUsername(user.getUsername());
        if (oldUser != null && !oldUser.getDeletedFlag()) {
            throw new Exception("记录已存在");
        }
        if (oldUser != null && oldUser.getDeletedFlag()) {
            user.setId(oldUser.getId());
        }
        user.setDeletedFlag(false);
        user.setCreatedTime(new Date());
        return userRepository.saveAndFlush(user);
    }

    public User update(User user) throws Exception {
        if (user.getId() == null || user.getId() <= 0) {
            throw new Exception("更新信息异常");
        }
        Optional<User> optional = userRepository.findById(user.getId());
        optional.orElseThrow(() -> new Exception("找不到该记录"));
        if (!optional.get().getUsername().equals(user.getUsername())) {
            User oldUser = userRepository.getByUsername(user.getUsername());
            if (oldUser != null && !oldUser.getId().equals(user.getId())) {
                throw new Exception("记录已存在");
            }
        }
        User oldUser = optional.get();
        user.setDeletedFlag(oldUser.getDeletedFlag());
        user.setCreatedTime(oldUser.getCreatedTime());
        user.setUpdatedTime(new Date());
        return userRepository.saveAndFlush(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void logicDelete(Long id) {
        boolean exists = userRepository.existsById(id);
        if (exists) {
            userRepository.updateDeletedFlag(true, id);
        }
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
