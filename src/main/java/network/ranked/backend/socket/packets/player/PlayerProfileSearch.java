package network.ranked.backend.socket.packets.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author JustReddy
 */
@AllArgsConstructor
@Getter
public class PlayerProfileSearch {

    private final Type type;
    private final String query;

    public enum Type {
        UUID,
        NAME
    }
}
