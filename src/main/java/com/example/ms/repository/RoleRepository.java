package com.example.ms.repository;

import com.example.ms.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Modifying
    @Query("update Role AS p set p.deletedFlag =:deletedFlag where p.id = :id")
    void updateDeletedFlag(Boolean deletedFlag, Long id);

    Page<Role> findFirst10ByDeletedFlag(Boolean deletedFlag, Pageable pageable);

    Page<Role> findFirst10ByDeletedFlagAndRoleNameLike(Boolean deletedFlag, String roleName, Pageable pageable);

    Role findByRoleName(String roleName);

}
