package com.lucky;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * Created by wukuijun on 2018/10/29
 */
public class LockRedis {

    //redis线程池
    private JedisPool jedisPool;

    //定义key
    private  String redisKey = "redis_lock";

    public LockRedis(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     *
     * @param acquireTimeout  获取超时时间
     * @param timeOut  key的有效时间
     * @return
     */
    public String getRedisLock(Long acquireTimeout, Long timeOut ){
        Jedis conn = null;
        try {
            //1.创建链接
            conn = jedisPool.getResource();
            //2.定义redis对应的key的value值，作用是知道那把锁，释放的时候有用
            String redisValue = UUID.randomUUID().toString();
            //3.设置链接超时时间和key的有效期
            int expireLock = (int)(timeOut/1000);
            Long endTime = System.currentTimeMillis()+acquireTimeout;
            //4.使用循环机制，如果没有获取到锁，在规定的时间内 保证重复进行尝试获取锁
            while (System.currentTimeMillis() < endTime){
                //获取锁的过程
                //5.使用setnx命令插入redislockkey,返回1，成功获取锁
                if(conn.setnx(redisKey,redisValue) == 1){
                    //获取锁成功，设置该锁的有效时间
                    conn.expire(redisKey,expireLock);
                    return redisValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.close();
            }
        }
        return null;
    }

    //释放redis锁
    public void releaseLock(String redisValue){
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
            if(conn.get(redisKey).equals(redisValue)){
                System.out.println("释放锁..." + Thread.currentThread().getName() + ",identifierValue:" + redisValue);
                conn.del(redisKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.close();
            }
        }
    }
}
