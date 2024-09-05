package io.github.lix3nn53.guardiansofadelia.guardian.skill.managers;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.TriggerComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger.InitializeTrigger;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger.LandTrigger;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TriggerListener {
    // Every skill can have only one trigger of a type

    // Player to skillid to triggerType to trigger
    private static final HashMap<Player, TriggerPlayerData> playerToData = new HashMap<>();

    public static void onPlayerQuit(Player player) {
        if (playerToData.containsKey(player)) {
            TriggerPlayerData triggerPlayerData = playerToData.get(player);

            triggerPlayerData.onPlayerQuit();

            playerToData.remove(player);
        }
    }

    public static void add(Player player, TriggerComponent triggerComponent, int skillId) {
        TriggerPlayerData triggerPlayerData = new TriggerPlayerData();
        if (playerToData.containsKey(player)) {
            triggerPlayerData = playerToData.get(player);
        }
        triggerPlayerData.add(player, triggerComponent, skillId);
        playerToData.put(player, triggerPlayerData);
    }

    public static void remove(Player player, String triggerType, int skillId) {
        if (playerToData.containsKey(player)) {
            TriggerPlayerData triggerPlayerData = playerToData.get(player);

            boolean remove = triggerPlayerData.remove(player, triggerType, skillId);
            if (remove) {
                playerToData.remove(player);
            }
        }
    }

    public static void onPlayerLandGround(Player player) {
        if (playerToData.containsKey(player)) {
            TriggerPlayerData triggerPlayerData = playerToData.get(player);

            triggerPlayerData.onPlayerLandGround(player);
            boolean b = triggerPlayerData.removeAll(LandTrigger.class.getSimpleName());
            if (b) {
                playerToData.remove(player);
            }
        }
    }

    public static void onPlayerTookDamage(Player player, LivingEntity attacker) {
        if (playerToData.containsKey(player)) {
            TriggerPlayerData triggerPlayerData = playerToData.get(player);

            triggerPlayerData.onPlayerTookDamage(player, attacker);
        }
    }

    public static void onPlayerNormalAttack(Player player, LivingEntity target, boolean isProjectile) {
        if (playerToData.containsKey(player)) {
            TriggerPlayerData triggerPlayerData = playerToData.get(player);

            triggerPlayerData.onPlayerNormalAttack(player, target, isProjectile);
        }
    }

    public static void onPlayerSkillAttack(Player player, LivingEntity target) {
        if (playerToData.containsKey(player)) {
            TriggerPlayerData triggerPlayerData = playerToData.get(player);

            triggerPlayerData.onPlayerSkillAttack(player, target);
        }
    }

    public static void onPlayerSkillCast(Player player) {
        if (playerToData.containsKey(player)) {
            TriggerPlayerData triggerPlayerData = playerToData.get(player);

            triggerPlayerData.onPlayerSkillCast(player);
        }
    }

    public static void onPlayerShootCrossbow(Player player, Arrow arrow) {
        if (playerToData.containsKey(player)) {
            TriggerPlayerData triggerPlayerData = playerToData.get(player);

            triggerPlayerData.onPlayerShootCrossbow(player, arrow);
        }
    }

    public static void onPlayerSavedEntitySpawn(Player player, LivingEntity created) {
        if (playerToData.containsKey(player)) {
            TriggerPlayerData triggerPlayerData = playerToData.get(player);

            triggerPlayerData.onPlayerSavedEntitySpawn(player, created);
        }
    }

    public static void onSkillUpgrade(Player player, InitializeTrigger initializeTrigger, int skillId, int nextSkillLevel, int castCounter) {
        stopAndRemoveTrigger(player, skillId); //stop old init
        initializeTrigger.stopPreviousEffects(player);

        List<LivingEntity> targets = new ArrayList<>();
        targets.add(player);

        initializeTrigger.startEffects(player, nextSkillLevel, targets, castCounter, skillId);
    }

    public static void onSkillDowngrade(Player player, InitializeTrigger initializeTrigger, int skillId, int nextSkillLevel, int castCounter) {
        stopAndRemoveTrigger(player, skillId);
        initializeTrigger.stopPreviousEffects(player);
        if (nextSkillLevel == 0) return;

        List<LivingEntity> targets = new ArrayList<>();
        targets.add(player);

        initializeTrigger.startEffects(player, nextSkillLevel, targets, castCounter, skillId);
    }

    private static void stopAndRemoveTrigger(Player player, int skillId) {
        if (playerToData.containsKey(player)) {
            TriggerPlayerData triggerPlayerData = playerToData.get(player);

            boolean b = triggerPlayerData.stopAndRemoveTrigger(skillId);
            if (b) {
                playerToData.remove(player);
            }
        }
    }
}
