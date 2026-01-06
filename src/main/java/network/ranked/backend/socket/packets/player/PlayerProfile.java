package network.ranked.backend.socket.packets.player;

import network.ranked.backend.socket.packets.player.enums.PlayerStatus;

import java.util.UUID;

/**
 * @author JustReddy
 */
public class PlayerProfile extends DefaultPlayerProfile {

    public PlayerProfile(UUID uniqueId, String cachedName, String discordId, PlayerStatus status) {
        super(uniqueId, cachedName, discordId, status);
    }
}
