package io.github.lix3nn53.guardiansofadelia.guardian.attribute;

import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import org.bukkit.inventory.EquipmentSlot;

public class Attribute {
    private final AttributeType attributeType;

    private int bonusFromHelmet;
    private int bonusFromChestplate;
    private int bonusFromLeggings;
    private int bonusFromBoots;
    private int bonusFromMainhand;
    private int bonusFromOffhand;

    private int bonusTotalPassive;

    public Attribute(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public int getBonusFromEquipment() {
        return bonusFromHelmet + bonusFromChestplate + bonusFromLeggings + bonusFromBoots + bonusFromMainhand + bonusFromOffhand + bonusTotalPassive;
    }

    public int getBonusFromLevel(int playerLevel, String playerClassStr) {
        if (RPGClassManager.hasClass(playerClassStr)) {
            RPGClass playerClass = RPGClassManager.getClass(playerClassStr);
            return playerClass.getAttributeBonusForLevel(this.attributeType, playerLevel);
        }
        return 0;
    }

    public void clearAllEquipment(RPGCharacterStats rpgCharacterStats, RPGClassStats rpgClassStats, boolean fixDisplay) {
        this.bonusFromHelmet = 0;
        this.bonusFromChestplate = 0;
        this.bonusFromLeggings = 0;
        this.bonusFromBoots = 0;
        this.bonusFromMainhand = 0;
        this.bonusFromOffhand = 0;

        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            onValueChange(rpgCharacterStats, rpgClassStats);
        }
    }

    public void clearEquipmentBonus(EquipmentSlot equipmentSlot, RPGCharacterStats rpgCharacterStats,
                                    RPGClassStats rpgClassStats, boolean fixDisplay) {
        switch (equipmentSlot) {
            case HAND:
                this.bonusFromMainhand = 0;
                break;
            case OFF_HAND:
                this.bonusFromOffhand = 0;
                break;
            case FEET:
                this.bonusFromBoots = 0;
                break;
            case LEGS:
                this.bonusFromLeggings = 0;
                break;
            case CHEST:
                this.bonusFromChestplate = 0;
                break;
            case HEAD:
                this.bonusFromHelmet = 0;
                break;
        }

        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            onValueChange(rpgCharacterStats, rpgClassStats);
        }
    }

    public void setEquipmentBonus(EquipmentSlot equipmentSlot, int bonus, RPGCharacterStats rpgCharacterStats,
                                  RPGClassStats rpgClassStats, boolean fixDisplay) {
        switch (equipmentSlot) {
            case HAND:
                this.bonusFromMainhand = bonus;
                break;
            case OFF_HAND:
                this.bonusFromOffhand = bonus;
                break;
            case FEET:
                this.bonusFromBoots = bonus;
                break;
            case LEGS:
                this.bonusFromLeggings = bonus;
                break;
            case CHEST:
                this.bonusFromChestplate = bonus;
                break;
            case HEAD:
                this.bonusFromHelmet = bonus;
                break;
        }

        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            onValueChange(rpgCharacterStats, rpgClassStats);
        }
        //onBonusChangeFillEmpty(player, rpgCharacterStats, bonusPointDifference);
    }

    public void addToTotalPassive(int value, RPGCharacterStats rpgCharacterStats, RPGClassStats rpgClassStats, boolean fixDisplay) {
        this.bonusTotalPassive += value;

        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            onValueChange(rpgCharacterStats, rpgClassStats);
        }
    }

    public void removeFromTotalPassive(int value, RPGCharacterStats rpgCharacterStats, RPGClassStats rpgClassStats, boolean fixDisplay) {
        this.bonusTotalPassive -= value;

        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            onValueChange(rpgCharacterStats, rpgClassStats);
        }
    }

    public void clearTotalPassive(RPGCharacterStats rpgCharacterStats, RPGClassStats rpgClassStats, boolean fixDisplay) {
        this.bonusTotalPassive = 0;

        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            onValueChange(rpgCharacterStats, rpgClassStats);
        }
    }

    public float getIncrement(int playerLevel, String playerClass, RPGClassStats rpgClassStats) {
        float bonusFromLevel = getBonusFromLevel(playerLevel, playerClass) * attributeType.getBonusFromLevelReduction();

        return (rpgClassStats.getInvested(this.attributeType) + getBonusFromEquipment() + bonusFromLevel) * attributeType.getIncrementPerPoint();
    }

    public void onValueChange(RPGCharacterStats rpgCharacterStats, RPGClassStats rpgClassStats) {
        switch (attributeType) {
            case BONUS_MAX_HEALTH:
                rpgCharacterStats.onMaxHealthChange(rpgClassStats);
                break;
            case BONUS_MAX_MANA:
                rpgCharacterStats.onCurrentManaChange(rpgClassStats);
                break;
        }
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    /*public void onBonusChangeFillEmpty(Player player, RPGCharacterStats rpgCharacterStats, int bonusPointDifference) {
        if (bonusPointDifference > 0) {
            switch (attributeType) {
                case EARTH:
                    player.sendMessage("Bonus earth fix health");
                    float increment = attributeType.getIncrement();
                    float v = increment * bonusPointDifference;

                    float currentHealth = player.getHealth();
                    float maxHealth = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();

                    if (currentHealth == maxHealth) break;

                    float nextHealth = currentHealth + v;

                    if (nextHealth > maxHealth) {
                        nextHealth = maxHealth;
                    }

                    player.setHealth(nextHealth);
                    break;
                case WATER:
                    player.sendMessage("Bonus spirit fix mana");
                    increment = attributeType.getIncrement();
                    v = increment * bonusPointDifference;

                    int currentMana = rpgCharacterStats.getCurrentMana();

                    float maxMana = rpgCharacterStats.getTotalMaxMana();

                    if (currentMana == maxMana) break;

                    float nextMana = currentMana + v;

                    if (nextMana > maxMana) {
                        nextMana = maxMana;
                    }

                    rpgCharacterStats.setCurrentMana((int) nextMana);
                    break;
            }
        }
    }*/
}
