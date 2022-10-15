package com.example.ms.module.system.service;

import com.example.ms.model.SearchParam;
import com.example.ms.module.system.entity.Permission;
import com.example.ms.module.system.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Page<Permission> search(SearchParam param) {
        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize(), Sort.Direction.ASC, "id");
        String keyword = param.getKeyword();
        if (keyword == null || "".equals(keyword)) {
            return permissionRepository.findByDeletedFlag(false, pageable);
        }
        return permissionRepository.findByDeletedFlagAndTagLike(false, "%" + keyword + "%", pageable);
    }

    public Permission create(Permission permission) throws Exception {
        Permission oldPermission = permissionRepository.findByPath(permission.getPath());
        if (oldPermission != null && !oldPermission.getDeletedFlag()) {
            throw new Exception("该权限已存在");
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
            throw new Exception("更新权限异常");
        }
        Optional<Permission> optional = permissionRepository.findById(permission.getId());
        optional.orElseThrow(() -> new Exception("找不到该权限"));
        if (!optional.get().getPath().equals(permission.getPath())) {
            Permission oldPermission = permissionRepository.findByPath(permission.getPath());
            if (oldPermission != null && !oldPermission.getId().equals(permission.getId())) {
                throw new Exception("该权限已存在");
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

    public Permission getById(Long id) {
        return permissionRepository.getById(id);
    }

    public List<Permission> listAll() {
        return permissionRepository.findAll();
    }

    public List<Permission> listAllEnabled() {
        return permissionRepository.findByDeletedFlag(false);
    }

}
