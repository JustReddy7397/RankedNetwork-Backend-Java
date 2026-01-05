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
public class ClientInfo extends ClientIdentity {

    private final String displayName;
    private final List<ClientPlayer> onlinePlayers;

    public ClientInfo(String displayName, List<ClientPlayer> onlinePlayers, ClientType type, ClientEnvironment environment, UUID identifier, String address, int port, int maxPlayerCount) {
        this.displayName = displayName;
        this.onlinePlayers = onlinePlayers;
        super(type, environment, identifier, address, port, maxPlayerCount, new ArrayList<>());
    }
}
