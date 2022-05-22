package com.example.ms.repository;

import com.example.ms.model.bo.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Modifying
    @Query("update Menu AS p set p.deletedFlag =:deletedFlag where p.id = :id")
    void updateDeletedFlag(Boolean deletedFlag, Long id);

    Page<Menu> findFirst10ByDeletedFlag(Boolean deletedFlag, Pageable pageable);

    Page<Menu> findFirst10ByDeletedFlagAndMenuNameLike(Boolean deletedFlag, String menuName, Pageable pageable);

    Menu findByPath(String path);

}
