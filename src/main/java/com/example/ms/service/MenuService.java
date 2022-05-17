package com.example.ms.service;

import com.example.ms.model.Menu;
import com.example.ms.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuService {

    private MenuRepository menuRepository;

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Page<Menu> list(Integer pageIndex, Integer pageSize, String keywords) {
        PageRequest request = PageRequest.of(pageIndex, pageSize, Sort.Direction.ASC, "id");
        return menuRepository.findAll(request);
    }
}
