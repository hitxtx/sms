package com.example.ms.controller.system;

import com.example.ms.model.bo.Role;
import com.example.ms.model.dto.SearchParam;
import com.example.ms.model.dto.PageResult;
import com.example.ms.model.dto.Result;
import com.example.ms.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            e.printStackTrace();
            return Result.failure();
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result update(Role role) {
        try {
            roleService.update(role);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result delete(Long id) {
        roleService.logicDelete(id);
        return Result.success();
    }

}
