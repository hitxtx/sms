package com.example.ms.module.system.controller;

import com.example.ms.common.annotation.MenuMarker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@MenuMarker(value = "系统管理", icon = "fa-cog")
@Slf4j
@AllArgsConstructor
@RequestMapping("/system")
@Controller
public class SystemController {

    @MenuMarker("用户管理")
    @GetMapping("/user")
    public String user() {
        return "/system/user";
    }

    @MenuMarker("角色管理")
    @GetMapping("/role")
    public String role() {
        return "/system/role";
    }

    @MenuMarker("菜单管理")
    @GetMapping("/menu")
    public String menu() {
        return "/system/menu";
    }

    @MenuMarker("权限管理")
    @GetMapping("/permission")
    public String permission() {
        return "/system/permission";
    }

}
