package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.chat.StaffRank;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.ArrayList;
import java.util.List;

public class MyBlockEvents implements Listener {

    private static final List<Player> allowBuild = new ArrayList<>();

    public static void allowBuild(Player sender, Player target) {
        if (allowBuild.contains(target)) {
            allowBuild.remove(target);
            sender.sendMessage(ChatPalette.RED + "Deny build for " + target.getName());
            target.sendMessage(ChatPalette.RED + sender.getName() + " has denied you to build.");
        } else {
            allowBuild.add(target);
            sender.sendMessage(ChatPalette.GREEN + "Allowing " + target.getName() + " to build.");
            target.sendMessage(ChatPalette.GREEN + sender.getName() + " has allowed you to build.");
        }
    }

    public static boolean canStaffRankAllowBuild(Player player) {
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);

            StaffRank staffRank = guardianData.getStaffRank();
            if (staffRank != null) {
                return staffRank.canBuild();
            }
        }

        return false;
    }

    public static boolean canBuild(Player player) {
        return allowBuild.contains(player);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(BlockBreakEvent e) {
        if (!allowBuild.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(BlockPlaceEvent e) {
        if (!allowBuild.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(BlockBurnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(BlockIgniteEvent e) {
        if (!allowBuild.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(EntityBlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(BlockFertilizeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(StructureGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(BlockGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(LeavesDecayEvent event) {
        event.setCancelled(true);
    }
}
