package io.github.lix3nn53.guardiansofadelia.guild;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.economy.EconomyUtils;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.utilities.TablistUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuildHelper {

    public final static int CREATE_GUILD_COST = 128;
    private final static List<Player> guildDestroyWaitingForConfirm = new ArrayList<>();

    public static void kickPlayer(Player kicker, Player toKick) {
        if (GuildManager.inGuild(kicker)) {
            Guild guild = GuildManager.getGuild(kicker);
            if (guild.isMember(toKick.getUniqueId())) {
                PlayerRankInGuild rank = guild.getRankInGuild(kicker.getUniqueId());
                PlayerRankInGuild rankToKick = guild.getRankInGuild(toKick.getUniqueId());

                if (rank.equals(PlayerRankInGuild.LEADER) || rank.equals(PlayerRankInGuild.COMMANDER)) {
                    if (rankToKick.equals(PlayerRankInGuild.LEADER)) {
                        kicker.sendMessage(ChatPalette.RED + "You can't kick a leader from your guild");
                        return;
                    } else if (rankToKick.equals(PlayerRankInGuild.COMMANDER)) {
                        if (!rank.equals(PlayerRankInGuild.LEADER)) {
                            kicker.sendMessage(ChatPalette.RED + "Only guild leader can kick a commander");
                            return;
                        }
                    }

                    guild.removeMember(toKick.getUniqueId());
                    kicker.sendMessage(ChatPalette.RED + "You kicked " + toKick.getName() + " from guild " + guild.getName());
                    if (toKick.isOnline()) {
                        toKick.sendMessage(kicker.getName() + " kicked you from guild " + guild.getName());
                        TablistUtils.updateTablist(toKick);
                    }
                    for (UUID uuid : guild.getMembers()) {
                        Player member = Bukkit.getPlayer(uuid);
                        if (member != null) {
                            TablistUtils.updateTablist(member);
                        }
                    }
                }
            } else {
                kicker.sendMessage(ChatPalette.RED + toKick.getName() + " is not a member of your guild");
            }
        } else {
            kicker.sendMessage(ChatPalette.RED + "You are not in a guild");
        }
    }

    public static boolean giveRank(Player giver, Player toGive, PlayerRankInGuild rankToGive) {
        if (rankToGive.equals(PlayerRankInGuild.LEADER)) {
            giver.sendMessage(ChatPalette.RED + "You can't give LEADER rank to a player. Use /guild newleader instead");
            return false;
        }

        if (GuildManager.inGuild(giver)) {
            Guild guild = GuildManager.getGuild(giver);
            if (guild.isMember(toGive.getUniqueId())) {
                PlayerRankInGuild giverRank = guild.getRankInGuild(giver.getUniqueId());
                if (rankToGive.equals(PlayerRankInGuild.COMMANDER)) {
                    if (giverRank.equals(PlayerRankInGuild.LEADER)) {
                        guild.setRankInGuild(toGive.getUniqueId(), PlayerRankInGuild.COMMANDER);
                        giver.sendMessage(ChatPalette.PURPLE + toGive.getName() + "'s new guild rank is " + rankToGive);
                        toGive.sendMessage(ChatPalette.PURPLE + giver.getName() + " changed your guild rank to " + rankToGive);
                        return true;
                    } else {
                        giver.sendMessage(ChatPalette.RED + "You must be LEADER to give COMMANDER rank");
                    }
                } else if (giverRank.equals(PlayerRankInGuild.LEADER) || giverRank.equals(PlayerRankInGuild.COMMANDER)) {
                    guild.setRankInGuild(toGive.getUniqueId(), rankToGive);
                    giver.sendMessage(ChatPalette.PURPLE + toGive.getName() + "'s new guild rank is " + rankToGive);
                    toGive.sendMessage(ChatPalette.PURPLE + giver.getName() + " changed your guild rank to " + rankToGive);

                    return true;
                } else {
                    giver.sendMessage(ChatPalette.RED + "You must be LEADER or COMMANDER to change guild ranks");
                }
            } else {
                giver.sendMessage(ChatPalette.RED + "You can't give a rank to someone who is not in your guild");
            }
        }

        return false;
    }

    public static void destroy(Player player) {
        if (GuildManager.inGuild(player)) {
            Guild guild = GuildManager.getGuild(player);
            PlayerRankInGuild rank = guild.getRankInGuild(player.getUniqueId());
            if (rank.equals(PlayerRankInGuild.LEADER)) {
                player.sendMessage(ChatPalette.RED + "Are you sure? Do you want to destroy this guild permanently?");
                player.sendMessage(ChatPalette.RED + "This will destroy all items in the guild storage");
                player.sendMessage(ChatPalette.RED + "Use command '/guild comfirmdestroy' to confirm in 30 seconds");
                guildDestroyWaitingForConfirm.add(player);
                new BukkitRunnable() {


                    @Override
                    public void run() {
                        guildDestroyWaitingForConfirm.remove(player);
                        cancel();
                    }
                }.runTaskLater(GuardiansOfAdelia.getInstance(), 20L * 30L);
            } else {
                player.sendMessage(ChatPalette.RED + "You must be guild leader to destroy the guild");
            }
        }
    }

    public static void destroyConfirm(Player player) {
        if (guildDestroyWaitingForConfirm.contains(player)) {
            if (GuildManager.inGuild(player)) {
                Guild guild = GuildManager.getGuild(player);
                guild.destroy();
                for (UUID uuid : guild.getMembers()) {
                    Player member = Bukkit.getPlayer(uuid);
                    if (member != null) {
                        TablistUtils.updateTablist(member);
                        member.sendMessage(ChatPalette.RED + "Guild leader (" + player.getName() + ") destroyed the guild: " + guild.getName());
                    }
                }
                player.sendMessage(ChatPalette.RED + "Destroyed the guild: " + ChatPalette.WHITE + guild.getName());
            }
            guildDestroyWaitingForConfirm.remove(player);
        }
    }

    public static void newLeader(Player leader, Player newLeader) {
        if (GuildManager.inGuild(leader)) {
            Guild guild = GuildManager.getGuild(leader);
            if (guild.isMember(newLeader.getUniqueId())) {
                PlayerRankInGuild rank = guild.getRankInGuild(leader.getUniqueId());
                if (rank.equals(PlayerRankInGuild.LEADER)) {
                    guild.setLeader(newLeader.getUniqueId());
                } else {
                    leader.sendMessage(ChatPalette.RED + "You must be guild leader to choose a new guild leader");
                }
            }
        }
    }

    public static void setAnnouncement(Player player, String announcement) {
        if (GuildManager.inGuild(player)) {
            Guild guild = GuildManager.getGuild(player);
            PlayerRankInGuild rank = guild.getRankInGuild(player.getUniqueId());
            if (rank.canSetAnnouncement()) {
                if (announcement.length() < 420) {
                    guild.setAnnouncement(announcement);
                    player.sendMessage(ChatPalette.PURPLE + "Set guild announcement to: " + ChatPalette.WHITE + announcement);
                } else {
                    player.sendMessage(ChatPalette.RED + "Announcement must be less than 420 characters");
                }
            } else {
                StringBuilder ranks = new StringBuilder();
                for (PlayerRankInGuild rankInGuild : PlayerRankInGuild.values()) {
                    if (rankInGuild.canSetAnnouncement()) {
                        ranks.append(rankInGuild).append(", ");
                    }
                }
                player.sendMessage(ChatPalette.RED + "You must be one of these ranks to set announcement: " + ranks);
            }
        }
    }

    public static void invite(Player player, Player invited) {
        if (invited.isOnline()) {
            if (GuildManager.inGuild(player)) {
                Guild guild = GuildManager.getGuild(player);
                PlayerRankInGuild rank = guild.getRankInGuild(player.getUniqueId());
                if (rank.equals(PlayerRankInGuild.LEADER) || rank.equals(PlayerRankInGuild.COMMANDER)) {
                    String receiverMessage = ChatPalette.PURPLE + player.getName() + " invites you to " + guild.getName() + " guild";
                    String receiverTitle = ChatPalette.PURPLE + "Received guild invitation";
                    String senderTitle = ChatPalette.PURPLE + "Sent guild invitation";
                    GuildInvite invite = new GuildInvite(player, invited, senderTitle, receiverMessage, receiverTitle);
                    invite.send();
                } else {
                    StringBuilder ranks = new StringBuilder();
                    for (PlayerRankInGuild rankInGuild : PlayerRankInGuild.values()) {
                        if (rankInGuild.canInvite()) {
                            ranks.append(rankInGuild).append(", ");
                        }
                    }
                    player.sendMessage(ChatPalette.RED + "You must be one of these ranks to invite: " + ranks);
                }
            } else {
                player.sendMessage(ChatPalette.RED + "You must be in a guild to invite players");
            }
        }
    }

    public static boolean create(Player player, String name, String tag) {
        if (GuildManager.inGuild(player)) {
            player.sendMessage(ChatPalette.RED + "You are already in a guild");
            return false;
        }

        if (name.length() > 20 || name.length() < 3) {
            player.sendMessage(ChatPalette.RED + "Guild name must be between 3 and 20 characters");
            return false;
        }

        if (tag.length() > 5 || tag.length() < 2) {
            player.sendMessage(ChatPalette.RED + "Guild tag must be between 2 and 5 characters");
            return false;
        }

        if (EconomyUtils.pay(player, CREATE_GUILD_COST)) {
            Guild guild = new Guild(name, tag, player);
            player.sendMessage(ChatPalette.PURPLE + "Successfully created a new guild called " + ChatPalette.WHITE + guild.getName());
            TablistUtils.updateTablist(player);
        } else {
            player.sendMessage(ChatPalette.RED + "Creating a new guild costs 2 silvers.");
            player.sendMessage(ChatPalette.RED + "You don't have enough coins to create a guild.");
            return false;
        }

        return true;
    }

    public static void leave(Player player) {
        if (GuildManager.inGuild(player)) {
            Guild guild = GuildManager.getGuild(player);

            if (guild.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(ChatPalette.RED + "You can't leave your guild because you are the leader");
                return;
            }

            guild.removeMember(player.getUniqueId());
            player.sendMessage(ChatPalette.PURPLE + "You have left the guild");
            TablistUtils.updateTablist(player);
        } else {
            player.sendMessage(ChatPalette.RED + "You are not in a guild");
        }
    }
}
