package cn.edu.nsu.onlinejudge.controller.interceptor;

import cn.edu.nsu.onlinejudge.entity.LoginTicket;
import cn.edu.nsu.onlinejudge.entity.User;
import cn.edu.nsu.onlinejudge.service.UserService;
import cn.edu.nsu.onlinejudge.util.CookieUtil;
import cn.edu.nsu.onlinejudge.common.Enum.LoginTicketStatusEnum;
import cn.edu.nsu.onlinejudge.common.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor{

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;


    /**
     * 在 Controller 方法处理之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求中获取 Cookie 并通过 Cookie 获取登录凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            // 查询登录凭证实体
            LoginTicket loginTicket = userService.findLoginTicket(ticket);

            // 检查登录凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == LoginTicketStatusEnum.SUCCESS && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserByUserId(loginTicket.getUserId());

                // 在本次请求中持有用户
                hostHolder.setUser(user);
            }
        }

        return true;
    }


    /**
     * 在模板引擎调用之前将 user 加入 modelAndView
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();

        if (user != null && modelAndView != null) {
             modelAndView.addObject("loginUser", user);
        }
    }


    /**
     * 在本次请求结束后清除 hostHolder 中的 user 对象
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
