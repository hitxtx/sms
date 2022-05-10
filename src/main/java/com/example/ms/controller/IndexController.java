package com.example.ms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/")
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "dashboard/v1";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/system/")
    public String system() {
        return "dashboard/v1";
    }

    @GetMapping("/hello/")
    public String hello() {
        return "dashboard/v1";
    }

}
