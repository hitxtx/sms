package com.example.ms.security;

import com.example.ms.model.Permission;
import com.example.ms.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private final Map<String, Collection<ConfigAttribute>> configAttributeMap = new HashMap<>();;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private PermissionRepository permissionRepository;

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        String httpMethod = fi.getRequest().getMethod();

        List<ConfigAttribute> attributes = new ArrayList<>();
        List<Permission> permissionList = permissionRepository.findAll();
        Map<String, String> urlRoleMap = permissionList
                .stream()
                .collect(Collectors.toMap(Permission::getPath, Permission::getPath));

        for (Map.Entry<String, String> entry : urlRoleMap.entrySet()) {
            if (antPathMatcher.match(entry.getKey(), url)) {
                return SecurityConfig.createList(entry.getValue());
            }
        }

        return attributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
