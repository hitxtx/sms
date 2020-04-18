package com.example.ms.controller;

import com.example.ms.model.Menu;
import com.example.ms.repository.MenuRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
    public List<Menu> list() {

        return null;
    }

}