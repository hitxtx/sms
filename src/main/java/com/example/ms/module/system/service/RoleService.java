package com.example.ms.module.system.service;

import com.example.ms.model.SearchParam;
import com.example.ms.module.system.entity.Menu;
import com.example.ms.module.system.entity.Permission;
import com.example.ms.module.system.entity.Role;
import com.example.ms.module.system.model.TreeNode;
import com.example.ms.module.system.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final MenuService menuService;
    private final PermissionService permissionService;

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
            throw new Exception("该角色已存在");
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
            throw new Exception("更新角色异常");
        }
        Optional<Role> optional = roleRepository.findById(role.getId());
        optional.orElseThrow(() -> new Exception("找不到该角色"));
        if (!optional.get().getRoleName().equals(role.getRoleName())) {
            Role oldRole = roleRepository.findByRoleName(role.getRoleName());
            if (oldRole != null && !oldRole.getId().equals(role.getId())) {
                throw new Exception("该角色名称已存在");
            }
        }
        Role oldRole = optional.get();
        role.setDeletedFlag(oldRole.getDeletedFlag());
        role.setUpdatedTime(new Date());
        role.setMenus(oldRole.getMenus());
        role.setPermissions(oldRole.getPermissions());
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

    public List<TreeNode> roleMenuTreeNodes(Long id) {
        List<TreeNode> treeNodeList = new ArrayList<>();

        Role role = roleRepository.getById(id);
        List<Menu> roleMenuList = role.getMenus();
        List<Long> roleMenuIdList = roleMenuList.stream().map(Menu::getId).collect(Collectors.toList());
        List<Menu> menuList = menuService.list(0L);
        if (menuList.isEmpty()) {
            return treeNodeList;
        }
        for (Menu menu : menuList) {
            TreeNode treeNode = new TreeNode(menu.getId(), menu.getMenuName());
            boolean setFlag = roleMenuIdList.contains(menu.getId());
            treeNode.setState(setFlag, menu.getDeletedFlag(), !menu.getSubmenus().isEmpty(), setFlag);
            if (!menu.getSubmenus().isEmpty()) {
                treeNode.setNodes(new ArrayList<>());
                for (Menu submenu : menu.getSubmenus()) {
                    TreeNode subTreeNode = new TreeNode(submenu.getId(), submenu.getMenuName());
                    boolean subSetFlag = roleMenuIdList.contains(submenu.getId());
                    subTreeNode.setState(subSetFlag, submenu.getDeletedFlag(), !submenu.getSubmenus().isEmpty(), subSetFlag);

                    treeNode.getNodes().add(subTreeNode);
                }
            }

            treeNodeList.add(treeNode);
        }

        return treeNodeList;
    }

    @Transactional
    public void assignRoleMenu(Long id, List<Long> menuIds) {
        Role role = roleRepository.getById(id);
        role.setMenus(new ArrayList<>());
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                role.getMenus().add(menuService.getById(menuId));
            }
        }
        roleRepository.save(role);
    }

    public List<TreeNode> rolePermissionTreeNodes(Long id) {
        List<TreeNode> treeNodeList = new ArrayList<>();

        Role role = roleRepository.getById(id);
        List<Permission> rolePermissionList = role.getPermissions();
        List<Long> rolePermissionIdList = rolePermissionList.stream().map(Permission::getId).collect(Collectors.toList());
        List<Permission> permissionList = permissionService.listAll();
        permissionList.sort(Comparator.comparing(Permission::getModule).thenComparing(Permission::getPath));
        if (permissionList.isEmpty()) {
            return treeNodeList;
        }
        for (Permission permission : permissionList) {
            TreeNode treeNode = new TreeNode(permission.getId(), permission.getPath());
            boolean setFlag = rolePermissionIdList.contains(permission.getId());
            treeNode.setState(setFlag, permission.getDeletedFlag(), setFlag);

            treeNodeList.add(treeNode);
        }

        return treeNodeList;
    }

    @Transactional
    public void assignRolePermission(Long id, List<Long> permissionIds) {
        Role role = roleRepository.getById(id);
        role.setPermissions(new ArrayList<>());
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permissionId : permissionIds) {
                role.getPermissions().add(permissionService.getById(permissionId));
            }
        }
        roleRepository.save(role);
    }

}
