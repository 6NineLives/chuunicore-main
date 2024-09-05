package io.github.lix3nn53.guardiansofadelia.guardian.skill;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticRoom;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.statuseffect.StatusEffectManager;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger.InitializeTrigger;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.managers.TriggerListener;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillBarData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillRPGClassData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillTreeData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import io.github.lix3nn53.guardiansofadelia.items.list.OtherItems;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Each player character has unique skill-bar
 */
public class SkillBar {

    private final HashMap<Integer, BukkitTask> slotsOnCooldown = new HashMap<>();
    private final Player player;

    private int castCounter = 0;

    public SkillBar(Player player, SkillTree skillTree, SkillRPGClassData skillRPGClassData, boolean remake) {
        this.player = player;

        player.getInventory().setHeldItemSlot(4);

        if (remake) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            remakeSkillBar(skillTree, skillRPGClassData, guardianData.getLanguage());
        }

        //activate init triggers
        new BukkitRunnable() {

            final SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
            final Set<Integer> skillIds = skillTreeData.getSkillIds();

            @Override
            public void run() {
                for (int skillId : skillIds) {
                    if (skillId < 0) continue;
                    Skill skill = skillTree.getSkill(skillId);
                    int investedPoints = skillTreeData.getInvestedSkillPoints(skillId);

                    if (investedPoints > 0) {
                        List<InitializeTrigger> initializeTriggers = skill.getInitializeTriggers();
                        for (InitializeTrigger initializeTrigger : initializeTriggers) {
                            int currentSkillLevel = skill.getCurrentSkillLevel(investedPoints);
                            TriggerListener.onSkillUpgrade(player, initializeTrigger, skillId, currentSkillLevel, castCounter);
                            castCounter++;
                        }
                    }
                }
            }
        }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), 10L);
    }

    public void remakeSkillBar(SkillTree skillTree, SkillRPGClassData skillRPGClassData, String lang) {
        SkillBarData skillBarData = skillRPGClassData.getSkillBarData();
        SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
        for (int slotIndex = 0; slotIndex < 4; slotIndex++) {
            int skillId = skillBarData.getSkillId(slotIndex);
            if (skillId < 0) {
                player.getInventory().setItem(slotIndex, OtherItems.getUnassignedSkill());
                continue;
            }

            int investedPoints = skillTreeData.getInvestedSkillPoints(skillId);

            if (investedPoints > 0) {
                Skill skill = skillTree.getSkill(skillId);

                int skillPointsLeftToSpend = skillRPGClassData.getSkillPointsLeftToSpend(player);

                ItemStack icon = skill.getIcon(lang, skillPointsLeftToSpend, investedPoints);
                player.getInventory().setItem(slotIndex, icon);
            } else {
                player.getInventory().setItem(slotIndex, OtherItems.getUnassignedSkill());
            }
        }
    }

    public void remakeSkillBarIcon(int slotIndex, SkillTree skillTree, SkillRPGClassData skillRPGClassData, String lang) {
        SkillBarData skillBarData = skillRPGClassData.getSkillBarData();
        int skillId = skillBarData.getSkillId(slotIndex);
        if (slotIndex < 0) {
            return;
        }

        if (skillId < 0) {
            player.getInventory().setItem(slotIndex, OtherItems.getUnassignedSkill());
            return;
        }

        SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
        int invested = skillTreeData.getInvestedSkillPoints(skillId);

        if (invested > 0) {
            Skill skill = skillTree.getSkill(skillId);

            int skillPointsLeftToSpend = skillRPGClassData.getSkillPointsLeftToSpend(player);
            ItemStack icon = skill.getIcon(lang, skillPointsLeftToSpend, invested);
            player.getInventory().setItem(slotIndex, icon);
        } else {
            player.getInventory().setItem(slotIndex, OtherItems.getUnassignedSkill());
        }
    }

    public static float abilityHasteToMultiplier(float abilityHaste) {
        return 100 / (100 + abilityHaste);
    }

    public void reloadSkillSet(SkillTree skillTree, SkillRPGClassData skillRPGClassData, String lang) {
        remakeSkillBar(skillTree, skillRPGClassData, lang);
    }

    public boolean castSkill(GuardianData guardianData, int slot, SkillTree skillTree, SkillRPGClassData skillRPGClassData) {
        if (StatusEffectManager.isSilenced(player)) {
            return false;
        }

        if (CosmeticRoom.isPlayerInRoom(player)) {
            return false;
        }

        if (slotsOnCooldown.containsKey(slot)) {
            player.sendMessage(ChatPalette.RED + Translation.t(guardianData, "skill.cooldown"));
            return false;
        }

        SkillBarData skillBarData = skillRPGClassData.getSkillBarData();
        int skillId = skillBarData.getSkillId(slot);

        SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
        int invested = skillTreeData.getInvestedSkillPoints(skillId);

        Skill skill = skillTree.getSkill(skillId);
        if (skill == null) {
            return false;
        }
        int skillLevel = skill.getCurrentSkillLevel(invested);

        if (skillLevel <= 0) {
            player.sendMessage(ChatPalette.RED + Translation.t(guardianData, "skill.unlearned"));
            return false;
        }

        int manaCost = skill.getManaCost(skillLevel);
        RPGCharacter activeCharacter = guardianData.getActiveCharacter();
        RPGCharacterStats rpgCharacterStats = activeCharacter.getRpgCharacterStats();
        int currentMana = rpgCharacterStats.getCurrentMana();
        if (currentMana < manaCost) {
            player.sendMessage(ChatPalette.RED + Translation.t(guardianData, "skill.nomana"));
            return false;
        }

        boolean cast = skill.cast(player, skillLevel, new ArrayList<>(), castCounter, skillId);//cast ends when this returns

        if (!cast) {
            player.sendMessage(ChatPalette.RED + Translation.t(guardianData, "skill.fail"));
            return false; //dont go on cooldown and consume mana if cast failed
        }

        castCounter++;
        TriggerListener.onPlayerSkillCast(player);

        // mana cost
        RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();
        rpgCharacterStats.consumeMana(manaCost, rpgClassStats);

        float abilityHaste = rpgCharacterStats.getTotalAbilityHaste();

        int cooldownInTicks = (int) (((skill.getCooldown(skillLevel) * 20) * abilityHasteToMultiplier(abilityHaste)) + 0.5); // Ability haste formula from League of Legends
        PlayerInventory inventory = player.getInventory();

        BukkitTask cooldownTask = new BukkitRunnable() {

            int ticksPassed = 0;

            @Override
            public void run() {
                if (ticksPassed >= cooldownInTicks) {
                    cancel();
                    slotsOnCooldown.remove(slot);
                } else {
                    int cooldownLeft = cooldownInTicks - ticksPassed;
                    int secondsLeft = cooldownLeft / 20;
                    float modulus = cooldownLeft % 20;

                    if (modulus > 0) {
                        secondsLeft++;
                    }

                    ItemStack item = inventory.getItem(slot);
                    int currentAmount = item.getAmount();
                    if (currentAmount != secondsLeft) {
                        if (InventoryUtils.isAirOrNull(item)) {
                            remakeSkillBarIcon(slot, skillTree, skillRPGClassData, guardianData.getLanguage());
                            item = inventory.getItem(slot);
                        }
                        item.setAmount(secondsLeft);
                        player.updateInventory();
                    }
                }

                ticksPassed += 5;
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 0L, 5L);
        slotsOnCooldown.put(slot, cooldownTask);

        return true;
    }

    public void onQuit() {
        for (BukkitTask task : slotsOnCooldown.values()) {
            task.cancel();
        }

        slotsOnCooldown.clear();
    }

    public int getCastCounter() {
        return castCounter;
    }

    public void increaseCastCounter() {
        this.castCounter += 1;
    }
}
