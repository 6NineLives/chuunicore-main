package io.github.lix3nn53.guardiansofadelia.creatures.pets;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.rpginventory.slots.EggSlot;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.utilities.PersistentDataContainerUtil;
import io.github.lix3nn53.guardiansofadelia.utilities.centermessage.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PetExperienceManager {

    public static void giveExperienceToActivePet(Player owner, int expToGive) {
        if (PetManager.hasPet(owner)) {
            int eggExperience = getEggExperience(owner);
            if (eggExperience > -1) {
                int currentLevel = getLevelFromExp(eggExperience);
                int nextExperience = eggExperience + expToGive;
                int nextLevel = getLevelFromExp(nextExperience);

                updateEggExp(owner, nextExperience, nextLevel);

                if (currentLevel < nextLevel) {
                    levelUp(owner, nextLevel);
                }
            }
        }
    }

    public static int getLevelFromExp(int eggExp) {
        int totalExp = 0;
        for (int level = 1; level < 12; level++) {
            totalExp += getNextExperienceTarget(level);

            if (totalExp > eggExp) return level;
        }

        return 1;
    }

    public static int getNextExperienceTarget(int eggLevel) {
        int result = 40;

        if (eggLevel == 2) {
            result += 90;
        } else if (eggLevel == 3) {
            result += 150;
        } else if (eggLevel == 4) {
            result += 220;
        } else if (eggLevel == 5) {
            result += 300;
        } else if (eggLevel == 6) {
            result += 400;
        } else if (eggLevel == 7) {
            result += 520;
        } else if (eggLevel == 8) {
            result += 660;
        } else if (eggLevel == 9) {
            result += 820;
        } else if (eggLevel == 10) {
            result += 1000;
        } else if (eggLevel == 11) {
            result += 1300;
        } else if (eggLevel == 12) {
            result += 1600;
        }

        return result;
    }

    private static void updateEggExp(Player player, int nextExperience, int currentLevel) {
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            if (guardianData.hasActiveCharacter()) {
                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                EggSlot eggSlot = activeCharacter.getRpgInventory().getEggSlot();
                if (!eggSlot.isEmpty()) {
                    ItemStack egg = eggSlot.getItemOnSlot();
                    PersistentDataContainerUtil.putInteger("petExp", nextExperience, eggSlot.getItemOnSlot());

                    ItemMeta itemMeta = egg.getItemMeta();
                    List<String> lore = itemMeta.getLore();

                    int totalNextExperience = getNextExperienceTarget(currentLevel);
                    for (int i = currentLevel - 1; i > 0; i--) {
                        totalNextExperience += getNextExperienceTarget(i);
                    }

                    lore.set(5, ChatPalette.PURPLE_LIGHT + "Experience: " + ChatPalette.GRAY + nextExperience + " / " + totalNextExperience);
                    itemMeta.setLore(lore);
                    egg.setItemMeta(itemMeta);

                    eggSlot.setItemOnSlot(egg);
                }
            }
        }
    }

    private static void levelUp(Player owner, int nextLevel) {
        if (GuardianDataManager.hasGuardianData(owner)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(owner);
            if (guardianData.hasActiveCharacter()) {
                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                EggSlot eggSlot = activeCharacter.getRpgInventory().getEggSlot();
                if (!eggSlot.isEmpty()) {
                    ItemStack egg = eggSlot.getItemOnSlot();

                    ItemMeta itemMeta = egg.getItemMeta();
                    List<String> lore = itemMeta.getLore();
                    lore.set(4, ChatPalette.GOLD + "Level: " + ChatPalette.GRAY + nextLevel);

                    itemMeta.setLore(lore);

                    egg.setItemMeta(itemMeta);
                    eggSlot.setItemOnSlot(egg);

                    PetManager.respawnPet(owner);
                    MessageUtils.sendCenteredMessage(owner, ChatPalette.GOLD + "Your pet has leveled up!");
                }
            }
        }
    }

    private static int getEggExperience(Player player) {
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            if (guardianData.hasActiveCharacter()) {
                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                EggSlot eggSlot = activeCharacter.getRpgInventory().getEggSlot();
                if (!eggSlot.isEmpty()) {
                    ItemStack itemOnSlot = eggSlot.getItemOnSlot();
                    if (PersistentDataContainerUtil.hasInteger(itemOnSlot, "petExp")) {
                        return PersistentDataContainerUtil.getInteger(itemOnSlot, "petExp");
                    }
                }
            }
        }
        return -1;
    }
}
