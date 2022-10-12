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
public class MenuAspect {

    public final MenuService menuService;

    public MenuAspect(MenuService menuService) {
        this.menuService = menuService;
    }

    @Pointcut("execution(* com.example.ms.controller..*.*Page(..))")
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

//            System.out.println("URL : " + request.getRequestURL().toString());
//            System.out.println("HTTP_METHOD : " + request.getMethod());
//            System.out.println("getRequestURI : " + request.getRequestURI());
//            System.out.println("getContextPath : " + request.getContextPath());
//            System.out.println("getServletPath : " + request.getServletPath());
//            System.out.println("getPathInfo : " + request.getPathInfo());
//            System.out.println("getAuthType : " + request.getAuthType());
//            System.out.println("IP : " + request.getRemoteAddr());
//            System.out.println("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//            System.out.println("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        }

    }
}
