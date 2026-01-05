package network.ranked.backend.socket.services;

import lombok.RequiredArgsConstructor;
import network.ranked.backend.redis.RedisServerRepository;
import network.ranked.backend.socket.packets.identity.ClientIdentity;
import network.ranked.backend.socket.packets.identity.ClientInfo;
import network.ranked.backend.socket.packets.identity.enums.ClientType;
import network.ranked.backend.util.Common;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JustReddy
 */
@Service
@RequiredArgsConstructor
public class IdentityService {

    private final RedisServerRepository serverRepository;

    public ClientInfo handle(ClientIdentity identity) {
        final ClientInfo existing = serverRepository.getServer(identity.getIdentifier().toString());

        final String displayName = existing != null ? existing.getDisplayName() : Common.getDisplayName(existing.getType(),
                serverRepository.getServers(identity.getType()).size() + 1);

        final ClientInfo clientInfo = new ClientInfo(
                displayName,
                new ArrayList<>(), // TODO
                identity.getType(),
                identity.getEnvironment(),
                identity.getIdentifier(),
                identity.getAddress(),
                identity.getPort(),
                identity.getMaxPlayerCount());

        serverRepository.saveServer(clientInfo);

        return clientInfo;
    }

    public List<ClientInfo> getAllExceptProxy() {
        return serverRepository.getServersExcept(List.of(ClientType.PROXY));
    }

}
