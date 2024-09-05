package io.github.lix3nn53.guardiansofadelia.commands;

import io.github.lix3nn53.guardiansofadelia.guild.Guild;
import io.github.lix3nn53.guardiansofadelia.guild.GuildHelper;
import io.github.lix3nn53.guardiansofadelia.guild.GuildManager;
import io.github.lix3nn53.guardiansofadelia.guild.PlayerRankInGuild;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CommandGuild implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("guild")) {
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage(ChatPalette.PURPLE + "/guild create <guildName> <guildTag> - create a new guild");
                player.sendMessage(ChatPalette.PURPLE + "/guild top - 10 guild with highest war-point");
                player.sendMessage(ChatPalette.PURPLE + "Guild member commands");
                player.sendMessage(ChatPalette.PURPLE + "/guild leave - leave your current guild");
                player.sendMessage(ChatPalette.PURPLE + "/guild members - list members of your guild");
                player.sendMessage(ChatPalette.PURPLE + "/guild ann - view guild announcement");
                player.sendMessage(ChatPalette.PURPLE + "Guild commander commands");
                player.sendMessage(ChatPalette.PURPLE + "/guild invite <player> - invite a player to your guild");
                player.sendMessage(ChatPalette.PURPLE + "/guild kick <player> - kick a player from your guild");
                player.sendMessage(ChatPalette.PURPLE + "/guild ranklist - view rank list");
                player.sendMessage(ChatPalette.PURPLE + "/guild rank <player> <rank> - set a player's guild rank");
                player.sendMessage(ChatPalette.PURPLE + "/guild setann <sentence> - set guild's announcement");
                player.sendMessage(ChatPalette.PURPLE + "Guild leader commands");
                player.sendMessage(ChatPalette.PURPLE + "/guild newleader <player> - set new guild leader");
                player.sendMessage(ChatPalette.PURPLE + "/guild destroy - destroy the guild");
            } else if (args[0].equals("create")) {
                if (args.length == 3) {
                    String name = args[1];
                    String tag = args[2];

                    GuildHelper.create(player, name, tag);
                }
            } else if (args[0].equals("top")) {
                GuildManager.printTop10(player);
            } else if (args[0].equals("leave")) {
                GuildHelper.leave(player);
            } else if (args[0].equals("members")) {
                if (GuildManager.inGuild(player)) {
                    Guild guild = GuildManager.getGuild(player);
                    HashMap<UUID, PlayerRankInGuild> members = guild.getMembersWithRanks();
                    player.sendMessage(ChatPalette.PURPLE + "GuildMember  -  Rank");
                    for (UUID member : members.keySet()) {
                        OfflinePlayer memberPlayer = Bukkit.getOfflinePlayer(member);
                        PlayerRankInGuild playerRankInGuild = members.get(member);
                        player.sendMessage(memberPlayer.getName() + "  -  " + playerRankInGuild.toString());
                    }
                }
            } else if (args[0].equals("ann")) {
                if (GuildManager.inGuild(player)) {
                    Guild guild = GuildManager.getGuild(player);
                    String announcement = guild.getAnnouncement();
                    player.sendMessage(ChatPalette.PURPLE + "Guild announcement: " + ChatPalette.WHITE + announcement);
                }
            } else if (args[0].equals("invite")) {
                if (args.length == 2) {
                    Player player2 = Bukkit.getPlayer(args[1]);
                    if (player2 != null && player != player2) {
                        GuildHelper.invite(player, player2);
                    } else {
                        player.sendMessage(ChatPalette.RED + "You can't invite yourself!");
                    }
                }
            } else if (args[0].equals("kick")) {
                if (args.length == 2) {
                    if (!args[1].equalsIgnoreCase(player.getName())) {
                        Player player2 = Bukkit.getPlayer(args[1]);
                        if (player2 != null) {
                            GuildHelper.kickPlayer(player, player2);
                        } else {
                            player.sendMessage(ChatPalette.RED + "Player not found");
                        }
                    } else {
                        player.sendMessage(ChatPalette.RED + "You can't kick yourself");
                    }
                }
            } else if (args[0].equals("ranklist")) {
                player.sendMessage(ChatPalette.PURPLE + "Ranks in guild: 1-Leader, 2-Commander, 3-Sergeant, 4-Corporal, 5-Soldier");
            } else if (args[0].equals("rank")) {
                if (args.length == 3) {
                    if (!args[1].equalsIgnoreCase(player.getName())) {
                        Player player2 = Bukkit.getPlayer(args[1]);
                        if (player2 != null) {
                            GuildHelper.giveRank(player, player2, PlayerRankInGuild.valueOf(args[2].toUpperCase()));
                        } else {
                            player.sendMessage(ChatPalette.RED + "Player not found");
                        }
                    } else {
                        player.sendMessage(ChatPalette.RED + "You can't change your own rank");
                    }
                }
            } else if (args[0].equals("setann")) {
                if (args.length >= 2) {
                    String announcement = "";
                    for (int i = 1; i < args.length; i++) {
                        announcement += args[i];
                    }

                    GuildHelper.setAnnouncement(player, announcement);
                }
            } else if (args[0].equals("newleader")) {
                if (args.length >= 2) {
                    if (!args[1].equalsIgnoreCase(player.getName())) {
                        Player player2 = Bukkit.getPlayer(args[1]);
                        if (player2 != null) {
                            GuildHelper.newLeader(player, player2);
                        } else {
                            player.sendMessage(ChatPalette.RED + "Player not found");
                        }
                    } else {
                        player.sendMessage(ChatPalette.RED + "You can't change your own rank");
                    }
                }
            } else if (args[0].equals("destroy")) {
                GuildHelper.destroy(player);
            } else if (args[0].equals("confirmdestroy")) {
                GuildHelper.destroyConfirm(player);
            }
        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
