package com.example.ms.controller.system;

import com.example.ms.common.component.PageResult;
import com.example.ms.common.component.Result;
import com.example.ms.model.Role;
import com.example.ms.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RequestMapping("/system/role")
@Controller
public class RoleController {

    private final RoleService roleService;

    @GetMapping("")
    public String index() {
        return "/system/role";
    }

    @ResponseBody
    @PostMapping("/search")
    public Result search(@RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                         @RequestParam(name = "keyword", required = false) String keyword) {
        Page<Role> page = roleService.search(pageIndex - 1, pageSize, keyword);

        return Result.SUCCESS(new PageResult<>(page.getTotalElements(), page.getContent()));
    }

    @ResponseBody
    @PostMapping("/create")
    public Result create(Role role) {
        try {
            roleService.create(role);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILED();
        }

        return Result.SUCCESS();
    }

    @ResponseBody
    @PostMapping("/update")
    public Result update(Role role) {
        try {
            roleService.update(role);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILED();
        }

        return Result.SUCCESS();
    }

    @ResponseBody
    @PostMapping("/delete")
    public Result delete(Long id) {
        roleService.logicDelete(id);
        return Result.SUCCESS();
    }

}
