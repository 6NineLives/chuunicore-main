package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticRoom;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class MyEntityPortalEnterEvent implements Listener {

    private static final Set<Player> playersExitingCosmeticRoom = new HashSet<>();

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPortalEnter(EntityPortalEnterEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player player) {
            if (CosmeticRoom.isPlayerInRoom(player)) {
                boolean add = playersExitingCosmeticRoom.add(player);

                if (add) {
                    player.sendMessage(ChatPalette.PURPLE_LIGHT + "Leaving cosmetic room...");

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            if (playersExitingCosmeticRoom.contains(player)) {
                                CosmeticRoom.leaveRoom(player);
                                playersExitingCosmeticRoom.remove(player);
                            }
                        }
                    }.runTaskLater(GuardiansOfAdelia.getInstance(), 40L);
                }
            }
        }
    }
}
