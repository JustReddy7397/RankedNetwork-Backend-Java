package network.ranked.backend.socket.packets.identity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import network.ranked.backend.socket.packets.identity.enums.ClientEnvironment;
import network.ranked.backend.socket.packets.identity.enums.ClientType;

import java.util.UUID;

/**
 * @author JustReddy
 */
@ToString
@Getter
public class ClientIdentity {

    private final ClientType type;
    private final ClientEnvironment environment;
    private final UUID identifier;
    private final String address;
    private final int port;
    private final int maxPlayerCount;

    @JsonCreator
    public ClientIdentity(
            @JsonProperty("type") ClientType type,
            @JsonProperty("environment") ClientEnvironment environment,
            @JsonProperty("identifier") UUID identifier,
            @JsonProperty("address") String address,
            @JsonProperty("port") int port,
            @JsonProperty("maxPlayerCount") int maxPlayerCount
    ) {
        this.type = type;
        this.environment = environment;
        this.identifier = identifier;
        this.address = address;
        this.port = port;
        this.maxPlayerCount = maxPlayerCount;
    }

}
