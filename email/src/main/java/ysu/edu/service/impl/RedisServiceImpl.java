package ysu.edu.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ysu.edu.service.IRedisService;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {
    @Resource
    RedisTemplate<String,String> redisTemplate;
    @Override
    public void set(String key, String value, long time) {
        redisTemplate.opsForValue().set(key,value,time, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean expire(String key, long time) {
        return redisTemplate.expire(key,time,TimeUnit.SECONDS);
    }
}
