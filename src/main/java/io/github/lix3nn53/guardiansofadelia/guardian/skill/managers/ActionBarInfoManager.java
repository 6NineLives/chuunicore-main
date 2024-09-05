package io.github.lix3nn53.guardiansofadelia.guardian.skill.managers;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.ActionBarInfo;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionBarInfoManager {

    private static final HashMap<Player, BukkitTask> playerToRunner = new HashMap<>();

    private static final HashMap<Player, HashMap<Integer, ActionBarInfo>> playerToCastCounterToActionBar = new HashMap<>();

    private static final HashMap<Player, String> customMessages = new HashMap<>();
    private static final HashMap<Player, BukkitTask> customMessageRunners = new HashMap<>();

    public static void startRunner(Player player) {
        if (playerToRunner.containsKey(player)) {
            BukkitTask bukkitTask = playerToRunner.get(player);
            bukkitTask.cancel();
        }

        BukkitTask actionBarTask = new BukkitRunnable() {
            @Override
            public void run() {
                String message;

                if (customMessages.containsKey(player)) {
                    message = customMessages.get(player);
                } else {
                    message = getDefaultString(player);
                }

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            }
        }.runTaskTimerAsynchronously(GuardiansOfAdelia.getInstance(), 5L, 5L);

        playerToRunner.put(player, actionBarTask);
    }

    public static String getDefaultString(Player player) {
        RPGCharacterStats rpgCharacterStats;
        RPGClassStats rpgClassStats;
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            if (guardianData.hasActiveCharacter()) {
                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                rpgCharacterStats = activeCharacter.getRpgCharacterStats();

                rpgClassStats = activeCharacter.getRPGClassStats();
            } else {
                return "";
            }
        } else {
            return "";
        }


        String between = "                    ";

        List<ActionBarInfo> actionBarInfos = ActionBarInfoManager.getActionBarInfo(player);
        if (actionBarInfos != null) {
            StringBuilder actionBar = new StringBuilder();

            int i = 1;
            for (ActionBarInfo actionBarInfo : actionBarInfos) {
                String actionBarBetween = actionBarInfo.getActionBarBetween(player);
                actionBar.append(actionBarBetween);
                if (i < actionBarInfos.size()) {
                    actionBar.append("    ");
                }
                i++;
            }

            between = "        " + actionBar + "        ";
        }

        int totalMaxHealth = rpgCharacterStats.getTotalMaxHealth(rpgClassStats);
        int currentMana = rpgCharacterStats.getCurrentMana();
        int totalMaxMana = rpgCharacterStats.getTotalMaxMana(rpgClassStats);

        return ChatPalette.RED + "❤" + ((int) (player.getHealth() + 0.5)) + "/" + totalMaxHealth
                + between + ChatPalette.BLUE_LIGHT + "✦" + currentMana + "/" + totalMaxMana;
    }

    public static List<ActionBarInfo> getActionBarInfo(Player player) {
        HashMap<Integer, ActionBarInfo> castCounterToActionBars = playerToCastCounterToActionBar.get(player);

        if (castCounterToActionBars == null) {
            return null;
        }

        return new ArrayList<>(castCounterToActionBars.values());
    }

    public static void clearActionBarInfo(Player player) {
        playerToCastCounterToActionBar.remove(player);
    }

    public static void setActionBarInfo(Player player, ActionBarInfo actionBarInfo, int castCounter) {
        HashMap<Integer, ActionBarInfo> castCounterToActionBars = playerToCastCounterToActionBar.get(player);

        if (castCounterToActionBars == null) {
            castCounterToActionBars = new HashMap<>();
            playerToCastCounterToActionBar.put(player, castCounterToActionBars);
        }

        castCounterToActionBars.put(castCounter, actionBarInfo);
    }

    public static void removeActionBarInfo(Player player, int castCounter) {
        player.sendMessage("removeActionBarInfo: castCounter: " + castCounter);

        HashMap<Integer, ActionBarInfo> actionBarInfos = playerToCastCounterToActionBar.get(player);
        if (actionBarInfos != null) {
            actionBarInfos.remove(castCounter);
            if (actionBarInfos.isEmpty()) {
                playerToCastCounterToActionBar.remove(player);
            }
        }
    }

    public static void onQuit(Player player) {
        if (playerToRunner.containsKey(player)) {
            BukkitTask bukkitTask = playerToRunner.get(player);
            bukkitTask.cancel();
        }
    }

    public static void customMessage(Player player, String message, long duration) {
        if (customMessageRunners.containsKey(player)) {
            customMessageRunners.get(player).cancel();
        }

        customMessages.put(player, message);

        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                customMessages.remove(player);
                customMessageRunners.remove(player);
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), duration);

        customMessageRunners.put(player, bukkitTask);
    }
}
