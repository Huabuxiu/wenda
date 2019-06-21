package com.example.wenda.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.wenda.async.EventHandler;
import com.example.wenda.async.EventModel;
import com.example.wenda.async.EventType;
import com.example.wenda.model.*;
import com.example.wenda.service.*;
import com.example.wenda.util.JedisAdapter;
import com.example.wenda.util.RedisKeyUtil;
import com.example.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @program: wenda
 * @description:  关注事件的处理
 * @author: Huabuxiu
 * @create: 2019-06-19 15:54
 **/
@Component
public class FeedHandler implements EventHandler {


   @Autowired
   UserService userService;

   @Autowired
    QuestionService questionService;

   @Autowired
    FeedService feedService;

   @Autowired
    FollowService followService;

   @Autowired
    JedisAdapter jedisAdapter;

    @Override
    //构造一个关注消息
    public void doHandle(EventModel model) {
//        //测试，把userId随机化一下
//        Random r = new Random();
//        model.setActorId(1+r.nextInt(10));
        Feed feed  = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            return;
        }
        //拉取模式
        feedService.addFeed(feed);

        //拉取模式
        Push(model,feed);

    }

    private void Push(EventModel model,Feed feed){

        //获得所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);
        //加入队列
        followers.add(0);

        for (int follower :
                followers) {
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));
        }
    }


    private String buildFeedData(EventModel model){
        Map<String,String> map = new HashMap<>();
        //触发的用户是通用的
        User actor = userService.getUser(model.getActorId());
        if (actor == null ){
            return null;
        }
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getName());


        if (model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)){// 更新评论或者是关注了问题
            Question question = questionService.getById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);
        }
            return null;
    }



    /**
    * @Description: 对评论和关注事件进行处理
    * @Param:
    * @return:
    * @Author: Huabuxiu
    * @Date: 2019/6/21
    */

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(
                new EventType[]{
                        EventType.COMMENT,
                        EventType.FOLLOW});
    }
}
