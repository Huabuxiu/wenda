package com.example.wenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-03-23 14:43
 **/

@Controller
public class IndesController {

    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String index(){
        return "Hello world!";
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam("type") int type,
                          @RequestParam("key") String key){
        return String.format("Profile Page of %s/%d, t:%d/key:%s",groupId,userId,type,key);
    }
}
