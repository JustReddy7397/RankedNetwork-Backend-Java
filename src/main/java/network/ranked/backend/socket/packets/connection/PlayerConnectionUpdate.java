package network.ranked.backend.socket.packets.connection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import network.ranked.backend.socket.packets.identity.ClientPlayer;

/**
 * @author JustReddy
 */
@ToString
@Getter
public class PlayerConnectionUpdate {

    private final ClientPlayer clientPlayer;
    private final String ipAddress;
    private final boolean auth;

    public PlayerConnectionUpdate(@JsonProperty ClientPlayer clientPlayer,
                                  @JsonProperty String ipAddress,
                                  @JsonProperty boolean auth) {
        this.clientPlayer = clientPlayer;
        this.ipAddress = ipAddress;
        this.auth = auth;
    }


}
