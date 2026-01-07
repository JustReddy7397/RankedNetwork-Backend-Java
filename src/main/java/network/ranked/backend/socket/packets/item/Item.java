package network.ranked.backend.socket.packets.item;

import lombok.Getter;

import java.util.List;

/**
 * @author JustReddy
 */
@Getter
public class Item {

    private final String material;
    private final int amount;
    private final String name;
    private final List<ItemEnchantment> enchantments;
    private final ItemPotion potion;
    private final List<String> description;

    public Item(String material, int amount, String name, List<ItemEnchantment> enchantments, ItemPotion potion, List<String> description) {
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.enchantments = enchantments;
        this.potion = potion;
        this.description = description;
    }

    public Item(String material, int amount, String name, List<ItemEnchantment> enchantments, ItemPotion potion) {
        this(material, amount, name, enchantments, potion, List.of());
    }

    public Item(String material, int amount, String name, List<ItemEnchantment> enchantments) {
        this(material, amount, name, enchantments, null);
    }

    public Item(String material, int amount, String name) {
        this(material, amount, name, List.of());
    }

    public Item(String material, String name) {
        this(material, 1, name);
    }


}
