package com.example.ms.controller;

import com.example.ms.model.Menu;
import com.example.ms.repository.MenuRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/menu")
public class MenuController {

    final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping("/")
    public String index() {
        return "menu";
    }

    @GetMapping("/list")
    @ResponseBody
    public Object list(Long pageIndex, Long pageSize) {
        List<Menu> menus = new ArrayList<>();
        for (long i = (pageIndex - 1L) * pageSize; i < pageIndex * pageSize; i++) {
            Menu menu = new Menu();
            menu.setId(i);
            menu.setParentMenu(new Menu());
            menu.setMenuName("菜单名称" + i);
            menu.setMenuCode("Menu-" + i);
            menu.setPath("#");
            menu.setIcon("fa fa-smile");
            menu.setSort(1L);
            menu.setDeletedFlag(false);
            menu.setCreatedTime(new Date());

            menus.add(menu);
        }
        Map<String, Object> json = new HashMap<>();
        json.put("data", menus);
        json.put("itemsCount", 20);
        return json;
    }

    @PostMapping("/save")
    @ResponseBody
    public Menu save(Menu menu) {

        return null;
    }

    @PostMapping("/delete")
    @ResponseBody
    public Long delete(Long id) {

        return 0L;
    }

}