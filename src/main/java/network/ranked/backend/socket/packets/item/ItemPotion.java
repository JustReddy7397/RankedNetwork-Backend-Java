package network.ranked.backend.socket.packets.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author JustReddy
 */
@AllArgsConstructor
@Getter
public class ItemPotion {

    private final int potionId;
    private final String potionType;
    private final long duration;
    private final int amplifier;
    private final boolean splash;

}
