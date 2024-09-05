package io.github.lix3nn53.guardiansofadelia.commands;

import io.github.lix3nn53.guardiansofadelia.chat.ChatChannel;
import io.github.lix3nn53.guardiansofadelia.chat.ChatChannelData;
import io.github.lix3nn53.guardiansofadelia.chat.ChatManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPrivateMessage implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equals("pm")) {
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage(ChatPalette.YELLOW + "/pm <player> - set active chat channel to private message with <player>");
                player.sendMessage(ChatPalette.YELLOW + "/pm <player> <message> - send a single private message");
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(ChatPalette.RED + "Player not found.");
                    return true;
                } else {
                    if (GuardianDataManager.hasGuardianData(player)) {
                        GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                        ChatChannelData chatChannelData = guardianData.getChatChannelData();
                        chatChannelData.setPrivateChatTo(target);
                        chatChannelData.setTextingTo(ChatChannel.PRIVATE);
                    }
                }
            } else {
                Player privateChatTo = Bukkit.getPlayer(args[0]);
                if (privateChatTo == null) {
                    player.sendMessage(ChatPalette.RED + "Player not found.");
                    return true;
                } else {
                    if (GuardianDataManager.hasGuardianData(privateChatTo)) {
                        GuardianData guardianData = GuardianDataManager.getGuardianData(privateChatTo);
                        ChatChannelData chatChannelDataTo = guardianData.getChatChannelData();
                        boolean listening = chatChannelDataTo.isListening(ChatChannel.PRIVATE);
                        if (listening) {
                            ChatChannel chatChannel = ChatChannel.PRIVATE;
                            String prefix = chatChannel.getPrefix();
                            ChatPalette chatPalette = chatChannel.getPalette();
                            String displayName = player.getDisplayName();

                            StringBuilder message = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                message.append(args[i]).append(" ");
                            }
                            String messageWithPrefix = chatPalette + "[" + prefix + "]" + ChatColor.RESET + displayName + ChatManager.getChatSuffix() + message;

                            privateChatTo.sendMessage(messageWithPrefix);
                            player.sendMessage(messageWithPrefix);
                        } else {
                            player.sendMessage(ChatColor.RED + "That player is not listening to private chat.");
                        }
                    }
                }
            }
            // If the player (or console) uses our command correct, we can return true
            return true;
        }
        return false;
    }
}
