package com.example.ms.module.system.repository;

import com.example.ms.module.system.model.bo.Menu;
import com.example.ms.model.SelectOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Modifying
    @Query("update Menu AS p set p.deletedFlag =:deletedFlag where p.id = :id")
    void updateDeletedFlag(Boolean deletedFlag, Long id);

    List<Menu> findByDeletedFlag(Boolean deleteFlag);

    List<Menu> findMenuByDeletedFlagAndParentMenu(Boolean deletedFlag, Menu parentMenu);

    Page<Menu> findByDeletedFlagAndParentMenu(Boolean deletedFlag, Menu parentMenu, Pageable pageable);

    Page<Menu> findByDeletedFlagAndMenuNameLike(Boolean deletedFlag, String menuName, Pageable pageable);

    Menu findByPath(String path);

    @Query("SELECT m.id AS id, m.menuName as text from Menu as m where m.deletedFlag =:deletedFlag")
    List<SelectOption> listByDeletedFlag(Boolean deletedFlag);

    @Query("SELECT m.id AS id, m.menuName as text from Menu as m where m.deletedFlag =:deletedFlag and m.menuName like concat('%', :text, '%') ")
    List<SelectOption> listByDeletedFlagAndMenuNameLike(Boolean deletedFlag, String text);

}
