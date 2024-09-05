package io.github.lix3nn53.guardiansofadelia.guardian.skill.managers;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.commands.admin.CommandAdmin;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillBar;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.TriggerComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger.*;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillRPGClassData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillTreeData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TriggerPlayerData {

    // Skillid to triggerType to trigger
    private final HashMap<Integer, HashMap<String, TriggerComponent>> map = new HashMap<>();

    public void onPlayerQuit() {
        for (HashMap<String, TriggerComponent> triggers : map.values()) {
            for (TriggerComponent trigger : triggers.values()) {
                trigger.stopEffects();
            }
        }
    }

    public void add(Player player, TriggerComponent triggerComponent, int skillId) {
        HashMap<String, TriggerComponent> triggerComponents = new HashMap<>();
        if (map.containsKey(skillId)) {
            triggerComponents = map.get(skillId);
        }
        triggerComponents.put(triggerComponent.getClass().getSimpleName(), triggerComponent);
        map.put(skillId, triggerComponents);
        if (CommandAdmin.DEBUG_MODE) player.sendMessage("ADD trigger, new size: " + map.size());
    }

    public boolean remove(Player player, String triggerType, int skillId) {
        if (map.containsKey(skillId)) {
            HashMap<String, TriggerComponent> triggers = map.get(skillId);
            triggers.remove(triggerType);
            /*if (triggers.isEmpty()) {
                map.remove(skillId);
            }*/
            if (CommandAdmin.DEBUG_MODE) player.sendMessage("REMOVE trigger, new size: " + map.size());
            return map.isEmpty();
        }

        if (CommandAdmin.DEBUG_MODE) player.sendMessage("REMOVE trigger, does not contain skillid: " + skillId);
        return false;
    }

    public boolean removeAll(String triggerType) {
        List<Integer> skillsToRemove = new ArrayList<>();

        for (int skillId : map.keySet()) {
            HashMap<String, TriggerComponent> triggers = map.get(skillId);
            triggers.remove(triggerType);
            if (triggers.isEmpty()) {
                skillsToRemove.add(skillId);
            }
        }

        for (int skillId : skillsToRemove) {
            map.remove(skillId);
        }

        return map.isEmpty();
    }

    public boolean stopAndRemoveTrigger(int skillId) {
        HashMap<String, TriggerComponent> removed = map.remove(skillId);

        if (removed != null) {
            for (TriggerComponent trigger : removed.values()) {
                trigger.stopEffects();
            }
        }

        return map.isEmpty();
    }

    public void startCooldown(Player player, int skillId, TriggerComponent trigger, int skillLevel) {
        List<Integer> cooldowns = trigger.getCooldowns();

        remove(player, trigger.getClass().getSimpleName(), skillId);

        if (!cooldowns.isEmpty()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    TriggerListener.add(player, trigger, skillId);
                }
            }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), cooldowns.get(skillLevel - 1));
        }
    }

    public void startCooldown(TriggerPlayerCooldownRecord record) {
        startCooldown(record.player(), record.skillId(), record.trigger(), record.skillLevel());
    }

    public SkillBar getSkillBar(Player player) {
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            RPGCharacter activeCharacter = guardianData.getActiveCharacter();
            return activeCharacter.getSkillBar();
        }

        return null;
    }

    public SkillTree getSkillTree(Player player) {
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            RPGCharacter activeCharacter = guardianData.getActiveCharacter();
            String rpgClassStr = activeCharacter.getRpgClassStr();
            RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);
            return rpgClass.getSkillTree();
        }

        return null;
    }

    public SkillRPGClassData getSkillRPGClassData(Player player) {
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            RPGCharacter activeCharacter = guardianData.getActiveCharacter();
            RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();
            return rpgClassStats.getSkillRPGClassData();
        }

        return null;
    }

    public void onPlayerLandGround(Player player) {
        SkillBar skillBar = getSkillBar(player);
        if (skillBar == null) return;
        SkillRPGClassData skillRPGClassData = getSkillRPGClassData(player);
        if (skillRPGClassData == null) return;
        SkillTree skillTree = getSkillTree(player);
        if (skillTree == null) return;

        for (int skillId : map.keySet()) {
            HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
            for (TriggerComponent triggerComponent : triggerComponents.values()) {
                if (!(triggerComponent instanceof LandTrigger trigger)) continue;

                int castCounter = skillBar.getCastCounter();

                SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
                int invested = skillTreeData.getInvestedSkillPoints(skillId);

                Skill skill = skillTree.getSkill(skillId);
                if (skill == null) {
                    continue;
                }
                int skillLevel = skill.getCurrentSkillLevel(invested);

                trigger.callback(player, skillLevel, castCounter);
                skillBar.increaseCastCounter();
            }
        }
    }

    public void onPlayerTookDamage(Player player, LivingEntity attacker) {
        SkillBar skillBar = getSkillBar(player);
        if (skillBar == null) return;
        SkillRPGClassData skillRPGClassData = getSkillRPGClassData(player);
        if (skillRPGClassData == null) return;
        SkillTree skillTree = getSkillTree(player);
        if (skillTree == null) return;

        List<TriggerPlayerCooldownRecord> toCooldown = new ArrayList<>();

        for (int skillId : map.keySet()) {
            HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
            for (TriggerComponent triggerComponent : triggerComponents.values()) {
                if (!(triggerComponent instanceof TookDamageTrigger trigger)) continue;

                int castCounter = skillBar.getCastCounter();

                SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
                int invested = skillTreeData.getInvestedSkillPoints(skillId);

                Skill skill = skillTree.getSkill(skillId);
                if (skill == null) {
                    continue;
                }
                int skillLevel = skill.getCurrentSkillLevel(invested);

                boolean callback = trigger.callback(player, attacker, skillLevel, castCounter);

                if (callback) {
                    toCooldown.add(new TriggerPlayerCooldownRecord(player, skillId, trigger, skillLevel));
                    skillBar.increaseCastCounter();
                }
            }
        }

        for (TriggerPlayerCooldownRecord record : toCooldown) {
            startCooldown(record);
        }
    }

    public void onPlayerNormalAttack(Player player, LivingEntity target, boolean isProjectile) {
        SkillBar skillBar = getSkillBar(player);
        if (skillBar == null) return;
        SkillRPGClassData skillRPGClassData = getSkillRPGClassData(player);
        if (skillRPGClassData == null) return;
        SkillTree skillTree = getSkillTree(player);
        if (skillTree == null) return;

        List<TriggerPlayerCooldownRecord> toCooldown = new ArrayList<>();

        for (int skillId : map.keySet()) {
            HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
            for (TriggerComponent triggerComponent : triggerComponents.values()) {
                if (!(triggerComponent instanceof NormalAttackTrigger trigger)) continue;

                int castCounter = skillBar.getCastCounter();

                SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
                int invested = skillTreeData.getInvestedSkillPoints(skillId);

                Skill skill = skillTree.getSkill(skillId);
                if (skill == null) {
                    continue;
                }
                int skillLevel = skill.getCurrentSkillLevel(invested);

                boolean callback = trigger.callback(player, target, isProjectile, skillLevel, castCounter);

                if (callback) {
                    toCooldown.add(new TriggerPlayerCooldownRecord(player, skillId, trigger, skillLevel));
                    skillBar.increaseCastCounter();
                }
            }
        }

        for (TriggerPlayerCooldownRecord record : toCooldown) {
            startCooldown(record);
        }
    }

    public void onPlayerSkillAttack(Player player, LivingEntity target) {
        SkillBar skillBar = getSkillBar(player);
        if (skillBar == null) return;
        SkillRPGClassData skillRPGClassData = getSkillRPGClassData(player);
        if (skillRPGClassData == null) return;
        SkillTree skillTree = getSkillTree(player);
        if (skillTree == null) return;

        List<TriggerPlayerCooldownRecord> toCooldown = new ArrayList<>();

        for (int skillId : map.keySet()) {
            HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
            for (TriggerComponent triggerComponent : triggerComponents.values()) {
                if (!(triggerComponent instanceof SkillAttackTrigger trigger)) continue;

                int castCounter = skillBar.getCastCounter();

                SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
                int invested = skillTreeData.getInvestedSkillPoints(skillId);

                Skill skill = skillTree.getSkill(skillId);
                if (skill == null) {
                    continue;
                }
                int skillLevel = skill.getCurrentSkillLevel(invested);

                boolean callback = trigger.callback(player, target, skillLevel, castCounter);

                if (callback) {
                    toCooldown.add(new TriggerPlayerCooldownRecord(player, skillId, trigger, skillLevel));
                    skillBar.increaseCastCounter();
                }
            }
        }

        for (TriggerPlayerCooldownRecord record : toCooldown) {
            startCooldown(record);
        }
    }

    public void onPlayerSkillCast(Player player) {
        SkillBar skillBar = getSkillBar(player);
        if (skillBar == null) return;
        SkillRPGClassData skillRPGClassData = getSkillRPGClassData(player);
        if (skillRPGClassData == null) return;
        SkillTree skillTree = getSkillTree(player);
        if (skillTree == null) return;

        List<TriggerPlayerCooldownRecord> toCooldown = new ArrayList<>();

        for (int skillId : map.keySet()) {
            HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
            for (TriggerComponent triggerComponent : triggerComponents.values()) {
                if (!(triggerComponent instanceof SkillCastTrigger trigger)) continue;

                int castCounter = skillBar.getCastCounter();

                SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
                int invested = skillTreeData.getInvestedSkillPoints(skillId);

                Skill skill = skillTree.getSkill(skillId);
                if (skill == null) {
                    continue;
                }
                int skillLevel = skill.getCurrentSkillLevel(invested);

                boolean callback = trigger.callback(player, skillLevel, castCounter);

                if (callback) {
                    toCooldown.add(new TriggerPlayerCooldownRecord(player, skillId, trigger, skillLevel));
                    skillBar.increaseCastCounter();
                }
            }
        }

        for (TriggerPlayerCooldownRecord record : toCooldown) {
            startCooldown(record);
        }
    }

    public void onPlayerShootCrossbow(Player player, Arrow arrow) {
        SkillBar skillBar = getSkillBar(player);
        if (skillBar == null) return;
        SkillRPGClassData skillRPGClassData = getSkillRPGClassData(player);
        if (skillRPGClassData == null) return;
        SkillTree skillTree = getSkillTree(player);
        if (skillTree == null) return;

        for (int skillId : map.keySet()) {
            HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
            for (TriggerComponent triggerComponent : triggerComponents.values()) {
                if (!(triggerComponent instanceof AddPiercingToArrowShootFromCrossbowTrigger trigger)) continue;

                SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
                int invested = skillTreeData.getInvestedSkillPoints(skillId);

                Skill skill = skillTree.getSkill(skillId);
                if (skill == null) {
                    continue;
                }
                int skillLevel = skill.getCurrentSkillLevel(invested);

                trigger.callback(arrow, skillLevel);
            }
        }
    }

    public void onPlayerSavedEntitySpawn(Player player, LivingEntity created) {
        SkillBar skillBar = getSkillBar(player);
        if (skillBar == null) return;
        SkillRPGClassData skillRPGClassData = getSkillRPGClassData(player);
        if (skillRPGClassData == null) return;
        SkillTree skillTree = getSkillTree(player);
        if (skillTree == null) return;

        List<TriggerPlayerCooldownRecord> toCooldown = new ArrayList<>();

        for (int skillId : map.keySet()) {
            HashMap<String, TriggerComponent> triggerComponents = map.get(skillId);
            for (TriggerComponent triggerComponent : triggerComponents.values()) {
                if (!(triggerComponent instanceof SavedEntitySpawnTrigger trigger)) continue;

                int castCounter = skillBar.getCastCounter();

                SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
                int invested = skillTreeData.getInvestedSkillPoints(skillId);

                Skill skill = skillTree.getSkill(skillId);
                if (skill == null) {
                    continue;
                }
                int skillLevel = skill.getCurrentSkillLevel(invested);

                boolean callback = trigger.callback(player, created, skillLevel, castCounter);

                if (callback) {
                    toCooldown.add(new TriggerPlayerCooldownRecord(player, skillId, trigger, skillLevel));
                    skillBar.increaseCastCounter();
                }
            }
        }

        for (TriggerPlayerCooldownRecord record : toCooldown) {
            startCooldown(record);
        }
    }
}
