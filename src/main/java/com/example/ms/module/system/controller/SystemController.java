package com.example.ms.module.system.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@AllArgsConstructor
@RequestMapping("/system")
@Controller
public class SystemController {

    @GetMapping("/user")
    public String userPage() {
        return "/system/user";
    }

    @GetMapping("/role")
    public String rolePage() {
        return "/system/role";
    }

    @GetMapping("/menu")
    public String menuPage() {
        return "/system/menu";
    }

    @GetMapping("/permission")
    public String permissionPage() {
        return "/system/permission";
    }

}
