package com.example.ms.controller.system;

import com.example.ms.model.bo.Menu;
import com.example.ms.model.dto.SearchParam;
import com.example.ms.model.dto.SelectOption;
import com.example.ms.model.dto.PageResult;
import com.example.ms.model.dto.Result;
import com.example.ms.service.MenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/search")
    public Result search(SearchParam param) {
        Page<Menu> page = menuService.search(param);

        return Result.success(new PageResult<>(page.getTotalElements(), page.getContent()));
    }

    @PostMapping("/create")
    public Result create(Menu menu) {
        try {
            menuService.create(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result update(Menu menu) {
        try {
            menuService.update(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result delete(Long id) {
        menuService.logicDelete(id);
        return Result.success();
    }

    @PostMapping("/select")
    public Result select(String term) {
        List<SelectOption> optionList = menuService.select(term);
        return Result.success(optionList);
    }

}
