package com.example.ms.security;

import com.example.ms.model.Role;
import com.example.ms.model.User;
import com.example.ms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(account);
        user.orElseThrow(() -> new UsernameNotFoundException("账号或密码不正确"));
        if (user.get().getDeletedFlag()) {
            throw new UsernameNotFoundException("账号或密码不正确");
        }
        if (!user.get().getStatus()) {
            throw new UsernameNotFoundException("账号已停用");
        }
//        if (!user.get().getUnlockFlag()) {
//            throw new UsernameNotFoundException("密码错误次数过多，账号已锁定");
//        }

        // 当登录用户需要定制功能时（如：获取登录用户权限列表，登录用户一些其它信息时），需要实现UserDetail接口
        return new UserDetailsImpl(user.get(), getAuthorities(user.get().getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roleList) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (roleList != null && !roleList.isEmpty()) {
            roleList.forEach(role -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));

                List<SimpleGrantedAuthority> authorityList = role.getPermissions()
                        .stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getPath()))
                        .collect(Collectors.toList());
                grantedAuthorities.addAll(authorityList);
            });
        }
        return grantedAuthorities;
    }
}
