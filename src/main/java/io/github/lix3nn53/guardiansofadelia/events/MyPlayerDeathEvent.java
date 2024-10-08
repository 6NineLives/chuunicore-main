package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.minigames.MiniGameManager;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.revive.TombManager;
import io.github.lix3nn53.guardiansofadelia.towns.Town;
import io.github.lix3nn53.guardiansofadelia.towns.TownManager;
import io.github.lix3nn53.guardiansofadelia.utilities.managers.CharacterSelectionScreenManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MyPlayerDeathEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();

        final Location deathLocation = player.getLocation();

        if (deathLocation.getY() < 1) {
            deathLocation.setY(1);
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                player.spigot().respawn();
                PetManager.despawnPet(player); // Deactivate Pet

                if (deathLocation.getWorld().getName().equals("world")) {
                    if (CharacterSelectionScreenManager.isPlayerInCharSelection(player)) {
                        player.teleport(CharacterSelectionScreenManager.getCharacterSelectionCenter());
                    } else {
                        TombManager.onDeath(player, deathLocation);
                    }
                } else if (deathLocation.getWorld().getName().equals("tutorial")) {
                    player.teleport(CharacterSelectionScreenManager.getTutorialStart());
                    PetManager.respawnPet(player);
                } else if (MiniGameManager.isInMinigame(player)) {
                    MiniGameManager.onPlayerDeath(player);
                } else {
                    Town town = TownManager.getTown(1);
                    player.teleport(town.getLocation());
                    PetManager.respawnPet(player);
                }

                // trigger death tasks for quests
                GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                if (guardianData.hasActiveCharacter()) {
                    RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                    List<Quest> questList = activeCharacter.getQuestList();
                    for (Quest quest : questList) {
                        quest.triggerDeathTasks(player);
                    }
                }
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), 5L);
    }

}
