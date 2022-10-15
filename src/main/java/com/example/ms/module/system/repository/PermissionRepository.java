package com.example.ms.module.system.repository;

import com.example.ms.module.system.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Modifying
    @Query("update Permission AS p set p.deletedFlag =:deletedFlag where p.id = :id")
    void updateDeletedFlag(Boolean deletedFlag, Long id);

    List<Permission> findByDeletedFlag(Boolean deletedFlag);

    Page<Permission> findByDeletedFlag(Boolean deletedFlag, Pageable pageable);

    Page<Permission> findByDeletedFlagAndMethodLike(Boolean deletedFlag, String method, Pageable pageable);

    Permission findByPath(String path);

}
