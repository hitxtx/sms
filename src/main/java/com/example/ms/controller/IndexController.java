package com.example.ms.controller;

import com.example.ms.model.Role;
import com.example.ms.model.User;
import com.example.ms.repository.RoleRepository;
import com.example.ms.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/")
@Controller
public class IndexController {

    final UserRepository userRepository;
    final RoleRepository roleRepository;

    public IndexController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        User user = userRepository.getOne(1L);
        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public User user() {
        return userRepository.getOne(1L);
    }

    @GetMapping("/role")
    @ResponseBody
    public Role role() {
        return roleRepository.getOne(1L);
    }
}
