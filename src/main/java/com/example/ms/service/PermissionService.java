package com.example.ms.service;

import cn.hutool.core.util.StrUtil;
import com.example.ms.model.Permission;
import com.example.ms.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@AllArgsConstructor
@Transactional
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Page<Permission> search(Integer pageIndex, Integer pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.Direction.ASC, "id");
        if (StrUtil.isBlank(keyword)) {
            return permissionRepository.findAll(pageable);
        }
        return permissionRepository.findFirst10ByTagLike(keyword, pageable);
    }

    public Permission create(Permission permission) {

        permission.setCreatedTime(new Date());
        return permissionRepository.saveAndFlush(permission);
    }

}
