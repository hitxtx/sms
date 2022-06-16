package com.example.ms.controller.system;

import com.example.ms.common.component.PageResult;
import com.example.ms.common.component.Result;
import com.example.ms.model.bo.Permission;
import com.example.ms.model.dto.SearchParam;
import com.example.ms.service.PermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RequestMapping("/system/permission")
@Controller
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("")
    public String index() {
        return "/system/permission";
    }

    @ResponseBody
    @PostMapping("/search")
    public Result search(SearchParam param) {
        Page<Permission> page = permissionService.search(param);

        return Result.SUCCESS(new PageResult<>(page.getTotalElements(), page.getContent()));
    }

    @ResponseBody
    @PostMapping("/create")
    public Result create(Permission permission) {
        try {
            permissionService.create(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILED();
        }

        return Result.SUCCESS();
    }

    @ResponseBody
    @PostMapping("/update")
    public Result update(Permission permission) {
        try {
            permissionService.update(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILED();
        }

        return Result.SUCCESS();
    }

    @ResponseBody
    @PostMapping("/delete")
    public Result delete(Long id) {
        permissionService.logicDelete(id);
        return Result.SUCCESS();
    }

}
