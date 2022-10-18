package com.example.ms.module.system.controller;

import com.example.ms.model.PageResult;
import com.example.ms.model.Result;
import com.example.ms.model.SearchParam;
import com.example.ms.module.system.entity.Role;
import com.example.ms.module.system.model.TreeNode;
import com.example.ms.module.system.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/system/role")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/search")
    public Result search(SearchParam param) {
        Page<Role> page = roleService.search(param);

        return Result.success(new PageResult<>(page.getTotalElements(), page.getContent()));
    }

    @PostMapping("/create")
    public Result create(Role role) {
        try {
            roleService.create(role);
        } catch (Exception e) {
            log.error("新增角色：", e);
            return Result.failure();
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result update(Role role) {
        try {
            roleService.update(role);
        } catch (Exception e) {
            log.error("更新角色：", e);
            return Result.failure();
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result delete(Long id) {
        roleService.logicDelete(id);
        return Result.success();
    }

    @GetMapping("/menu/treeview")
    public Result menuTreeview(Long id) {
        List<TreeNode> treeNodeList = roleService.roleMenuTreeNodes(id);
        return Result.success(treeNodeList);
    }

    @PostMapping("/assign/menu")
    public Result assignMenu(Long id, @RequestParam List<Long> menuIds) {
        roleService.assignRoleMenu(id, menuIds);
        return Result.success();
    }

    @GetMapping("/permission/treeview")
    public Result permissionTreeview(Long id) {
        List<TreeNode> treeNodeList = roleService.rolePermissionTreeNodes(id);
        return Result.success(treeNodeList);
    }

    @PostMapping("/assign/permission")
    public Result assignPermission(Long id, @RequestParam List<Long> permissionIds) {
        roleService.assignRolePermission(id, permissionIds);
        return Result.success();
    }

}
