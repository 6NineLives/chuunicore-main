package io.github.lix3nn53.guardiansofadelia.items.list.armors;

import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public enum ArmorSlot {
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS;

    public static ArmorSlot getArmorSlot(ItemStack itemStack) {
        Material material = itemStack.getType();

        if (material.equals(Material.AIR)) return null;
        String type = material.name();
        if (type.endsWith("_HELMET") || type.endsWith("_SKULL"))
            return HELMET;
        else if (type.endsWith("_CHESTPLATE")) return CHESTPLATE;
        else if (type.endsWith("_LEGGINGS")) return LEGGINGS;
        else if (type.endsWith("_BOOTS")) return BOOTS;
        else if (material.equals(CosmeticManager.COSMETIC_MATERIAL)) {
            if (CosmeticManager.isAppliedHelmetSkin(itemStack)) {
                return HELMET;
            }
        }
        return null;
    }

    public int getSlot() {
        switch (this) {
            case CHESTPLATE:
                return 6;
            case LEGGINGS:
                return 7;
            case BOOTS:
                return 8;
        }
        return 5;
    }

    public ItemStack getItemStack(Player player) {
        PlayerInventory inventory = player.getInventory();
        switch (this) {
            case CHESTPLATE:
                return inventory.getChestplate();
            case LEGGINGS:
                return inventory.getLeggings();
            case BOOTS:
                return inventory.getBoots();
        }
        return inventory.getHelmet();
    }

    public float getAttributeReduction() {
        switch (this) {
            case HELMET:
            case BOOTS:
                return 0.4f;
            case LEGGINGS:
                return 0.7f;
        }

        return 1;
    }

    public String getDisplayName() {
        switch (this) {
            case CHESTPLATE:
                return "Chestplate";
            case LEGGINGS:
                return "Leggings";
            case BOOTS:
                return "Boots";
        }

        return "Helmet";
    }

    public int getReqLevelAddition() {
        switch (this) {
            case CHESTPLATE:
                return 8;
            case LEGGINGS:
                return 6;
            case HELMET:
                return 4;
            case BOOTS:
                return 2;
        }

        return 9999;
    }
}
