package com.example.ms.repository;

import com.example.ms.model.bo.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Modifying
    @Query("update Permission AS p set p.deletedFlag =:deletedFlag where p.id = :id")
    void updateDeletedFlag(Boolean deletedFlag, Long id);

    Page<Permission> findFirst10ByDeletedFlag(Boolean deletedFlag, Pageable pageable);

    Page<Permission> findFirst10ByDeletedFlagAndTagLike(Boolean deletedFlag, String tag, Pageable pageable);

    Permission findByPath(String path);

}
