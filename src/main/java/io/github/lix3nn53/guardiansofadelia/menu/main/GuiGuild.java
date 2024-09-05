package io.github.lix3nn53.guardiansofadelia.menu.main;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guild.Guild;
import io.github.lix3nn53.guardiansofadelia.guild.GuildManager;
import io.github.lix3nn53.guardiansofadelia.menu.GuiHelper;
import io.github.lix3nn53.guardiansofadelia.menu.main.guild.GuiGuildMembers;
import io.github.lix3nn53.guardiansofadelia.menu.main.guild.GuiGuildRanking;
import io.github.lix3nn53.guardiansofadelia.menu.main.guild.GuiGuildUpgrades;
import io.github.lix3nn53.guardiansofadelia.menu.main.guild.GuiGuildWar;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuiGuild extends GuiGeneric {

    public GuiGuild(Player player, GuardianData guardianData) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + Translation.t(guardianData, "menu.guild.name"), 0);

        Guild guild = GuildManager.getGuild(player);

        ItemStack info = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = info.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.GREEN + "Info");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Name: " + ChatPalette.GREEN + guild.getName());
        lore.add(ChatPalette.GRAY + "Tag: " + ChatPalette.GREEN + guild.getTag());
        lore.add(ChatPalette.GRAY + "Leader: " + ChatPalette.GREEN + Bukkit.getOfflinePlayer(guild.getLeader()).getName());
        lore.add(ChatPalette.GRAY + "Members: " + ChatPalette.GREEN + guild.getMembers().size());
        itemMeta.setLore(lore);
        info.setItemMeta(itemMeta);

        ItemStack members = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.GREEN + "Members");
        lore.clear();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to view members.");
        members.setItemMeta(itemMeta);

        ItemStack rankings = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.GREEN + "Rankings");
        lore.clear();
        lore.add("");
        lore.add(ChatPalette.GRAY + "War points: " + ChatPalette.GREEN + guild.getWarPoints());
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to view rankings.");
        rankings.setItemMeta(itemMeta);

        ItemStack upgrades = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.GREEN + "Upgrades");
        lore.clear();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Hall: " + ChatPalette.GREEN + "lv " + guild.getHallLevel());
        lore.add(ChatPalette.GRAY + "Bank: " + ChatPalette.GREEN + "lv " + guild.getBankLevel());
        lore.add(ChatPalette.GRAY + "Laboratory: " + ChatPalette.GREEN + "lv " + guild.getLabLevel());
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to upgrade guild buildings.");
        upgrades.setItemMeta(itemMeta);

        ItemStack war = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.GREEN + "War");
        lore.clear();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to view or join guild war.");
        war.setItemMeta(itemMeta);

        ItemStack leave = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.GREEN + "Leave");
        lore.clear();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to leave guild.");
        leave.setItemMeta(itemMeta);

        ItemStack[] items = new ItemStack[]{info, members, rankings, upgrades, war, leave};

        if (guild.getLeader().equals(player.getUniqueId())) {
            ItemStack destroy = new ItemStack(Material.LIME_WOOL);
            itemMeta.setDisplayName(ChatPalette.GREEN + "Destroy");
            lore.clear();
            lore.add("");
            lore.add(ChatPalette.GRAY + "Click to destroy guild.");
            destroy.setItemMeta(itemMeta);

            items = new ItemStack[]{info, members, rankings, upgrades, war, leave, destroy};
        }

        GuiHelper.form54Small(this, items, "Main Menu");
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GuardianData guardianData;
        if (GuardianDataManager.hasGuardianData(player)) {
            guardianData = GuardianDataManager.getGuardianData(player);
        } else {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory.getType().equals(InventoryType.CHEST)) {
            int slot = event.getSlot();
            if (slot == 0) {
                GuiMain gui = new GuiMain(guardianData);
                gui.openInventory(player);
            } else if (GuiHelper.get54SmallButtonIndex(1) == slot) {
                GuiGuildMembers gui = new GuiGuildMembers(player, guardianData);
                gui.openInventory(player);
            } else if (GuiHelper.get54SmallButtonIndex(2) == slot) {
                new GuiGuildRanking(guardianData).openInventory(player);
            } else if (GuiHelper.get54SmallButtonIndex(3) == slot) {
                new GuiGuildUpgrades(player, guardianData).openInventory(player);
            } else if (GuiHelper.get54SmallButtonIndex(4) == slot) {
                new GuiGuildWar().openInventory(player);
            } else if (GuiHelper.get54SmallButtonIndex(5) == slot) {
                // leave
            } else if (GuiHelper.get54SmallButtonIndex(6) == slot) {
                // destroy
            }
        }
    }
}
