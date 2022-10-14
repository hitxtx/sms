package com.example.ms.module.system.controller;

import com.example.ms.common.annotation.MenuMarker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@MenuMarker(value = "仪表盘", icon = "fa-dashboard")
@Controller
public class IndexController {

    @MenuMarker("仪表盘")
    @GetMapping("/")
    public String index() {
        return "dashboard/v1";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
