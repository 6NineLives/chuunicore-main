package io.github.lix3nn53.guardiansofadelia.commands;

import io.github.lix3nn53.guardiansofadelia.menu.GuiPlayerInterract;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInteract implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equals("interact")) {
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage(ChatPalette.YELLOW + "/interact <player>");
            } else if (args.length == 1) {
                Player receiver = Bukkit.getPlayer(args[0]);
                if (receiver != null && receiver != sender) {
                    GuiPlayerInterract gui = new GuiPlayerInterract(player, receiver);
                    gui.openInventory(player);
                } else {
                    player.sendMessage(ChatPalette.RED + "You can't interact with yourself!");
                }
            }
            // If the player (or console) uses our command correct, we can return true
            return true;
        }
        return false;
    }
}
