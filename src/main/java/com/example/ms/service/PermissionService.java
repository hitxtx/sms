package com.example.ms.service;

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
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Page<Permission> search(Integer pageIndex, Integer pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.Direction.ASC, "id");
        if (keyword == null || "".equals(keyword.trim())) {
            return permissionRepository.findFirst10ByDeletedFlag(false, pageable);
        }
        return permissionRepository.findFirst10ByDeletedFlagAndTagLike(false, keyword.trim(), pageable);
    }

    public Permission create(Permission permission) throws Exception {
        Permission oldPermission = permissionRepository.findByPath(permission.getPath());
        if (oldPermission != null && !oldPermission.getDeletedFlag()) {
            throw new Exception("记录已存在");
        }
        if (oldPermission != null && oldPermission.getDeletedFlag()) {
            permission.setId(oldPermission.getId());
        }
        permission.setDeletedFlag(false);
        permission.setCreatedTime(new Date());
        return permissionRepository.saveAndFlush(permission);
    }

    public Permission update(Permission permission) throws Exception {
        if (permission.getId() == null || permission.getId() <= 0) {
            throw new Exception("更新信息异常");
        }
        Optional<Permission> optional = permissionRepository.findById(permission.getId());
        optional.orElseThrow(() -> new Exception("找不到该记录"));
        if (!optional.get().getPath().equals(permission.getPath())) {
            Permission oldPermission = permissionRepository.findByPath(permission.getPath());
            if (oldPermission != null && !oldPermission.getId().equals(permission.getId())) {
                throw new Exception("记录已存在");
            }
        }
        Permission oldPermission = optional.get();
        permission.setDeletedFlag(oldPermission.getDeletedFlag());
        permission.setCreatedTime(oldPermission.getCreatedTime());
        permission.setUpdatedTime(new Date());
        return permissionRepository.saveAndFlush(permission);
    }

    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

    public void logicDelete(Long id) {
        boolean exists = permissionRepository.existsById(id);
        if (exists) {
            permissionRepository.updateDeletedFlag(true, id);
        }
    }
}
