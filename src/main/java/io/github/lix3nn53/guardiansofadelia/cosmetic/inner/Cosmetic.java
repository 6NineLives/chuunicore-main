package io.github.lix3nn53.guardiansofadelia.cosmetic.inner;

import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class Cosmetic {

    private final CosmeticType type;
    private final String name;
    private final int customModelData;

    public Cosmetic(CosmeticType type, String name, int customModelData) {
        this.type = type;
        this.name = name;
        this.customModelData = customModelData;
    }

    public CosmeticType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void applyTo(ItemStack itemStack, CosmeticColor color, int tintIndex) {
        itemStack.setType(type.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(customModelData);

        if (type.canChangeColor() && color != null) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
            leatherArmorMeta.setColor(color.getTint(tintIndex));
        }

        itemStack.setItemMeta(itemMeta);
    }

    public ItemStack getShowcase(CosmeticColor color, int tintIndex) {
        ItemStack itemStack = new ItemStack(type.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemMeta.setDisplayName(name);

        List<String> lore = new ArrayList<>();
        lore.add(ChatPalette.GRAY + "Type: " + type);
        lore.add("");

        if (type.canChangeColor() && color != null) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
            leatherArmorMeta.setColor(color.getTint(tintIndex));
            leatherArmorMeta.addItemFlags(ItemFlag.HIDE_DYE);
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
