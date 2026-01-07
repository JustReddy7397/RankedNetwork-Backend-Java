package network.ranked.backend.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

/**
 * @author JustReddy
 */
@Repository
@RequiredArgsConstructor
@Getter
public class RedisApiKeyRepository {

    private final StringRedisTemplate redis;

    private static final String KEY_PREFIX = "api-key:";

    private static final Duration TTL = Duration.ofDays(1);

    public void storeKey(String apiKey) {
        redis.opsForValue().set(KEY_PREFIX + apiKey, "1", TTL);
    }

    public boolean exists(String apiKey) {
        return redis.hasKey(KEY_PREFIX + apiKey);
    }

    public void delete(String apiKey) {
        redis.delete(KEY_PREFIX + apiKey);
    }

}
