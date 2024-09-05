package io.github.lix3nn53.guardiansofadelia.menu.main;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.economy.EconomyUtils;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guild.GuildHelper;
import io.github.lix3nn53.guardiansofadelia.menu.ActiveGuiManager;
import io.github.lix3nn53.guardiansofadelia.menu.GuiHelper;
import io.github.lix3nn53.guardiansofadelia.menu.main.guild.GuiGuildRanking;
import io.github.lix3nn53.guardiansofadelia.menu.main.guild.GuiGuildWar;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import io.github.lix3nn53.guardiansofadelia.utilities.signmenu.SignMenuFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class GuiGuildEmpty extends GuiGeneric {

    public GuiGuildEmpty(Player player, GuardianData guardianData) {
        super(27, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + Translation.t(guardianData, "menu.guild.name"), 0);

        ItemStack create = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = create.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.GREEN + "Create");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "");
        itemMeta.setLore(lore);
        create.setItemMeta(itemMeta);

        ItemStack rankings = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.GREEN + "Rankings");
        lore.clear();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to view rankings.");
        rankings.setItemMeta(itemMeta);

        ItemStack war = new ItemStack(Material.LIME_WOOL);
        itemMeta.setDisplayName(ChatPalette.GREEN + "War");
        lore.clear();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Click to view or join guild war.");
        war.setItemMeta(itemMeta);

        GuiHelper.form54Small(this, new ItemStack[]{create, rankings, war}, "Main Menu");
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
            } else if (GuiHelper.get54SmallButtonIndex(0) == slot) {
                if (!EconomyUtils.canPay(player, GuildHelper.CREATE_GUILD_COST)) {
                    player.sendMessage(ChatPalette.RED + "Creating a new guild costs 2 silvers.");
                    player.sendMessage(ChatPalette.RED + "You don't have enough coins to create a guild.");
                    return;
                }

                SignMenuFactory signMenuFactory = GuardiansOfAdelia.getSignMenuFactory();

                SignMenuFactory.Menu menu = signMenuFactory.newMenu(Arrays.asList("", "▲ Guild Name ▲", "", "▲ Guild Tag ▲"))
                        .reopenIfFail(false)
                        .response((signPlayer, strings) -> {
                            if (GuardianDataManager.hasGuardianData(player)) {
                                String name = strings[0];
                                String tag = strings[2];

                                return GuildHelper.create(player, name, tag);
                            }

                            return true;
                        });

                menu.open(player);
                if (ActiveGuiManager.hasActiveGui(player)) {
                    // Gui gui = ActiveGuiManager.getActiveGui(player);
                    // gui.onClose();

                    ActiveGuiManager.clearActiveGui(player);
                }
            } else if (GuiHelper.get54SmallButtonIndex(1) == slot) {
                new GuiGuildRanking(guardianData).openInventory(player);
            } else if (GuiHelper.get54SmallButtonIndex(2) == slot) {
                new GuiGuildWar().openInventory(player);
            }
        }
    }
}
