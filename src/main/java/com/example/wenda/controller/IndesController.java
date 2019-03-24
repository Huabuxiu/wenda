package com.example.wenda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
}
