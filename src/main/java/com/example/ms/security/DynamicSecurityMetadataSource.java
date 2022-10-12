package com.example.ms.security;

import com.example.ms.module.system.model.bo.Permission;
import com.example.ms.module.system.repository.PermissionRepository;
import com.sun.xml.internal.ws.util.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private static Map<String, ConfigAttribute> configAttributeMap = null;

    private PermissionRepository permissionRepository;

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @PostConstruct
    public void loadDataSource() {
        configAttributeMap = new ConcurrentHashMap<>();
        List<Permission> permissionList = permissionRepository.findAll();
        for (Permission permission : permissionList) {
//            configAttributeMap.put(permission.getPath(), new SecurityConfig(permission.getId() + ":" + permission.getGroup()));
            configAttributeMap.put(permission.getPath(), new SecurityConfig(permission.getPath()));
        }
    }

    public void clearDataSource() {
        configAttributeMap.clear();
        configAttributeMap = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        if (configAttributeMap == null) this.loadDataSource();
        List<ConfigAttribute> configAttributes = new ArrayList<>();
        //获取当前访问的路径
        String url = ((FilterInvocation) o).getRequestUrl();
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException var3) {
            throw new UtilException(var3);
        }
        String path = uri.getPath();

        PathMatcher pathMatcher = new AntPathMatcher();
        // 获取访问该路径所需资源
        for (String pattern : configAttributeMap.keySet()) {
            if (pathMatcher.match(pattern, path)) {
                configAttributes.add(configAttributeMap.get(pattern));
            }
        }

        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
