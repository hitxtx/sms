package com.example.ms.controller.system;

import com.example.ms.model.vo.PageResult;
import com.example.ms.model.vo.Result;
import com.example.ms.model.bo.User;
import com.example.ms.model.dto.SearchParam;
import com.example.ms.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RequestMapping("/system/user")
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public String index() {
        return "/system/user";
    }

    @ResponseBody
    @PostMapping("/search")
    public Result search(SearchParam param) {
        Page<User> page = userService.search(param);

        return Result.SUCCESS(new PageResult<>(page.getTotalElements(), page.getContent()));
    }

    @ResponseBody
    @PostMapping("/create")
    public Result create(User user) {
        try {
            userService.create(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILED();
        }

        return Result.SUCCESS();
    }

    @ResponseBody
    @PostMapping("/update")
    public Result update(User user) {
        try {
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILED();
        }

        return Result.SUCCESS();
    }

    @ResponseBody
    @PostMapping("/delete")
    public Result delete(Long id) {
        userService.logicDelete(id);
        return Result.SUCCESS();
    }

}
