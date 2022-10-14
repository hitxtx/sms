package com.example.ms.module.member.controller;

import com.example.ms.common.annotation.MenuMarker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@MenuMarker(value = "会员管理", icon = "fa-user")
@Slf4j
@AllArgsConstructor
@RequestMapping("/member")
@Controller
public class MemberController {

    @MenuMarker("会员等级")
    @GetMapping("/level")
    public String level() {
        return "/member/level";
    }

    @MenuMarker("会员账号")
    @GetMapping("/account")
    public String account() {
        return "/member/account";
    }

//    @MenuMarker("支付记录")
//    @GetMapping("/payment")
//    public String payment() {
//        return "/member/payment";
//    }
//
//    @MenuMarker("消费记录")
//    @GetMapping("/expense")
//    public String expense() {
//        return "/member/expense";
//    }

}
