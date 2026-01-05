package network.ranked.backend.util;

import network.ranked.backend.RankedNetworkBackendJavaApplication;
import network.ranked.backend.socket.packets.identity.enums.ClientType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author JustReddy
 */
public class Common {

    private static final Logger logger = LoggerFactory.getLogger(Common.class);

    public static void logInfo(String message) {
        logger.info("[{}] {}", "BACKEND", message);
    }

    public static void logWarning(String message) {
        logger.warn("[{}] {}", "BACKEND", message);
    }

    public static String getDisplayName(ClientType type, int id) {
        return switch (type) {
            case PROXY -> "Proxy-" + id;
            case LOBBY -> "Lobby-" + id;
            case LOBBY_SKYWARS -> "LobbySkyWars-" + id;
            case GAME_SKYWARS -> "GameSkyWars-" + id;
        };
    }

}
