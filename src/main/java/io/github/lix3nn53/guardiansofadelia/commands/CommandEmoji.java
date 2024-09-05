package io.github.lix3nn53.guardiansofadelia.commands;

import io.github.lix3nn53.guardiansofadelia.chat.EmojiManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEmoji implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equals("emojis")) {
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String emojiGuideMessage = EmojiManager.getEmojiGuideMessage();
            player.sendMessage("-- Emoji List --");
            player.sendMessage(emojiGuideMessage);
            // If the player (or console) uses our command correct, we can return true
            return true;
        }
        return false;
    }
}
