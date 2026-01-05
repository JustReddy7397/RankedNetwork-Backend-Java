package network.ranked.backend.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import network.ranked.backend.socket.decoder.PacketDecoder;
import network.ranked.backend.socket.packets.identity.ClientInfo;
import network.ranked.backend.socket.packets.identity.enums.ClientType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author JustReddy
 */
@Repository
@RequiredArgsConstructor
@Getter
public class RedisServerRepository {

    private static final Duration EXPIRY_TIME = Duration.ofSeconds(60);

    private final StringRedisTemplate redis;
    private final PacketDecoder decoder;

    private String getServerKey(ClientType type, String id) {
        return "server:" + type + ":" + id;
    }

    public ClientInfo getServer(String identifier) {
        final Set<String> keys = redis.keys("server:*:" + identifier);

        if (keys == null || keys.isEmpty()) {
            return null;
        }

        final String key = keys.iterator().next();
        final String payload = redis.opsForValue().get(key);

        return decoder.decode(payload, ClientInfo.class);
    }

    public void saveServer(ClientInfo info) {
        try {
            redis.opsForValue().set(
                    getServerKey(info.getType(), info.getIdentifier().toString()),
                    decoder.encode(info),
                    EXPIRY_TIME
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not save server info to Redis", e);
        }
    }

    public void refreshTtl(String identifier) {
        final Set<String> keys = redis.keys("server:*:" + identifier);

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            redis.expire(key, EXPIRY_TIME);
        }
    }

    public List<ClientInfo> getServers(ClientType type) {
        final String pattern = type == null ? "server:*" : "server:" + type + ":*";

        final Set<String> keys = redis.keys(pattern);

        if (keys == null || keys.isEmpty()) {
            return List.of();
        }

        final List<ClientInfo> servers = new ArrayList<>();

        for (String key : keys) {
            final String json = redis.opsForValue().get(key);
            ClientInfo info = decoder.decode(json, ClientInfo.class);
            if (info != null) {
                servers.add(info);
            }
        }
        return servers;
    }

    public List<ClientInfo> getServersExcept(List<ClientType> types) {
        final Set<String> keys = redis.keys("server:*");

        if (keys == null || keys.isEmpty()) {
            return List.of();
        }

        final List<ClientInfo> servers = new ArrayList<>();

        for (String key : keys) {
            final String json = redis.opsForValue().get(key);
            ClientInfo info = decoder.decode(json, ClientInfo.class);
            if (info != null && (types == null || !types.contains(info.getType()))) {
                servers.add(info);
            }
        }
        return servers;
    }

    public void deleteServer(String identifier) {
        final Set<String> keys =
                redis.keys("server:*:" + identifier);

        if (keys != null && !keys.isEmpty()) {
            redis.delete(keys);
        }
    }

}
