package jack.labs.mark77.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserQueueService {
    private final RedisService redisService;

    public boolean registerUser(long userId){
        long unixTimestamp = Instant.now().getEpochSecond();
        return redisService.add("user-queue", Long.toString(userId), unixTimestamp);
    }
}
