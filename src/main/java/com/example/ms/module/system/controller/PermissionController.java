package com.example.ms.module.system.controller;

import com.example.ms.module.system.model.bo.Permission;
import com.example.ms.model.PageResult;
import com.example.ms.model.Result;
import com.example.ms.model.SearchParam;
import com.example.ms.module.system.service.PermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/system/permission")
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/search")
    public Result search(SearchParam param) {
        Page<Permission> page = permissionService.search(param);

        return Result.success(new PageResult<>(page.getTotalElements(), page.getContent()));
    }

    @PostMapping("/create")
    public Result create(Permission permission) {
        try {
            permissionService.create(permission);
        } catch (Exception e) {
            log.error("新增权限：", e);
            return Result.failure(e.getMessage());
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result update(Permission permission) {
        try {
            permissionService.update(permission);
        } catch (Exception e) {
            log.error("更新权限：", e);
            return Result.failure(e.getMessage());
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result delete(Long id) {
        permissionService.logicDelete(id);
        return Result.success();
    }

}
