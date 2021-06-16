package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author LCY
 * @create 2021-06-16-下午 07:20
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    // 在请求前处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 该方法上有@LoginRequired注解，即该方法必须登录，才有效
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            // ① 加了注解，但是没有登录
            if (loginRequired != null && hostHolder.getUser() == null) {
                // ③ 并重定向到登录页面
                response.sendRedirect(request.getContextPath() + "/login");
                // ② 则进行拦截
                return false;
            }
        }
        return true;
    }
}
