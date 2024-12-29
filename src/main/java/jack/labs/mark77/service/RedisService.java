package jack.labs.mark77.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public boolean add(String queueName, String key, long value){
        return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(queueName, key, value));
    }

}
