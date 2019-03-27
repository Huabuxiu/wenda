package com.example.wenda.controller;

import com.example.wenda.dao.LoginTicketDAO;
import com.example.wenda.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-03-26 12:51
 **/

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;


    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("next") String next,
                      @RequestParam(value="rememberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response){

        try {
            Map<String,String> map = userService.register(username,password);
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                return  "redirect:/";
            }else {
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e) {
         logger.error("注册异常"+e.getMessage());

         return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String reg(Model model,@RequestParam(value = "next", required = false) String next){
        model.addAttribute("next",next);
        return "login";
    }


/**
* @Description: 登录
* @Param:
* @return:
* @Author: Huabuxiu
* @Date: 2019/3/26
*/

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.POST})
    public String login(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                        @RequestParam(value="next", required = false) String next,
                      @RequestParam(value = "remeberme",defaultValue = "false") boolean remeberme,
                      HttpServletResponse response){

        try {
            Map<String,Object> map = userService.login(username,password);
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if (remeberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                return  "redirect:/";
            }else {
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }

        }catch (Exception e)
        {
            logger.error("注册异常"+e.getMessage());
            return "login";
        }
    }


    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

 }
