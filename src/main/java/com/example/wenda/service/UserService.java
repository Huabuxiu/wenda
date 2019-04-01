package com.example.wenda.service;

import com.example.wenda.dao.LoginTicketDAO;
import com.example.wenda.dao.UserDAO;
import com.example.wenda.model.LoginTicket;
import com.example.wenda.model.User;
import com.example.wenda.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: wenda
 * @description: 用户 SERVICER
 * @author: Huabuxiu
 * @create: 2019-03-25 20:31
 **/
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id ){
        return userDAO.selectUserById(id);
    }

    public Map<String,String> register(String username,String password){
        Map<String,String> map = new HashMap<>();

        if (StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return  map;
        }

        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return  map;
        }

        User user = userDAO.selectUserByName(username);
        if (user != null){
            map.put("msg","用户名已经被注册");
            return map;
        }


        //密码强度
        user = new  User();
        user.setName(username);
        //加盐
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
            //插入数据库
        userDAO.addUser(user);
        //注册完设置session
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    public Map<String,Object> login(String username,String password){
        Map<String,Object> map= new HashMap<>();

        if (StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return  map;
        }

        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return  map;
        }

        User user = userDAO.selectUserByName(username);
        if (user == null){
            map.put("msg","用户不存在");
            return map;
        }

        //验证密码

        if (!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }



    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}
