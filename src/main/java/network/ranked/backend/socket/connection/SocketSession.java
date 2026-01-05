package network.ranked.backend.socket.connection;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import network.ranked.backend.socket.packets.identity.ClientInfo;

import java.time.Instant;

/**
 * @author JustReddy
 */
@Getter
@Setter
public class SocketSession {

    private final Instant connectedAt = Instant.now();

    private ClientInfo clientInfo;

    public boolean isIdentified() {
        return clientInfo != null;
    }

}
