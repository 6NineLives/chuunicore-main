package io.github.lix3nn53.guardiansofadelia.chat;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticRoom;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guild.Guild;
import io.github.lix3nn53.guardiansofadelia.guild.GuildManager;
import io.github.lix3nn53.guardiansofadelia.party.Party;
import io.github.lix3nn53.guardiansofadelia.party.PartyManager;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacter;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGuild;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterMisc;
import io.github.lix3nn53.guardiansofadelia.text.font.NegativeSpace;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatManager {

    /**
     * Create chat hologram and return boolean to allow on normal chat
     *
     * @param player
     * @param message
     */
    public static void onChat(Player player, String message) {
        ChatChannel chatChannel = null;
        ChatChannelData chatChannelData = null;

        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            chatChannelData = guardianData.getChatChannelData();
            chatChannel = chatChannelData.getTextingTo();
        }

        String displayNameForInteract = player.getDisplayName();
        TextComponent displayNameInteract = new TextComponent(displayNameForInteract);
        displayNameInteract.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/interact " + player.getName()));
        displayNameInteract.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY +
                "Click to interact with " + displayNameForInteract)));

        message = EmojiManager.replaceEmojisInMessage(message, ChatColor.GRAY);
        TextComponent suffixText = new TextComponent(getChatSuffix() + message);

        GuardiansOfAdelia.getInstance().getLogger().info(chatChannel + "-" + player.getName() + ": " + message);

        if (chatChannel == null) { //send message to normal chat
            if (!CosmeticRoom.isPlayerInRoom(player)) { // chat bubble
                final String holoMessage = CustomCharacterMisc.SPEECH_BUBBLE.toString() + ChatPalette.GRAY + message;
                Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                    SpeechBubble.player(player, holoMessage);
                });
            }

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.spigot().sendMessage(displayNameInteract, suffixText);
            }

            return;
        }

        String prefix = chatChannel.getPrefix();
        ChatPalette chatPalette = chatChannel.getPalette();
        TextComponent prefixText = new TextComponent(chatPalette + "[" + prefix + "]" + ChatColor.RESET);

        if (chatChannel.equals(ChatChannel.GUILD)) {
            Guild guild = GuildManager.getGuild(player);
            if (guild != null) {
                guild.getMembers().forEach(memberUUID -> {
                    Player member = Bukkit.getPlayer(memberUUID);
                    if (member != null) {
                        member.spigot().sendMessage(prefixText, displayNameInteract, suffixText);
                    }
                });
            } else {
                player.sendMessage(ChatColor.RED + "You are not in a guild.");
            }
        } else if (chatChannel.equals(ChatChannel.PARTY)) {
            Party party = PartyManager.getParty(player);
            if (party != null) {
                party.getMembers().forEach(member -> {
                    member.spigot().sendMessage(prefixText, displayNameInteract, suffixText);
                });
            } else {
                player.sendMessage(ChatPalette.RED + "You are not in a party.");
            }
        } else if (chatChannel.equals(ChatChannel.PRIVATE)) {
            Player privateChatTo = chatChannelData.getPrivateChatTo();
            if (privateChatTo != null) {
                if (GuardianDataManager.hasGuardianData(privateChatTo)) {
                    GuardianData guardianData = GuardianDataManager.getGuardianData(privateChatTo);
                    ChatChannelData chatChannelDataTo = guardianData.getChatChannelData();
                    boolean listening = chatChannelDataTo.isListening(ChatChannel.PRIVATE);
                    if (listening) {
                        TextComponent prefixTextFrom = new TextComponent(chatPalette + "[" + prefix + "] from" + ChatColor.RESET);
                        privateChatTo.spigot().sendMessage(prefixTextFrom, displayNameInteract, suffixText);

                        String displayNameTo = privateChatTo.getDisplayName();
                        TextComponent prefixTextTo = new TextComponent(chatPalette + "[" + prefix + "] to " + displayNameTo + ChatColor.RESET);
                        privateChatTo.spigot().sendMessage(prefixTextTo, displayNameInteract, suffixText);
                    } else {
                        player.sendMessage(ChatColor.RED + "That player is not listening to private chat.");
                    }
                }
            } else {
                player.sendMessage(ChatPalette.RED + "You are not chatting to anyone.");
            }
        } else { // GLOBAL CHANNELS
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (GuardianDataManager.hasGuardianData(onlinePlayer)) {
                    GuardianData guardianData = GuardianDataManager.getGuardianData(onlinePlayer);
                    ChatChannelData chatChannelData1 = guardianData.getChatChannelData();
                    if (chatChannelData1.isListening(chatChannel)) {
                        onlinePlayer.spigot().sendMessage(prefixText, displayNameInteract, suffixText);
                    }
                }
            }
        }
    }

    public static String getFormat(Player player) {
        // String format = "<group-prefix>%s<group-suffix>%s"; //first %s is player.getDisplayName(), second %s is the message
        String format = "%s<group-suffix>%s"; //first %s is player.getDisplayName(), second %s is the message
        // replacing your values
        // format = format.replace("<group-prefix>", getChatPrefix(player));
        format = format.replace("<group-suffix>", getChatSuffix());

        return format;
    }

    private static String getChatPrefix(Player player) {
        String prefix = "";
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            StaffRank staffRank = guardianData.getStaffRank();
            if (staffRank != null) {
                CustomCharacter customCharacter = staffRank.getCustomCharacter();
                prefix += customCharacter;
            } else if (guardianData.getPremiumRank() != null) {
                CustomCharacter customCharacter = guardianData.getPremiumRank().getCustomCharacter();
                prefix += customCharacter;
            } else if (guardianData.hasActiveCharacter()) {
                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                ChatTag chatTag = activeCharacter.getChatTag();
                CustomCharacter customCharacter = chatTag.getCustomCharacter();
                prefix += customCharacter;
            }

            if (GuildManager.inGuild(player)) {
                Guild guild = GuildManager.getGuild(player);
                String tag = guild.getTag();
                int length = tag.length();
                CustomCharacter customCharacter;
                if (length == 2) {
                    customCharacter = CustomCharacterGuild.GUILD_2;
                } else if (length == 3) {
                    customCharacter = CustomCharacterGuild.GUILD_3;
                } else if (length == 4) {
                    customCharacter = CustomCharacterGuild.GUILD_4;
                } else { // if (length == 5) {
                    customCharacter = CustomCharacterGuild.GUILD_5;
                }

                prefix += customCharacter + tag + NegativeSpace.POSITIVE_2;
            }
        }
        return prefix + ChatColor.WHITE + NegativeSpace.POSITIVE_2;
    }

    public static String getChatSuffix() {
        return ChatColor.YELLOW + " > " + ChatColor.GRAY;
    }

    public static void updatePlayerName(Player player) {
        String chatPrefix = getChatPrefix(player);

        String name = player.getName();
        player.setDisplayName(chatPrefix + name);
        player.setPlayerListName(chatPrefix + name);
    }

    public static void onPlayerJoin(Player player) {
        updatePlayerName(player);
    }
}
