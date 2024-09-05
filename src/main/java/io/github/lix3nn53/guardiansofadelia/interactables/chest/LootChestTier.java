package io.github.lix3nn53.guardiansofadelia.interactables.chest;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.economy.Coin;
import io.github.lix3nn53.guardiansofadelia.economy.CoinType;
import io.github.lix3nn53.guardiansofadelia.items.Consumable;
import io.github.lix3nn53.guardiansofadelia.items.GearLevel;
import io.github.lix3nn53.guardiansofadelia.items.enchanting.EnchantStone;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum LootChestTier {
    TIER_ONE,
    TIER_TWO,
    TIER_THREE,
    TIER_FOUR;

    public static LootChestTier fromLevel(int level) {
        if (level < 30) {
            return LootChestTier.TIER_ONE;
        } else if (level < 50) {
            return LootChestTier.TIER_TWO;
        } else if (level < 70) {
            return LootChestTier.TIER_THREE;
        } else {
            return LootChestTier.TIER_FOUR;
        }

    }

    public int getMinSkillLevel() {
        switch (this) {
            case TIER_ONE:
                return 1;
            case TIER_TWO:
                return 3;
            case TIER_THREE:
                return 5;
            case TIER_FOUR:
                return 7;
        }

        return -1;
    }

    public int getMaxSkillLevel() {
        switch (this) {
            case TIER_ONE:
                return 4;
            case TIER_TWO:
                return 6;
            case TIER_THREE:
                return 8;
            case TIER_FOUR:
                return 10;
        }

        return -1;
    }

    public List<ItemStack> getLoot() {
        List<ItemStack> list = new ArrayList<>();

        float random = (float) Math.random();

        //ENCHANT_STONES
        if (random > 0.85f) {
            int minTierIndex = getMinStoneTierIndex();
            int maxTierIndex = getMaxStoneTierIndex();
            int minUses = 1;
            int maxUses = 3;

            int amountRandom = GuardiansOfAdelia.RANDOM.nextInt(2);

            EnchantStone[] values = EnchantStone.values();
            for (int i = 0; i < amountRandom; i++) {
                int tierRandom = 0;
                if (maxTierIndex != 0) {
                    tierRandom = GuardiansOfAdelia.RANDOM.nextInt(maxTierIndex - minTierIndex) + minTierIndex;
                }

                int usesRandom = GuardiansOfAdelia.RANDOM.nextInt(maxUses - minUses) + minUses;
                ItemStack itemStack = values[tierRandom].getItemStack(usesRandom);
                list.add(itemStack);
            }
        } else if (random > 0.6f) { // CONSUMABLES
            int minSkillLevel = getMinSkillLevel();
            int maxSkillLevel = getMaxSkillLevel();
            int minUses = 4;
            int maxUses = 10;

            int amountRandom = GuardiansOfAdelia.RANDOM.nextInt(2);

            Consumable[] values = Consumable.values();
            for (int i = 0; i < amountRandom; i++) {
                int enumRandom = GuardiansOfAdelia.RANDOM.nextInt(values.length);
                int skillLevelRandom = GuardiansOfAdelia.RANDOM.nextInt(maxSkillLevel - minSkillLevel) + minSkillLevel;

                int usesRandom = GuardiansOfAdelia.RANDOM.nextInt(maxUses - minUses) + minUses;
                ItemStack itemStack = values[enumRandom].getItemStack(skillLevelRandom, usesRandom);
                list.add(itemStack);
            }
        }

        int amountRandom = GuardiansOfAdelia.RANDOM.nextInt(getMaxCoinAmount()) + 1;

        Coin coin = new Coin(CoinType.COPPER, amountRandom);
        list.add(coin.getItemStack());

        return list;
    }

    public int getMinStoneTierIndex() {
        switch (this) {
            case TIER_ONE:
            case TIER_TWO:
                return 0;
            case TIER_THREE:
                return 1;
            case TIER_FOUR:
                return 2;
        }

        return -1;
    }

    public int getMaxStoneTierIndex() {
        switch (this) {
            case TIER_ONE:
                return 0;
            case TIER_TWO:
                return 1;
            case TIER_THREE:
                return 2;
            case TIER_FOUR:
                return 3;
        }

        return -1;
    }

    public String getMobKey() {
        return switch (this) {
            case TIER_ONE -> "lootChestTierOne";
            case TIER_TWO -> "lootChestTierOne";
            case TIER_THREE -> "lootChestTierOne";
            case TIER_FOUR -> "lootChestTierOne";
        };
    }

    public int getMaxCoinAmount() {
        return switch (this) {
            case TIER_ONE -> 8;
            case TIER_TWO -> 16;
            case TIER_THREE -> 24;
            case TIER_FOUR -> 32;
        };
    }

    private GearLevel getMinGearLevel() {
        return switch (this) {
            case TIER_ONE -> GearLevel.ZERO;
            case TIER_TWO -> GearLevel.THREE;
            case TIER_THREE -> GearLevel.FIVE;
            case TIER_FOUR -> GearLevel.SEVEN;
        };

    }

    private GearLevel getMaxGearLevel() {
        return switch (this) {
            case TIER_ONE -> GearLevel.THREE;
            case TIER_TWO -> GearLevel.FIVE;
            case TIER_THREE -> GearLevel.SEVEN;
            case TIER_FOUR -> GearLevel.NINE;
        };
    }

    public GearLevel getRandomGearLevel() {
        int random = GuardiansOfAdelia.RANDOM.nextInt(getMaxGearLevel().ordinal() - getMinGearLevel().ordinal()) + getMinGearLevel().ordinal();
        return GearLevel.values()[random];
    }
}
