package com.example.ms.module.member.controller;

import com.example.ms.model.PageResult;
import com.example.ms.model.Result;
import com.example.ms.model.SearchParam;
import com.example.ms.module.member.entity.MemberLevel;
import com.example.ms.module.member.service.MemberLevelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/member/level")
public class MemberLevelController {

    private final MemberLevelService levelService;

    @PostMapping("/search")
    public Result search(SearchParam param) {
        Page<MemberLevel> page = levelService.search(param);

        return Result.success(new PageResult<>(page.getTotalElements(), page.getContent()));
    }

    @PostMapping("/create")
    public Result create(MemberLevel level) {
        try {
            levelService.create(level);
        } catch (Exception e) {
            log.error("新增会员等级：", e);
            return Result.failure();
        }

        return Result.success();
    }

    @PostMapping("/update")
    public Result update(MemberLevel level) {
        try {
            levelService.update(level);
        } catch (Exception e) {
            log.error("更新会员等级：", e);
            return Result.failure();
        }

        return Result.success();
    }

    @PostMapping("/delete")
    public Result delete(Long id) {
        levelService.logicDelete(id);
        return Result.success();
    }

}
