package com.example.wenda.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.example.wenda.aspect.LogAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.List;


/**
 * @program: wenda
 * @description:测试
 * @author: Huabuxiu
 * @create: 2019-03-23 14:43
 **/

//@Controller
public class IndexController {

    private static final Logger logger =  LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String index(HttpSession httpSession){
        System.out.println("method do");
        return "Hello world!"+httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                           @PathVariable("groupId") String groupId,
                          @RequestParam("type") int type,
                          @RequestParam(value = "key",required = false) String key){
        return String.format("Profile Page of %s/%d, t:%d/key:%s",groupId,userId,type,key);
    }

    @RequestMapping(path = {"/vm"})
    public String template(Model model){

        model.addAttribute("value1","huabuxiu");
        List<String> colors = Arrays.asList(new String[]{"RED", "GREEN", "BLUE"});
        model.addAttribute("colors",colors);
        return "home";
    }

    @RequestMapping(path = {"/request"})
    @ResponseBody
    public String request(Model model, HttpServletRequest request,
                          HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();

        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"})
    public RedirectView request(@PathVariable("code") int code,HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession httpSession){
        httpSession.setAttribute("msg","jump form redirect");
        RedirectView redirectView= new RedirectView("/");
        if (code == 301)
        {
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redirectView;
    }

    @RequestMapping(path = {"/admin/{key}"})
    @ResponseBody
    public String admin(@PathVariable("key") String  key,HttpServletRequest request,
                                HttpServletResponse response,
                                HttpSession httpSession){

        if ("amdin".equals(key))
        {
            return "hello  admin";
        }
        throw  new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }


}
