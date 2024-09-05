package io.github.lix3nn53.guardiansofadelia.quests.actions;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.chat.ChatTag;
import io.github.lix3nn53.guardiansofadelia.database.DatabaseManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.*;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillBar;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import io.github.lix3nn53.guardiansofadelia.npc.QuestNPCManager;
import io.github.lix3nn53.guardiansofadelia.rpginventory.RPGInventory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class TutorialEndAction implements Action {

    @Override
    public void perform(Player player, int questID, int taskIndex) {
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);

            if (guardianData.hasActiveCharacter()) {
                String lang = guardianData.getLanguage();

                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                RPGCharacterStats rpgCharacterStats = activeCharacter.getRpgCharacterStats();
                SkillBar skillBar = activeCharacter.getSkillBar();

                RPGInventory rpgInventory = activeCharacter.getRpgInventory();
                rpgInventory.clearInventory(player);

                player.getInventory().clear();

                String rpgClassStr = activeCharacter.getRpgClassStr();
                RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);
                SkillTree skillTree = rpgClass.getSkillTree();
                RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();
                rpgClassStats.resetAll(rpgCharacterStats, player, skillTree, skillBar, lang);

                activeCharacter.setChatTag(player, ChatTag.NOVICE);

                rpgCharacterStats.setTotalExp(0);

                rpgCharacterStats.setCurrentHealth(rpgCharacterStats.getTotalMaxHealth(rpgClassStats));
                rpgCharacterStats.setCurrentMana(rpgCharacterStats.getTotalMaxMana(rpgClassStats), rpgClassStats);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.removePotionEffect(PotionEffectType.WITHER);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 4));
                        QuestNPCManager.setAllNpcHologramForPlayer(player);
                        activeCharacter.getRpgCharacterStats().recalculateEquipment(rpgClassStr, rpgClassStats);
                        activeCharacter.getRpgCharacterStats().recalculateRPGInventory(rpgInventory, rpgClassStats);
                    }
                }.runTaskLater(GuardiansOfAdelia.getInstance(), 5L);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        DatabaseManager.writeGuardianDataWithCurrentCharacter(player, guardianData);
                    }
                }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), 40L);
            }
        }
    }

    @Override
    public boolean preventTaskCompilation() {
        return false;
    }
}
