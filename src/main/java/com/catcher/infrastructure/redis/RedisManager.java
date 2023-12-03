package com.catcher.infrastructure.redis;

import com.catcher.core.db.DBManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.*;

@Service
@RequiredArgsConstructor
public class RedisManager implements DBManager {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void putValue(String key, String value, long milliseconds) {
        stringRedisTemplate.opsForValue().set(key, value, milliseconds);
    }

    @Override
    public Optional<String> getValue(String key) {
        return ofNullable(stringRedisTemplate.opsForValue().get(key));
    }

    @Override
    public void deleteKey(String key) {
        stringRedisTemplate.delete(key);
    }
}
