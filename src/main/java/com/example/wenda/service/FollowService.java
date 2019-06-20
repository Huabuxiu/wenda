package com.example.wenda.service;

import com.example.wenda.util.JedisAdapter;
import com.example.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @program: wenda
 * @description:关注通知
 * @author: Huabuxiu
 * @create: 2019-06-19 14:10
 **/
@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
    * @Description: 关注一个实体 ， 可以关注问题，用户，评论等等
    * @Param:
    * @return:
    * @Author: Huabuxiu
    * @Date: 2019/6/19
    */
    public boolean follow(int userId, int entityType, int entityid){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityid);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityid);
        Date data = new Date();

        //实体的粉丝增加当前用户
        Jedis jedis = jedisAdapter.getjedis();

        Transaction tx = jedisAdapter.multi(jedis);

        tx.zadd(followerKey,data.getTime(),String.valueOf(userId));
        //当前用户对这个类实体的关注
        tx.zadd(followeeKey,data.getTime(),String.valueOf(entityid));

        List<Object> ret = jedisAdapter.exec(tx,jedis);
        //返回点赞结果
        return ret.size() == 2 && (Long)ret.get(0)>0 && (Long)ret.get(1) >0;
    }



    /**
    * @Description: 取消关注
    * @Param:
    * @return:
    * @Author: Huabuxiu
    * @Date: 2019/6/19
    */

    public boolean unfollow(int userId, int entityType, int entityid){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityid);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityid);
        Date data = new Date();

        //实体的粉丝删除当前用户
        Jedis jedis = jedisAdapter.getjedis();

        Transaction tx = jedisAdapter.multi(jedis);

        tx.zrem(followerKey,String.valueOf(userId));
        //当前用户对这个类实体的关注
        tx.zrem(followeeKey,String.valueOf(entityid));

        List<Object> ret = jedisAdapter.exec(tx,jedis);
        //返回点赞结果
        return ret.size() == 2 && (Long)ret.get(0)>0 && (Long)ret.get(1) >0;
    }

    public List<Integer> getFollowers(int entityType,int entityId,int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,0,count));
    }


    public List<Integer> getFollowers(int entityType,int entityId,int offset,int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,offset,offset+count));
    }

    public List<Integer> getFollowees(int userId,int entityId,int count){
        String followerKey = RedisKeyUtil.getFolloweeKey(userId,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,0,count));
    }


    public List<Integer> getFollowees(int userId,int entityId,int offset,int count){
        String followerKey = RedisKeyUtil.getFolloweeKey(userId,entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey,offset,offset+count));
    }


    public long getFollowerCount(int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zcard(followerKey);
    }

    public long getFolloweeCount(int userId,int entityId){
        String followerKey = RedisKeyUtil.getFolloweeKey(userId,entityId);
        return jedisAdapter.zcard(followerKey);
    }


    //统计关注列表
    private List<Integer> getIdsFromSet(Set<String> idset){
        List<Integer> ids = new ArrayList<>();
        for (String str :
                idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    public boolean IsFollower(int userId, int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId)) !=null;
    }
}
