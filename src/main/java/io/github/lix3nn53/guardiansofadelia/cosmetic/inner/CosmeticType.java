package io.github.lix3nn53.guardiansofadelia.cosmetic.inner;

import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticManager;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ShieldGearType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.WeaponGearType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;

public enum CosmeticType {
    //One handed Melee Weapons
    WEAPON_SKIN_SWORD, // Normal attack speed, normal damage, can equip with shield
    WEAPON_SKIN_DAGGER, // Fast attack speed, low damage, can dual wield
    //Two handed Melee Weapons
    WEAPON_SKIN_BATTLE_AXE, // Slow attack speed, max damage
    WEAPON_SKIN_WAR_HAMMER, // Slow attack speed, normal damage, sweep attack
    WEAPON_SKIN_GREAT_SWORD, // Normal attack speed, high damage
    //Ranged + Melee weapons
    WEAPON_SKIN_SPEAR, // Normal attack speed, normal damage, ranged + melee
    //Ranged Weapons
    WEAPON_SKIN_BOW, // Normal attack speed, normal damage
    WEAPON_SKIN_CROSSBOW, // slow attack speed, high damage
    //Magic Weapons
    WEAPON_SKIN_STAFF, // high damage, Skill cooldown reduction
    WEAPON_SKIN_WAND,
    SHIELD_SKIN,
    HELMET_SKIN,
    COSMETIC_BACK;

    public boolean canChangeColor() {
        switch (this) {
            case HELMET_SKIN, COSMETIC_BACK -> {
                return true;
            }
        }

        return false;
    }

    public Cosmetic load(ConfigurationSection section) {
        String name = section.getString("name");
        int customModelData = section.getInt("customModelData");

        return new Cosmetic(this, name, customModelData);
    }

    public EquipmentSlot getEquipmentSlot() {
        switch (this) {
            case HELMET_SKIN, COSMETIC_BACK -> {
                return EquipmentSlot.HEAD;
            }
            case SHIELD_SKIN -> {
                return EquipmentSlot.OFF_HAND;
            }
        }

        return EquipmentSlot.HAND;
    }

    public boolean isOnPlayer() {
        switch (this) {
            case COSMETIC_BACK -> {
                return false;
            }
        }

        return true;
    }

    public int getIdOffset() {
        return this.ordinal() * 1000;
    }

    public Material getMaterial() {
        switch (this) {
            case WEAPON_SKIN_SWORD -> {
                return WeaponGearType.SWORD.getMaterial();
            }
            case WEAPON_SKIN_DAGGER -> {
                return WeaponGearType.DAGGER.getMaterial();
            }
            case WEAPON_SKIN_BATTLE_AXE -> {
                return WeaponGearType.BATTLE_AXE.getMaterial();
            }
            case WEAPON_SKIN_WAR_HAMMER -> {
                return WeaponGearType.WAR_HAMMER.getMaterial();
            }
            case WEAPON_SKIN_GREAT_SWORD -> {
                return WeaponGearType.GREAT_SWORD.getMaterial();
            }
            case WEAPON_SKIN_SPEAR -> {
                return WeaponGearType.SPEAR.getMaterial();
            }
            case WEAPON_SKIN_BOW -> {
                return WeaponGearType.BOW.getMaterial();
            }
            case WEAPON_SKIN_CROSSBOW -> {
                return WeaponGearType.CROSSBOW.getMaterial();
            }
            case WEAPON_SKIN_STAFF -> {
                return WeaponGearType.STAFF.getMaterial();
            }
            case WEAPON_SKIN_WAND -> {
                return WeaponGearType.WAND.getMaterial();
            }
            case SHIELD_SKIN -> {
                return ShieldGearType.SHIELD.getMaterial();
            }
        }

        return CosmeticManager.COSMETIC_MATERIAL;
    }

    public CosmeticSlot getCosmeticSlot() {
        switch (this) {
            case WEAPON_SKIN_SWORD, WEAPON_SKIN_WAND, WEAPON_SKIN_STAFF, WEAPON_SKIN_CROSSBOW,
                    WEAPON_SKIN_BOW, WEAPON_SKIN_SPEAR, WEAPON_SKIN_GREAT_SWORD, WEAPON_SKIN_BATTLE_AXE,
                    WEAPON_SKIN_DAGGER, WEAPON_SKIN_WAR_HAMMER -> {
                return CosmeticSlot.HAND;
            }
            case SHIELD_SKIN -> {
                return CosmeticSlot.OFFHAND;
            }
            case HELMET_SKIN -> {
                return CosmeticSlot.HEAD;
            }
            case COSMETIC_BACK -> {
                return CosmeticSlot.BACK;
            }
        }

        return null;
    }
}
