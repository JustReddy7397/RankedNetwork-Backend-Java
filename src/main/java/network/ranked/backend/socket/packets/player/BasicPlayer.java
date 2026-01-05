package network.ranked.backend.socket.packets.player;

import java.util.UUID;

/**
 * @author JustReddy
 */
public record BasicPlayer(UUID uuid, String cachedName) {
}
