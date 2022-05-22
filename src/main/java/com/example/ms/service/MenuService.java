package com.example.ms.service;

import com.example.ms.model.Menu;
import com.example.ms.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public Page<Menu> search(Integer pageIndex, Integer pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.Direction.ASC, "id");
        if (keyword == null || "".equals(keyword)) {
            return menuRepository.findFirst10ByDeletedFlag(false, pageable);
        }
        return menuRepository.findFirst10ByDeletedFlagAndMenuNameLike(false, "%" + keyword + "%", pageable);
    }

    public Menu create(Menu menu) throws Exception {
        Menu oldMenu = menuRepository.findByPath(menu.getPath());
        if (oldMenu != null && !oldMenu.getDeletedFlag()) {
            throw new Exception("记录已存在");
        }
        if (oldMenu != null && oldMenu.getDeletedFlag()) {
            menu.setId(oldMenu.getId());
        }
        menu.setDeletedFlag(false);
        menu.setCreatedTime(new Date());
        return menuRepository.saveAndFlush(menu);
    }

    public Menu update(Menu menu) throws Exception {
        if (menu.getId() == null || menu.getId() <= 0) {
            throw new Exception("更新信息异常");
        }
        Optional<Menu> optional = menuRepository.findById(menu.getId());
        optional.orElseThrow(() -> new Exception("找不到该记录"));
        if (!optional.get().getPath().equals(menu.getPath())) {
            Menu oldMenu = menuRepository.findByPath(menu.getPath());
            if (oldMenu != null && !oldMenu.getId().equals(menu.getId())) {
                throw new Exception("记录已存在");
            }
        }
        Menu oldMenu = optional.get();
        menu.setDeletedFlag(oldMenu.getDeletedFlag());
        menu.setCreatedTime(oldMenu.getCreatedTime());
        menu.setUpdatedTime(new Date());
        return menuRepository.saveAndFlush(menu);
    }

    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    public void logicDelete(Long id) {
        boolean exists = menuRepository.existsById(id);
        if (exists) {
            menuRepository.updateDeletedFlag(true, id);
        }
    }
}
