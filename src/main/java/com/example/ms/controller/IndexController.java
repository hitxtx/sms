package com.example.ms.controller;

import com.example.ms.model.User;
import com.example.ms.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/")
@Controller
public class IndexController {

    final UserRepository repo;

    public IndexController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String index(Model model) {
        User user = repo.getOne(1L);
        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping("/json")
    @ResponseBody
    public User json() {
        return repo.getOne(1L);
    }
}
