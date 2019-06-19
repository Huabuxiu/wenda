package com.example.wenda.async.handler;

import com.example.wenda.async.EventHandler;
import com.example.wenda.async.EventModel;
import com.example.wenda.async.EventType;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.Message;
import com.example.wenda.model.User;
import com.example.wenda.service.MessageService;
import com.example.wenda.service.UserService;
import com.example.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-06-19 15:54
 **/
public class FollowHandler  implements EventHandler {

   public final String address = "http://127.0.0.1:8080";

   @Autowired
   UserService userService;

   @Autowired
   MessageService messageService;

    @Override
    //发一条私信
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());

        if (model.getEntityType() == EntityType.ENTITY_QUESTION){
            message.setContent("用户"+user.getName()+
                    "关注了你的问题"+address+"/question/"+model.getEntityId());
        }else if (model.getEntityType() == EntityType.ENTITY_USER){
            message.setContent("用户"+user.getName()+
                    "关注了你,"+address+"/user/"+model.getActorId());
        }
        messageService.addmessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
