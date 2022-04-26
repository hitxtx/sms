package com.example.ms.service;

import com.example.ms.model.Permission;
import com.example.ms.model.Role;
import com.example.ms.model.User;
import com.example.ms.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(account);
        user.orElseThrow(() -> new UsernameNotFoundException(account + " NOT FOUND"));

        // 当登录用户需要定制功能时（如：获取登录用户权限列表，登录用户一些其它信息时），需要实现UserDetail接口
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(),
                user.get().getPassword(),
                getGrantedAuthorities(getPermissions(user.get().getRoles())));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    private List<String> getPermissions(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        Set<Permission> collection = new HashSet<>();
        for (Role role : roles) {
            privileges.add(role.getRoleName());
            collection.addAll(role.getPermissions());
        }
        for (Permission item : collection) {
            privileges.add(item.getPath());
        }
        return privileges;
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        return privileges.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Optional<User> user = userRepository.findByUserName(userName);
//        user.orElseThrow(() -> new UsernameNotFoundException(userName + " not found."));
//
//        return user.map(UserDetailsImpl::new).get();
//    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        SelectStatementProvider selectStatement = select(UserDynamicSqlSupport.id, UserDynamicSqlSupport.username, UserDynamicSqlSupport.password, UserDynamicSqlSupport.locked)
//                .from(UserDynamicSqlSupport.user)
//                .where(UserDynamicSqlSupport.username, isEqualTo(username))
//                .build().render(RenderingStrategy.MYBATIS3);
//
//        Map<String, Object> parameter = new HashMap<>();
//        parameter.put("#{username}", username);
//        User user = userRepository.selectOne(selectStatement);
//        if (user == null) throw new UsernameNotFoundException(username);
//
//        SelectStatementProvider manyPermission = select(PermissionDynamicSqlSupport.id, PermissionDynamicSqlSupport.permissionCode, PermissionDynamicSqlSupport.permissionName)
//                .from(PermissionDynamicSqlSupport.permission)
//                .join(RolePermissionDynamicSqlSupport.rolePermission).on(RolePermissionDynamicSqlSupport.permissionId, equalTo(PermissionDynamicSqlSupport.id))
//                .join(UserRoleDynamicSqlSupport.userRole).on(UserRoleDynamicSqlSupport.roleId, equalTo(RolePermissionDynamicSqlSupport.roleId))
//                .where(UserRoleDynamicSqlSupport.userId, isEqualTo(user.getId()))
//                .build()
//                .render(RenderingStrategy.MYBATIS3);
//        List<Permission> permissions = permissionRepository.selectMany(manyPermission);
//        if (!CollectionUtils.isEmpty(permissions)) {
//            Set<SimpleGrantedAuthority> sga = new HashSet<>();
//            permissions.forEach(p -> {
//                sga.add(new SimpleGrantedAuthority(p.getPermissionCode()));
//            });
//            user.setAuthorities(sga);
//        }
//
//        return user;
//    }
}
