package com.example.wenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-03-23 14:43
 **/

@Controller
public class IndesController {

    @RequestMapping("/")
    @ResponseBody
    public String index(){
        return "Hello world!";
    }
}
