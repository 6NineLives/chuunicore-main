package io.github.lix3nn53.guardiansofadelia.commands;

import io.github.lix3nn53.guardiansofadelia.events.MyBlockEvents;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBuild implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equals("build")) {
            return false;
        }
        if (sender instanceof Player player) {
            if (!MyBlockEvents.canStaffRankAllowBuild(player)) {
                player.sendMessage(ChatPalette.RED + "You don't have permission to use this command.");
                return true;
            }

            if (args.length == 0) {
                MyBlockEvents.allowBuild(player, player);
            } else if (args[0].equals("help")) {
                player.sendMessage(ChatPalette.YELLOW + "/build - toggle self build mode");
                player.sendMessage(ChatPalette.YELLOW + "/build <player> - toggle target's build mode");
            } else {
                Player target = player.getServer().getPlayer(args[0]);
                if (target != null) {
                    MyBlockEvents.allowBuild(player, target);
                } else {
                    player.sendMessage(ChatPalette.YELLOW + "Player not found.");
                }
            }
            // If the player (or console) uses our command correct, we can return true
            return true;
        }
        return false;
    }
}
