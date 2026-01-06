package network.ranked.backend.socket.packets.player;

import network.ranked.backend.socket.packets.player.enums.PlayerStatus;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * @author JustReddy
 */
@Document(collection = "profiles")
public class PlayerProfile extends DefaultPlayerProfile {

    public PlayerProfile(UUID uniqueId, String cachedName, String discordId, PlayerStatus status) {
        super(uniqueId, cachedName, discordId, status);
    }
}
