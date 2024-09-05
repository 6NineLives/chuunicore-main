package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.commands.admin.CommandAdmin;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MyPlayerTeleportEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(PlayerTeleportEvent event) {
        PlayerTeleportEvent.TeleportCause cause = event.getCause();

        if (cause.equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
            event.setCancelled(true);
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();

        String fromName = from.getWorld().getName();
        String toName = to.getWorld().getName();

        Player player = event.getPlayer();

        if (!fromName.equals(toName)) {

            PetManager.respawnPet(player);

            if (PetManager.hasCompanion(player)) {
                if (CommandAdmin.DEBUG_MODE) player.sendMessage("PLAYER TELEPORT COMPANION");
                PetManager.removeCompanions(player);
            }
        }

        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            if (guardianData.hasActiveCharacter()) {
                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                RPGCharacterStats rpgCharacterStats = activeCharacter.getRpgCharacterStats();
                rpgCharacterStats.reattachCosmeticHologram();
            }
        }
    }
}
