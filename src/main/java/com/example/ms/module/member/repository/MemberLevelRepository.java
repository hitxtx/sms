package com.example.ms.module.member.repository;

import com.example.ms.module.member.entity.MemberLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberLevelRepository extends JpaRepository<MemberLevel, Long> {

    @Modifying
    @Query("update MemberLevel AS p set p.deletedFlag =:deletedFlag where p.id = :id")
    void updateDeletedFlag(Boolean deletedFlag, Long id);

    List<MemberLevel> findByDeletedFlag(Boolean deleteFlag);

    Page<MemberLevel> findByDeletedFlag(Boolean deletedFlag, Pageable pageable);

    Page<MemberLevel> findByDeletedFlagAndLevelNameLike(Boolean deletedFlag, String roleName, Pageable pageable);

    MemberLevel findByLevelName(String roleName);

}
