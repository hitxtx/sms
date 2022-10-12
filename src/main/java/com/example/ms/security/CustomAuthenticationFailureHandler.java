package com.example.ms.security;

import com.example.ms.common.constant.UserConst;
import com.example.ms.module.system.model.bo.User;
import com.example.ms.module.system.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
            exception = new UsernameNotFoundException("该账号不存在");
        } else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
            exception = new DisabledException("该账号已被停用");
        } else if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            exception = new BadCredentialsException("账号或密码错误");
        } else if (exception.getClass().isAssignableFrom(LockedException.class)) {
            exception = new LockedException("你的账号已被锁定");
        } else {
            System.err.println(exception.getClass());

            String username = request.getParameter("username");
            Optional<User> userOptional = userService.getByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getStatus() && user.getUnlockFlag()) {
                    if (user.getFailedCount() < UserConst.MAX_FAILED_COUNT - 1) {
                        userService.increaseFailedCount(user);
                        exception = new BadCredentialsException("输入密码错误，还剩" + (UserConst.MAX_FAILED_COUNT - user.getFailedCount() - 1) + "次机会");
                    } else {
                        userService.lockAccount(user);
                        exception = new BadCredentialsException("连续三次输入密码错误，你的账号已被锁定24小时。");
                    }
                } else if (!user.getUnlockFlag()) {
                    if (userService.unlockWhenLockExpired(user)) {
                        exception = new BadCredentialsException("你的账号已被锁定，稍后重新登录。");
                    }
                }
            }
        }

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
