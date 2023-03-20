package cn.edu.nsu.onlinejudge.controller.interceptor;


import cn.edu.nsu.onlinejudge.annotation.LoginRequired;
import cn.edu.nsu.onlinejudge.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断拦截是的是否是一个方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 获取方法对象
            Method method = handlerMethod.getMethod();
            // 从方法中获取注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            // 如果该注解不为空则说明该方法需要登录
            // 但是此时用户没有登录则返回 false 拒绝访问
            if (loginRequired != null && hostHolder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }

        return true;
    }
}
