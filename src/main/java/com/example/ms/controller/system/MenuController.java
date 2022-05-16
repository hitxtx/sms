package com.example.ms.controller.system;

import com.example.ms.model.Menu;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Slf4j
@Setter
@Controller
@RequestMapping("/system/menu")
public class MenuController {



    @GetMapping("/")
    public String index() {
        return "system/menu";
    }

    @GetMapping("/list")
    @ResponseBody
    public Object list(Integer pageIndex, Integer pageSize, String keyword) {
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
            menu.setDeletedFlag(i % 2 == 0);
            menu.setCreatedTime(new Date());
            if (keyword != null && keyword.length() > 0) {
                menu.setMenuName(keyword + i);
            }

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
