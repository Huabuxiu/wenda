package com.example.wenda.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

/**
 * @program: wenda
 * @description:
 * @author: Huabuxiu
 * @create: 2019-04-25 19:54
 **/
@Service
public class JedisAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool();
    }


    public  long sadd(String key, String value){
        Jedis  jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public long lpush(String key,String  value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key,value);
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public List<String> lrange(String key, int start, int stop) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key,start,stop);
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Jedis getjedis(){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
           return jedis;
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Transaction multi(Jedis jedis){
        try {
            return jedis.multi();
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
        }
        return null;
    }

    public  List<Object> exec(Transaction transaction, Jedis jedis){
        try{
            return transaction.exec();
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
            transaction.discard();  //回滚
        } finally {
            if (transaction!=null){
                try{
                    transaction.close();
                }catch (Exception e){
                    logger.error("事务关闭异常"+e.getMessage());
                }
            }

            if (jedis !=null){
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zrevrange(String key,int start, int end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key,start,end);
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    public long zcard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }


    public Double zscore(String key, String member){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key,member);
        }catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

}
