package com.zy.shop.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Jong
 * @Date: 2020/12/5 14:57
 */
@Slf4j
public class RedisLock {

    private final static String REDIS_LOCK_VALUE = "Redis_Lock_Value";
    private final static Integer LOCK_TIME = 5;
    private final static Long SUCCESS = 1L;

    public static Boolean hasExecuted(RedisTemplate<Object,Object> redisTemplate, String key, int value){
        Integer redisValue = (Integer) redisTemplate.opsForValue().get(key);
        if (redisValue!=null && redisValue == value){
            return true;
        }
        redisTemplate.opsForValue().setIfAbsent(key,value);
        return false;
    }

    // TODO 使用线程优化请求
    @SuppressWarnings("unboxing")
    public static Boolean lock(RedisTemplate<Object,Object> redisTemplate, String key) {
        int tries = 3;
        Boolean locked = false;
        if (key == null) {
            log.info("redis 加锁失败，key 不能为空！");
        }
        while (!locked && tries > 0) {
            assert key != null;
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

    public static Boolean unlock(RedisTemplate<Object,Object> redisTemplate, String key) {
        if (key == null) {
            log.info("redis 释放锁失败，key 不能为空！");
        }
        Boolean releaseLock = false;
        assert key != null;
        String value = (String) redisTemplate.opsForValue().get(key);
        if (key.equals(value)) {
            releaseLock = redisTemplate.delete(key);
        }
        return releaseLock;
    }

    public static Boolean unlockLua(RedisTemplate<Object,Object> redisTemplate, String key){
        if (key == null) {
            log.info("redis 释放锁失败，key 不能为空！");
        }
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis('del',KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript,Long.class);
        Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), REDIS_LOCK_VALUE);
        return SUCCESS.equals(result);
    }
}
