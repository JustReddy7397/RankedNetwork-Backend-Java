package network.ranked.backend.redis.listeners;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import network.ranked.backend.redis.RedisServerRepository;
import network.ranked.backend.socket.packets.identity.ClientInfo;
import network.ranked.backend.util.Common;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @author JustReddy
 */
@Component
@RequiredArgsConstructor
public class RedisServerExpiryListener {

    private final RedisServerRepository repository;

    @PostConstruct
    public void subscribe() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        final StringRedisTemplate redis = repository.getRedis();

        if (redis == null || redis.getConnectionFactory() == null) {
            return;
        }


        container.setConnectionFactory(redis.getConnectionFactory());

        container.addMessageListener((message, pattern) -> onServerExpire(message),
                new PatternTopic("__keyevent@*__:expired"));
    }


    private void onServerExpire(Object serverOrIdentifier) {
        final String identifier =
                serverOrIdentifier instanceof String ? (String) serverOrIdentifier :
                        serverOrIdentifier instanceof ClientInfo ? ((ClientInfo) serverOrIdentifier).getIdentifier().toString() :
                                null;

        if (identifier == null) {
            Common.logWarning("SOCKET -> Received invalid server expiry message: " + serverOrIdentifier);
            return;
        }

        Common.logInfo("SOCKET -> Server with identifier " + identifier + " has expired from Redis.");

        if (!(serverOrIdentifier instanceof String)) {
            // TODO
        }

    }





}
