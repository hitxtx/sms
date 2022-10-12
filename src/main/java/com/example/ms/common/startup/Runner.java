package com.example.ms.common.startup;

import com.example.ms.common.annotation.Tag;
import com.example.ms.module.system.model.bo.Permission;
import com.example.ms.module.system.model.bo.Role;
import com.example.ms.module.system.repository.PermissionRepository;
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

    private PermissionRepository permissionRepository;

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    private static final String BASE_PACKAGE = "com.example.ms.controller";
    private static final String RESOURCE_PATTERN = "/**/*Controller.class";

    @Override
    public void run(ApplicationArguments args) throws Exception {

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

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
                RequestMapping classAnnotation = clazz.getAnnotation(RequestMapping.class);
                String classPath = classAnnotation != null ? classAnnotation.value()[0] : "";

                for (Method method : clazz.getDeclaredMethods()) {
                    // 请求路径
                    String path;
                    Tag tag = method.getAnnotation(Tag.class);
                    String tagName; // 路径备注
                    if (tag == null) {
                        tagName = method.getName();
                    } else {
                        tagName = tag.value();
                    }

                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    PutMapping putMapping = method.getAnnotation(PutMapping.class);
                    DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                    List<String> methodPathList = new ArrayList<>();
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

                    methodPathList.forEach(p -> {
                        Permission permission = new Permission();
                        permission.setModule(moduleName);
                        permission.setTag(tagName);
                        permission.setPath(classPath + p);
                        permission.setDeletedFlag(false);
                        permission.setCreatedTime(new Date());

                        permissionMap.put(permission.getPath(), permission);
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        updatePermissions(permissionMap);

    }

    public void updatePermissions(Map<String, Permission> permissionMap) {
        List<Permission> permissionList = permissionRepository.findAll();
        for (Permission oldPermission : permissionList) {
            Permission newPermission = permissionMap.get(oldPermission.getPath());
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
                    oldPermission.setTag(newPermission.getTag());
                    oldPermission.setDeletedFlag(false);
                    oldPermission.setUpdatedTime(new Date());
                    permissionRepository.saveAndFlush(oldPermission);
                }

                permissionMap.remove(oldPermission.getPath()); // 移除更新记录
            }
        }
        // 新增
        permissionRepository.saveAll(permissionMap.values());
    }

}
