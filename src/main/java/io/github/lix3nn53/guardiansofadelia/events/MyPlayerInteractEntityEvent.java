package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.menu.GuiPlayerInterract;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MyPlayerInteractEntityEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onEvent(PlayerInteractEntityEvent event) {
        event.setCancelled(true);

        if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            event.setCancelled(true);
            return;
        } else if (!event.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }

        Entity rightClicked = event.getRightClicked();
        Player player = event.getPlayer();

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (itemInMainHand.hasItemMeta()) {
            ItemMeta itemMeta = itemInMainHand.getItemMeta();
            if (itemMeta.hasDisplayName()) {
                if (rightClicked.isCustomNameVisible()) {
                    if (GuardianDataManager.hasGuardianData(player)) {
                        GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                        if (guardianData.hasActiveCharacter()) {
                            RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                            List<Quest> questList = activeCharacter.getQuestList();
                            for (Quest quest : questList) {
                                quest.progressGiftTasks(player, itemMeta.getDisplayName(), itemInMainHand.getAmount(), rightClicked.getCustomName());
                            }
                        }
                    }
                }
            }
        }

        if (rightClicked instanceof Player) {
            if (player.isSneaking()) {
                Player rightClickedPlayer = (Player) rightClicked;

                NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
                if (npcRegistry.isNPC(rightClickedPlayer)) return;

                GuiPlayerInterract gui = new GuiPlayerInterract(player, rightClickedPlayer);
                gui.openInventory(player);
            }
        }
    }
}
