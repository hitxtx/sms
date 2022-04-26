package com.example.ms.service;

import com.example.ms.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 自定义用户信息
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private boolean status;
    private List<GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.status = user.getStatus();
//		this.authorities = user.getRoles().stream().filter(Role::getRoleName)
//				.map(SimpleGrantedAuthority::new)
//				.collect(Collectors.toList());
    }

    public UserDetailsImpl() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {

        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }

}
