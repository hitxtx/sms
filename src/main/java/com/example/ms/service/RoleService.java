package com.example.ms.service;

import com.example.ms.model.bo.Role;
import com.example.ms.model.dto.SearchParam;
import com.example.ms.repository.RoleRepository;
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
public class RoleService {

    private final RoleRepository roleRepository;

    public Page<Role> search(SearchParam param) {
        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize(), Sort.Direction.ASC, "id");
        String keyword = param.getKeyword();
        if (keyword == null || "".equals(keyword)) {
            return roleRepository.findByDeletedFlag(false, pageable);
        }
        return roleRepository.findByDeletedFlagAndRoleNameLike(false, "%" + keyword + "%", pageable);
    }

    public Role create(Role role) throws Exception {
        Role oldRole = roleRepository.findByRoleName(role.getRoleName());
        if (oldRole != null && !oldRole.getDeletedFlag()) {
            throw new Exception("记录已存在");
        }
        if (oldRole != null && oldRole.getDeletedFlag()) {
            role.setId(oldRole.getId());
        }
        role.setDeletedFlag(false);
        role.setCreatedTime(new Date());
        return roleRepository.saveAndFlush(role);
    }

    public Role update(Role role) throws Exception {
        if (role.getId() == null || role.getId() <= 0) {
            throw new Exception("更新信息异常");
        }
        Optional<Role> optional = roleRepository.findById(role.getId());
        optional.orElseThrow(() -> new Exception("找不到该记录"));
        if (!optional.get().getRoleName().equals(role.getRoleName())) {
            Role oldRole = roleRepository.findByRoleName(role.getRoleName());
            if (oldRole != null && !oldRole.getId().equals(role.getId())) {
                throw new Exception("记录已存在");
            }
        }
        Role oldRole = optional.get();
        role.setDeletedFlag(oldRole.getDeletedFlag());
        role.setCreatedTime(oldRole.getCreatedTime());
        role.setUpdatedTime(new Date());
        return roleRepository.saveAndFlush(role);
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    public void logicDelete(Long id) {
        boolean exists = roleRepository.existsById(id);
        if (exists) {
            roleRepository.updateDeletedFlag(true, id);
        }
    }

    public Role getById(Long id) {
        return roleRepository.getById(id);
    }

    public List<Role> listAll() {
        return roleRepository.findAll();
    }

    public List<Role> listAllEnabled() {
        return roleRepository.findByDeletedFlag(false);
    }

}
