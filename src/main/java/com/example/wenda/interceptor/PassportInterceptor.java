package com.example.wenda.interceptor;

import com.example.wenda.dao.LoginTicketDAO;
import com.example.wenda.dao.UserDAO;
import com.example.wenda.model.HostHolder;
import com.example.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.wenda.model.LoginTicket;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @program: wenda
 * @description:    未登录拦截器验证
 * @author: Huabuxiu
 * @create: 2019-03-26 20:55
 **/
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        //session 验证，sessionId的名称为ticket
        if (httpServletRequest.getCookies() != null)
        {
            for (Cookie cookie:httpServletRequest.getCookies()){
                if (cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null){
            //获取session
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            //不存在，过时，失效
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                return true;
            }
            //获取用户数据
            User user = userDAO.selectUserById(loginTicket.getUserId());
            //设置用户数据到全局变量中去
            hostHolder.setUser(user);
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
            if (modelAndView != null && hostHolder.getUser() != null){
                //Controller执行后在modelAndView中设置user
                modelAndView.addObject("user",hostHolder.getUser());
            }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //执行完清理全局变量
        hostHolder.clear();
    }
}
