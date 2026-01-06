package network.ranked.backend.socket.packets.identity;

/**
 * @author JustReddy
 */
public record ClientPlayer(String uuid, String cachedName,
                           String serverIdentifier, String proxyIdentifier,
                           String ipAddress) {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ClientPlayer clientPlayer)) {
            return false;
        }
        return clientPlayer.uuid.equals(uuid);
    }
}