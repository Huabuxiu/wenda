package com.example.wenda.async.handler;

import com.example.wenda.async.EventHandler;
import com.example.wenda.async.EventModel;
import com.example.wenda.async.EventType;
import com.example.wenda.model.Message;
import com.example.wenda.model.User;
import com.example.wenda.service.MessageService;
import com.example.wenda.service.UserService;
import com.example.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-04-29 00:25
 **/
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        //实际做处理，给被点赞的人推送一条通知
        Message message = new Message();
        //系统推送
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setConversationId(message.getFromId() < message.getToId() ?
                String.format("%d_%d", message.getFromId(), message.getToId()) :
                String.format("%d_%d", message.getToId(), message.getFromId()));
        message.setCreatedDate(new Date());
        User user =  userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的评论，http://127.0.0.1:8080/question/"+model.getExts().get("questionId"));
        messageService.addmessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
      return Arrays.asList(EventType.LIKE);
    }
}
