package network.ranked.backend.socket.packets.identity;

import lombok.Getter;
import network.ranked.backend.socket.packets.identity.enums.ClientEnvironment;
import network.ranked.backend.socket.packets.identity.enums.ClientType;
import network.ranked.backend.socket.packets.player.BasicPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author JustReddy
 */
@Getter
public class ClientIdentity {

    private final ClientType type;
    private final ClientEnvironment environment;
    private final UUID identifier;
    private final String address;
    private final int port;
    private final int maxPlayerCount;
    private final List<BasicPlayer> initialOnlinePlayers;

    public ClientIdentity(ClientType type, ClientEnvironment environment,
                          UUID identifier, String address, int port,
                          int maxPlayerCount,
                          List<BasicPlayer> initialOnlinePlayers) {
        this.type = type;
        this.environment = environment;
        this.identifier = identifier;
        this.address = address;
        this.port = port;
        this.maxPlayerCount = maxPlayerCount;
        this.initialOnlinePlayers = initialOnlinePlayers;
    }

    public ClientIdentity(ClientType type, ClientEnvironment environment,
                          UUID identifier, String address, int port,
                          int maxPlayerCount) {
        this(type, environment, identifier, address, port, maxPlayerCount, new ArrayList<>());
    }

}
