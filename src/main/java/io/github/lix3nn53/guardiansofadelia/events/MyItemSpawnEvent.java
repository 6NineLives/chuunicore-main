package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ItemTier;
import io.github.lix3nn53.guardiansofadelia.items.stats.StatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class MyItemSpawnEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEvent(ItemSpawnEvent event) {
        Item itemDrop = event.getEntity();
        ItemStack itemStack = itemDrop.getItemStack();

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasDisplayName()) {
            itemDrop.setCustomName(itemMeta.getDisplayName());
            itemDrop.setCustomNameVisible(true);

            boolean hasStatType = StatUtils.hasStatType(itemStack);
            if (hasStatType) {
                ItemTier itemTierOfItemStack = ItemTier.getItemTierOfItemStack(itemStack);

                Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

                ChatColor color = itemTierOfItemStack.getTierColorOld();
                String teamName = color.name();

                Team team = board.getTeam(teamName) != null ? board.getTeam(teamName) : board.registerNewTeam(teamName);
                team.setColor(color);

                itemDrop.setGlowing(true);

                team.addEntry(itemDrop.getUniqueId().toString());
            }
        }
    }
}
