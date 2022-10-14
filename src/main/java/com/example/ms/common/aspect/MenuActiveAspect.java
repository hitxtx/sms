package com.example.ms.common.aspect;

import com.example.ms.module.system.model.bo.Menu;
import com.example.ms.module.system.service.MenuService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Aspect
@Component
public class MenuActiveAspect {

    public final MenuService menuService;

    public MenuActiveAspect(MenuService menuService) {
        this.menuService = menuService;
    }

    //    @Pointcut("execution(* com.example.ms.module..controller..*.*Index(..))")
    @Pointcut("@annotation(com.example.ms.common.annotation.MenuMarker)")
    public void active() {

    }

    @After("active()")
    public void before(JoinPoint joinPoint) {
        List<Menu> menuTree = menuService.list(0L);
        List<Menu> menuList = menuService.listAllEnabled();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
//            HttpServletResponse response = attributes.getResponse();
            String servletPath = request.getServletPath();
            for (Menu menu : menuList) {
                if (servletPath.equalsIgnoreCase(menu.getPath())) {
                    request.setAttribute("menuActive", menu);
                    break;
                }
            }
            request.setAttribute("menuTree", menuTree);
        }

    }
}
