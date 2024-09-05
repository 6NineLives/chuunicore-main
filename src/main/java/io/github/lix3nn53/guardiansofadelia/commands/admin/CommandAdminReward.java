package io.github.lix3nn53.guardiansofadelia.commands.admin;

import io.github.lix3nn53.guardiansofadelia.menu.admin.GuiAdminDailyRewards;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAdminReward implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equals("adminreward")) {
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage(ChatPalette.YELLOW + "/adminreward setdaily");
            } else if (args[0].equals("setdaily")) {
                GuiAdminDailyRewards gui = new GuiAdminDailyRewards();
                gui.openInventory(player);
            }

            // If the player (or console) uses our command correct, we can return true
            return true;
        }
        return false;
    }
}
