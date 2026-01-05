package network.ranked.backend.socket.packets.identity;

/**
 * @author JustReddy
 */
public record ClientPlayer(String uuid, String cachedName,
                           String serverIdentifier, String proxyIdentifier,
                           String ipAddress) {
}