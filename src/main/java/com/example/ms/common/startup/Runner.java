package com.example.ms.common.startup;

import com.example.ms.common.annotation.MenuMarker;
import com.example.ms.module.system.entity.Menu;
import com.example.ms.module.system.entity.Permission;
import com.example.ms.module.system.entity.Role;
import com.example.ms.module.system.repository.MenuRepository;
import com.example.ms.module.system.repository.PermissionRepository;
import com.example.ms.module.system.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

@Transactional(rollbackFor = Exception.class)
@Slf4j
@Component
public class Runner implements ApplicationRunner {

    private static final String BASE_PACKAGE = "com.example.ms.module";
    private static final String RESOURCE_PATTERN = "/**/*Controller.class";

    private RoleRepository roleRepository;
    private MenuRepository menuRepository;
    private PermissionRepository permissionRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Map<String, Menu> menuMap = new HashMap<>();
        Map<String, Permission> permissionMap = new HashMap<>();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE) + RESOURCE_PATTERN;
            Resource[] resources = resolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resolver);
            for (Resource resource : resources) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                String className = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);

                // 获取ClassShortName
                String simpleName = clazz.getSimpleName();
                String moduleName = simpleName.replace("Controller", "");

                // 获取Class路径
                RequestMapping classRequestMapping = clazz.getAnnotation(RequestMapping.class);
                String classPath = classRequestMapping != null ? classRequestMapping.value()[0] : "";
                MenuMarker classMenuMarker = clazz.getAnnotation(MenuMarker.class);
                if (classMenuMarker != null) {
                    Menu menu = new Menu();
                    menu.setMenuName(classMenuMarker.value());
                    menu.setMenuCode(moduleName);
                    menu.setPath("#");
                    menu.setIcon(classMenuMarker.icon());
                    menu.setSort(1L);
                    menu.setDeletedFlag(false);
                    menu.setCreatedTime(new Date());

                    menuMap.put(menu.getMenuCode(), menu);
                }

                for (Method method : clazz.getDeclaredMethods()) {
                    String methodName = method.getName();

                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    PutMapping putMapping = method.getAnnotation(PutMapping.class);
                    DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                    List<String> methodPathList;
                    if (postMapping != null) {
                        methodPathList = Arrays.asList(postMapping.value());
//                        path = classPath + postMapping.value()[0];
                    } else if (getMapping != null) {
                        methodPathList = Arrays.asList(getMapping.value());
//                        path = classPath + getMapping.value()[0];
                    } else if (putMapping != null) {
                        methodPathList = Arrays.asList(putMapping.value());
//                        path = classPath + putMapping.value()[0];
                    } else if (deleteMapping != null) {
                        methodPathList = Arrays.asList(deleteMapping.value());
//                        path = classPath + deleteMapping.value()[0];
                    } else if (mapping != null) {
                        methodPathList = Arrays.asList(mapping.value());
//                        path = classPath + mapping.value()[0];
                    } else {
                        continue;
                    }

                    MenuMarker menuMarker = method.getAnnotation(MenuMarker.class);
                    if (menuMarker != null) { // 菜单方法
                        for (String url : methodPathList) {
                            Menu menu = new Menu();
                            menu.setParentMenu(menuMap.get(moduleName));
                            menu.setMenuName(menuMarker.value());
                            menu.setMenuCode(moduleName + "#" + methodName);
                            menu.setPath(classPath + url);
                            menu.setIcon(menuMarker.icon());
                            menu.setSort(1L);
                            menu.setDeletedFlag(false);
                            menu.setCreatedTime(new Date());

                            menuMap.get(moduleName).getSubmenus().add(menu);
                        }
                    }


                    methodPathList.forEach(pathKey -> {
                        Permission permission = new Permission();
                        permission.setModule(moduleName);
                        permission.setMethod(methodName);
                        permission.setPath(classPath + pathKey + (menuMarker == null ? "/**" : ""));
                        permission.setDeletedFlag(false);
                        permission.setCreatedTime(new Date());

                        permissionMap.put(permission.getMethod(), permission);
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateMenus(menuMap);
        updatePermissions(permissionMap);

//        updateSuperRole();
    }

    private void updateSuperRole() {
        Role role = roleRepository.findByRoleCode("SUPER");

        List<Menu> menuList = menuRepository.findAll();
        List<Permission> permissionList = permissionRepository.findAll();
        role.setMenus(menuList);
        role.setPermissions(permissionList);
        roleRepository.saveAndFlush(role);
    }

    public void updateMenus(Map<String, Menu> menuMap) {
        List<String> menuCodeList = new ArrayList<>(menuMap.keySet());

        for (String menuCode : menuMap.keySet()) {
            Menu newMenu = menuMap.get(menuCode);
            Menu oldMenu = menuRepository.findByMenuCode(menuCode);
            if (oldMenu != null) {
                newMenu.setId(oldMenu.getId());
                newMenu.setSort(oldMenu.getSort());
                newMenu.setCreatedTime(oldMenu.getCreatedTime());
                newMenu.setUpdatedTime(new Date());

            }
            menuRepository.saveAndFlush(newMenu);

            List<Menu> submenus = newMenu.getSubmenus();
            if (submenus == null || submenus.isEmpty()) {
                continue;
            }
            for (Menu newSubmenu : submenus) {
                menuCodeList.add(newSubmenu.getMenuCode());

                Menu oldSubmenu = menuRepository.findByMenuCode(newSubmenu.getMenuCode());
                if (oldSubmenu == null) {
                    continue;
                }
                newSubmenu.setId(oldSubmenu.getId());
                newSubmenu.setParentMenu(newMenu);
                newSubmenu.setSort(oldSubmenu.getSort());
                newSubmenu.setCreatedTime(oldSubmenu.getCreatedTime());
                newSubmenu.setUpdatedTime(new Date());

            }

            menuRepository.saveAllAndFlush(submenus);
        }

        menuRepository.deleteByMenuCodeIsNotIn(menuCodeList);
    }

    public void updatePermissions(Map<String, Permission> permissionMap) {
        List<Permission> permissionList = permissionRepository.findAll();
        for (Permission oldPermission : permissionList) {
            Permission newPermission = permissionMap.get(oldPermission.getMethod());
            if (newPermission == null) {
                // 解除角色关联
                Set<Role> roles = oldPermission.getRoles();
                for (Role role : roles) {
//                    Hibernate.initialize(oldPermission.getRoles());
                    role.getPermissions().remove(oldPermission);
                }
                // 删除
                permissionRepository.updateDeletedFlag(true, oldPermission.getId());
            } else {
                // 更新
                if (!oldPermission.equals(newPermission)) {
                    oldPermission.setModule(newPermission.getModule());
                    oldPermission.setMethod(newPermission.getMethod());
                    oldPermission.setPath(newPermission.getPath());
                    oldPermission.setDeletedFlag(false);
                    oldPermission.setUpdatedTime(new Date());
                    permissionRepository.saveAndFlush(oldPermission);
                }

                permissionMap.remove(oldPermission.getMethod()); // 移除更新记录
            }
        }
        // 新增
        permissionRepository.saveAll(permissionMap.values());
    }

}
