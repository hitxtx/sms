package com.example.ms.controller.system;

import com.example.ms.common.component.PageResult;
import com.example.ms.common.component.Result;
import com.example.ms.model.bo.Menu;
import com.example.ms.service.MenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RequestMapping("/system/menu")
@Controller
public class MenuController {

    private final MenuService menuService;

    @GetMapping("")
    public String index() {
        return "/system/menu";
    }

    @ResponseBody
    @PostMapping("/search")
    public Result search(@RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                         @RequestParam(name = "keyword", required = false) String keyword) {
        Page<Menu> page = menuService.search(pageIndex - 1, pageSize, keyword.trim());

        return Result.SUCCESS(new PageResult<>(page.getTotalElements(), page.getContent()));
    }

    @ResponseBody
    @PostMapping("/create")
    public Result create(Menu menu) {
        try {
            menuService.create(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILED();
        }

        return Result.SUCCESS();
    }

    @ResponseBody
    @PostMapping("/update")
    public Result update(Menu menu) {
        try {
            menuService.update(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.FAILED();
        }

        return Result.SUCCESS();
    }

    @ResponseBody
    @PostMapping("/delete")
    public Result delete(Long id) {
        menuService.logicDelete(id);
        return Result.SUCCESS();
    }

}
