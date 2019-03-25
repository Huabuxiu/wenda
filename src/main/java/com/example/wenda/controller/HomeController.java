package com.example.wenda.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-03-25 11:46
 **/
@Controller
public class HomeController {

    private static final Logger logger =  LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(path = {"/","/index"})
    public String index(){
        return "index";
    }

}
