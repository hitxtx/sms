package com.example.ms.service.impl;

import com.example.ms.repository.PermissionRepository;
import com.example.ms.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;


}
