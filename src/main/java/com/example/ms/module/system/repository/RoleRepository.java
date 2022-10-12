package com.example.ms.module.system.repository;

import com.example.ms.module.system.model.bo.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Modifying
    @Query("update Role AS p set p.deletedFlag =:deletedFlag where p.id = :id")
    void updateDeletedFlag(Boolean deletedFlag, Long id);

    List<Role> findByDeletedFlag(Boolean deleteFlag);

    Page<Role> findByDeletedFlag(Boolean deletedFlag, Pageable pageable);

    Page<Role> findByDeletedFlagAndRoleNameLike(Boolean deletedFlag, String roleName, Pageable pageable);

    Role findByRoleName(String roleName);

}
