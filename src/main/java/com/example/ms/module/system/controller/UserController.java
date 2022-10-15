package com.example.ms.module.system.controller;

import com.example.ms.model.PageResult;
import com.example.ms.model.Result;
import com.example.ms.model.SearchParam;
import com.example.ms.module.system.entity.User;
import com.example.ms.module.system.model.TreeNode;
import com.example.ms.module.system.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/system/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/search")
    public Result search(SearchParam param) {
        Page<User> page = userService.search(param);

        return Result.success(new PageResult<>(page.getTotalElements(), page.getContent()));
    }

    @PostMapping("/create")
    public Result create(User user) {
        try {
            userService.create(user);
        } catch (Exception e) {
            log.error("新增用户：", e);
            return Result.failure(e.getMessage());
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result update(User user) {
        try {
            userService.update(user);
        } catch (Exception e) {
            log.error("更新用户：", e);
            return Result.failure(e.getMessage());
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result delete(Long id) {
        userService.logicDelete(id);
        return Result.success();
    }

    @GetMapping("/role/treeview")
    public Result userRoleTreeview(Long id) {
        List<TreeNode> treeNodeList = userService.userRoleTreeNodes(id);
        return Result.success(treeNodeList);
    }

    @PostMapping("/role/assign")
    public Result assignUserRole(Long id, @RequestParam List<Long> roleIds) {
        userService.assignUserRole(id, roleIds);
        return Result.success();
    }

}
