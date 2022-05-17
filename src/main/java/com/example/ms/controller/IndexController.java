package com.example.ms.controller;

import com.example.ms.common.annotation.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Tag("仪表盘")
    @GetMapping("/")
    public String index() {
        return "dashboard/v1";
    }

    @Tag("登录")
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
