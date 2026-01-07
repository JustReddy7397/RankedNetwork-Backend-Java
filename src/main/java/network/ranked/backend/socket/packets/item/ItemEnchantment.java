package network.ranked.backend.socket.packets.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author JustReddy
 */
@AllArgsConstructor
@Getter
public class ItemEnchantment {

    private String name;
    private int level;

}
