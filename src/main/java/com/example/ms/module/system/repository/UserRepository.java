package com.example.ms.module.system.repository;

import com.example.ms.module.system.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.failedCount = ?1 WHERE u.id = ?2")
    int updateFailedCount(Integer failedCount, Long userId);

    @Modifying
    @Query("update User AS p set p.deletedFlag =:deletedFlag where p.id = :id")
    void updateDeletedFlag(Boolean deletedFlag, Long id);

    Page<User> findByDeletedFlag(Boolean deletedFlag, Pageable pageable);

    Page<User> findByDeletedFlagAndUsernameLike(Boolean deletedFlag, String roleName, Pageable pageable);

    User getByUsername(String roleName);

}
