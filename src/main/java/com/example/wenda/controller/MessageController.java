package com.example.wenda.controller;


import com.example.wenda.model.HostHolder;
import com.example.wenda.model.Message;
import com.example.wenda.model.User;
import com.example.wenda.model.ViewObject;
import com.example.wenda.service.MessageService;
import com.example.wenda.service.UserService;
import com.example.wenda.util.WendaUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-04-02 09:24
 **/
@Controller
public class MessageController {
    private static final Logger logger =  LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;


    //增加一条私信
    @RequestMapping(path = {"/msg/addMessage"},method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){

        try {
                if (hostHolder.getUser() == null)
                {
                    return WendaUtil.getJSONString(999,"未登录");
                }
                User user = userService.selectByName(toName);
                if (user==null){
                    return WendaUtil.getJSONString(1,"用户不存在");
                }
                Message message = new Message();
                message.setContent(content);
                message.setCreatedDate(new Date());
                message.setFromId(hostHolder.getUser().getId());
                message.setToId(user.getId());
                int fromId= message.getFromId();
                int toId =message.getToId();
                message.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
                messageService.addmessage(message);
                return WendaUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("增加私信失败"+e.getMessage());
            return WendaUtil.getJSONString(1,"插入站内信失败");
        }
    }

    //当前用户与站内所有用户的私信列表
    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model){
        try {
            if (hostHolder.getUser() == null){
                return "redirect:/reglogin";
            }
            int localUserId = hostHolder.getUser().getId();
            List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
            List<ViewObject> conversations = new ArrayList<>();
            for (Message message : conversationList)
            {
                ViewObject vo = new ViewObject();
                vo.set("conversation",message);
                int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
                User user= userService.getUser(targetId);
                vo.set("user",user);
                vo.set("unread",messageService.getConvesationUnreadCount(localUserId,message.getConversationId()));
                //未读条目代做
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            logger.error("获取信息列表失败"+e.getMessage());
        }

        return "letter";
    }



    //两个用户之间的所有私信
    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId") String conversationId){
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message msg : conversationList){
                ViewObject vo = new ViewObject();
                vo.set("message",msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                vo.set("userName",user.getName());
                messages.add(vo);
                messageService.updateConvesationUnread(hostHolder.getUser().getId(),conversationId);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("获取消息失败"+e.getMessage());
        }
        return "letterDetail";
    }
}
