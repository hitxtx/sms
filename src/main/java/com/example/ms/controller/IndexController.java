package com.example.ms.controller;

import com.example.ms.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/")
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        List<String> prods = new ArrayList<>(15);
        for (int i = 0; i < 10; i++) {
            prods.add("hello " + i + " world");
        }
        model.addAttribute("prods", prods);

        User user = new User();
        user.setUsername("hello");
        user.setPassword("world");
        model.addAttribute("user", user);

        return "index";
    }
}
