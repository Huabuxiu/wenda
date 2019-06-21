package com.example.wenda.service;

import com.example.wenda.dao.CommentDAO;
import com.example.wenda.dao.FeedDAO;
import com.example.wenda.model.Comment;
import com.example.wenda.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: wenda
 * @description:model层
 * @author: Huabuxiu
 * @create: 2019-03-25 20:34
 **/
@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;


    public  List<Feed> getUserFeed(int maxId, List<Integer> userIds, int count){
        return feedDAO.selectUserFeeds(maxId,userIds,count);
    }

    public boolean addFeed(Feed feed){
        feedDAO.addFeed(feed);
        return feed.getId() > 0;
    }

    public Feed getById(int id){
        return feedDAO.getFeed(id);
    }
}
