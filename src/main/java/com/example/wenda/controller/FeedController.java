package com.example.wenda.controller;

import com.example.wenda.model.Comment;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.Feed;
import com.example.wenda.model.HostHolder;
import com.example.wenda.service.CommentService;
import com.example.wenda.service.FeedService;
import com.example.wenda.service.FollowService;
import com.example.wenda.service.QuestionService;
import com.example.wenda.util.JedisAdapter;
import com.example.wenda.util.RedisKeyUtil;
import com.example.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-04-01 21:46
 **/
@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    QuestionService questionService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @RequestMapping(path = {"/pushfeeds"},method = {RequestMethod.POST,RequestMethod.GET})
    public String getPushFees(Model model){
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId),0,10);
        List<Feed> feeds = new ArrayList<>();
        for (String feedid:feedIds){
            Feed feed = feedService.getById(Integer.parseInt(feedid));
            if (feed != null){
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds",feedIds);
        return "feeds";
    }


    @RequestMapping(path = {"/pullfeeds"},method = {RequestMethod.POST,RequestMethod.GET})
    public String getPullFees(Model model){
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
      List<Integer> followers = new ArrayList<>();

      if (localUserId != 0){
          followers = followService.getFollowees(localUserId,EntityType.ENTITY_USER,Integer.MAX_VALUE);
      }
      List<Feed> feeds = feedService.getUserFeed(Integer.MAX_VALUE,followers,10);
        model.addAttribute("feeds",feeds);
        return "feeds";
    }
}
