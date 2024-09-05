package io.github.lix3nn53.guardiansofadelia.menu;

import io.github.lix3nn53.guardiansofadelia.economy.trading.TradeInvite;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guild.Guild;
import io.github.lix3nn53.guardiansofadelia.guild.GuildInvite;
import io.github.lix3nn53.guardiansofadelia.guild.GuildManager;
import io.github.lix3nn53.guardiansofadelia.guild.PlayerRankInGuild;
import io.github.lix3nn53.guardiansofadelia.party.PartyInvite;
import io.github.lix3nn53.guardiansofadelia.rpginventory.slots.CharacterInfoSlot;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import io.github.lix3nn53.guardiansofadelia.utilities.invite.FriendInvite;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuiPlayerInterract extends GuiGeneric {

    private final Player rightClicked;

    public GuiPlayerInterract(Player clicker, Player rightClicked) {
        super(27, ChatPalette.GRAY_DARK + "Interact with " + rightClicked.getName(), 0);
        this.rightClicked = rightClicked;

        GuardianData viewerData;
        if (GuardianDataManager.hasGuardianData(clicker)) {
            viewerData = GuardianDataManager.getGuardianData(clicker);
        } else {
            return;
        }

        ItemStack infoItem = new CharacterInfoSlot(rightClicked).getItem(viewerData);
        this.setItem(9, infoItem);

        ItemStack party = new ItemStack(Material.LIGHT_BLUE_WOOL);
        ItemMeta itemMeta = party.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.BLUE_LIGHT + "Party Invite");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to invite to party!");
        itemMeta.setLore(lore);
        party.setItemMeta(itemMeta);
        this.setItem(15, party);

        ItemStack guild = new ItemStack(Material.PURPLE_WOOL);
        itemMeta.setDisplayName(ChatPalette.PURPLE + "Guild Invite");
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to invite to guild!");
        itemMeta.setLore(lore);
        guild.setItemMeta(itemMeta);
        this.setItem(13, guild);

        ItemStack trade = new ItemStack(Material.YELLOW_WOOL);
        itemMeta.setDisplayName(ChatPalette.YELLOW + "Trade Invite");
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to invite to trade!");
        itemMeta.setLore(lore);
        trade.setItemMeta(itemMeta);
        this.setItem(17, trade);

        ItemStack friend = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.GREEN_LIGHT + "Friend Invite");
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to send friend request!");
        itemMeta.setLore(lore);
        friend.setItemMeta(itemMeta);
        this.setItem(11, friend);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory.getType().equals(InventoryType.CHEST)) {
            if (rightClicked == null) return;

            if (rightClicked.isOnline()) {
                int slot = event.getSlot();
                if (slot == 15) {
                    String senderTitle = ChatPalette.BLUE_LIGHT + "Sent party request";
                    String receiverMessage = ChatPalette.BLUE_LIGHT + player.getName() + " invites you to party"; //sender = player
                    String receiverTitle = ChatPalette.BLUE_LIGHT + "Received party invite";
                    PartyInvite partyInvite = new PartyInvite(player, rightClicked, senderTitle, receiverMessage, receiverTitle); //receiver = rightClicked
                    partyInvite.send();
                    player.closeInventory();
                } else if (slot == 13) {
                    if (GuildManager.inGuild(player)) {
                        Guild guild = GuildManager.getGuild(player);
                        PlayerRankInGuild rank = guild.getRankInGuild(player.getUniqueId());
                        if (rank.equals(PlayerRankInGuild.LEADER) || rank.equals(PlayerRankInGuild.COMMANDER)) {
                            String receiverMessage = ChatPalette.PURPLE + player.getName() + " invites you to " + guild.getName() + " guild"; //sender = player
                            String receiverTitle = ChatPalette.PURPLE + "Received guild invite";
                            String senderTitle = ChatPalette.PURPLE + "Sent guild request";
                            GuildInvite invite = new GuildInvite(player, rightClicked, senderTitle, receiverMessage, receiverTitle); //receiver = rightClicked
                            invite.send();
                        } else {
                            player.sendMessage(ChatPalette.RED + "You must be guild leader or commander to invite players to guild");
                        }
                    }
                    player.closeInventory();
                } else if (slot == 17) {
                    String senderTitle = ChatPalette.GOLD + "Sent trade request";
                    String receiverMessage = ChatPalette.GOLD + player.getName() + " invites you to trade"; //sender = player
                    String receiverTitle = ChatPalette.GOLD + "Received trade request";
                    TradeInvite tradeInvite = new TradeInvite(player, rightClicked, senderTitle, receiverMessage, receiverTitle); //receiver = rightClicked
                    tradeInvite.send();
                    player.closeInventory();
                } else if (slot == 11) {
                    String senderTitle = ChatPalette.GREEN_LIGHT + "Sent friend request";
                    String receiverMessage = ChatPalette.GREEN_LIGHT + player.getName() + " sent you a friend request"; //sender = player
                    String receiverTitle = ChatPalette.GREEN_LIGHT + "Received friend request";
                    FriendInvite friendInvite = new FriendInvite(player, rightClicked, senderTitle, receiverMessage, receiverTitle); //receiver = rightClicked
                    friendInvite.send();
                    player.closeInventory();
                }
            }
        }
    }
}
