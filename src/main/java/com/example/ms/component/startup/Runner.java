package com.example.ms.component.startup;

import com.example.ms.repository.PermissionRepository;
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
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class Runner implements ApplicationRunner {

    private PermissionRepository permissionRepository;

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    private static final String BASE_PACKAGE = "com.example.ms.controller";
    private static final String RESOURCE_PATTERN = "/**/*.class";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE) + RESOURCE_PATTERN;
            Resource[] resources = resolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resolver);
            for (Resource resource : resources) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                RequestMapping classAnnotation = clazz.getAnnotation(RequestMapping.class);
                if (classAnnotation != null) {
                    //将注解中的类型值作为key，对应的类作为 value
                    System.err.println(Arrays.toString(classAnnotation.value()));
                    for (Method method : clazz.getDeclaredMethods()) {
                        GetMapping methodAnnotation = method.getAnnotation(GetMapping.class);
                        if (methodAnnotation != null) {
                            System.err.println(Arrays.toString(methodAnnotation.value()));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
