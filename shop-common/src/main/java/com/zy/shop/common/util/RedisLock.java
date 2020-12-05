package com.zy.shop.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author: jogin
 * @date: 2020/12/5 14:57
 */
@Slf4j
public class RedisLock {

    private final static String REDIS_LOCK_VALUE = "Redis_Lock_Value";
    private final static Integer LOCK_TIME = 5;
    private final static Long SUCCESS = 1L;

    public static boolean hasExecuted(RedisTemplate redisTemplate, String key, int value){
        Integer redisValue = (Integer) redisTemplate.opsForValue().get(key);
        if (redisValue.intValue() == value){
            return true;
        }
        redisTemplate.opsForValue().setIfAbsent(key,value);
        return false;
    }

    // TODO
    public static boolean lock(RedisTemplate redisTemplate, String key) {
        int tries = 3;
        boolean locked = false;
        if (key == null) {
            log.info("redis 加锁失败，key:{} 不能为空！", key);
        }
        while (!locked && tries > 0) {
            locked = redisTemplate.opsForValue().setIfAbsent(key, REDIS_LOCK_VALUE, LOCK_TIME, TimeUnit.SECONDS);
            tries--;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error("线程被中断：{}，redis 加锁失败!", e.getMessage(), e);
            }
        }
        return locked;
    }

    public static boolean unlock(RedisTemplate redisTemplate, String key) {
        if (key == null) {
            log.info("redis 释放锁失败，key: {} 不能为空！");
        }
        boolean releaseLock = false;
        String value = (String) redisTemplate.opsForValue().get(key);
        if (key.equals(value)) {
            releaseLock = redisTemplate.delete(key);
        }
        return releaseLock;
    }

    public static boolean unlockLua(RedisTemplate redisTemplate, String key){
        if (key == null) {
            log.info("redis 释放锁失败，key: {} 不能为空！");
        }
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis('del',KEYS[1]) else return 0 end";
        RedisScript<String> redisScript = new DefaultRedisScript<>(luaScript,String.class);
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(key), REDIS_LOCK_VALUE);
        if (SUCCESS.equals(result)){
            return true;
        }
        return false;
    }
}
