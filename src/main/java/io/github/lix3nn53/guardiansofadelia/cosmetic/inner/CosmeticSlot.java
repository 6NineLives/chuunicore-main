package io.github.lix3nn53.guardiansofadelia.cosmetic.inner;

import org.bukkit.inventory.EquipmentSlot;

public enum CosmeticSlot {
    BACK,
    HAND,
    OFFHAND,
    HEAD;

    public EquipmentSlot getEquipmentSlot() {
        switch (this) {
            case BACK, HEAD -> {
                return EquipmentSlot.HEAD;
            }
            case HAND -> {
                return EquipmentSlot.HAND;
            }
            case OFFHAND -> {
                return EquipmentSlot.OFF_HAND;
            }
        }

        return null;
    }

    public boolean isSkin() {
        switch (this) {
            case BACK -> {
                return false;
            }
        }

        return true;
    }
}
