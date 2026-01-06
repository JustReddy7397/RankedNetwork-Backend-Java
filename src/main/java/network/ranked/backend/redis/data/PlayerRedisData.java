package network.ranked.backend.redis.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import network.ranked.backend.socket.packets.identity.ClientPlayer;

import java.util.UUID;

/**
 * @author JustReddy
 */
@RequiredArgsConstructor
@Getter
public class PlayerRedisData {

    private final String uniqueId;
    private final String cachedName;
    private final String serverIdentifier;
    private final String proxyIdentifier;

}
