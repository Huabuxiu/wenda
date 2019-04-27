package com.example.wenda.service;


import com.example.wenda.dao.MessageDAO;
import com.example.wenda.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-03-25 20:31
 **/
@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    MessageDAO messageDAO;

    public int addmessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId,int offset, int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }

    public int getConvesationUnreadCount(int userId, String conversationId){
        return messageDAO.getConvesationUnreadCount(userId,conversationId);
    }

    public void updateConvesationUnread(int userId, String conversationId){
        messageDAO.updateConvesationUnread(userId, conversationId);
    }
}
