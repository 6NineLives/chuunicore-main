package io.github.lix3nn53.guardiansofadelia.menu.main.guild;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guild.Guild;
import io.github.lix3nn53.guardiansofadelia.guild.GuildManager;
import io.github.lix3nn53.guardiansofadelia.menu.GuiHelper;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiGuild;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuiGuildUpgrades extends GuiGeneric {

    public GuiGuildUpgrades(Player player, GuardianData guardianData) {
        super(27, CustomCharacterGui.MENU_27_FLAT.toString() + ChatPalette.BLACK + Translation.t(guardianData, "menu.language.name"), 0);

        Guild guild = GuildManager.getGuild(player);

        ItemStack info = new ItemStack(Material.LIGHT_BLUE_WOOL);
        ItemMeta itemMeta = info.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.BLUE_LIGHT + "Info");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Hall: " + ChatPalette.GREEN + "lv " + guild.getHallLevel());
        lore.add(ChatPalette.GRAY + "Bank: " + ChatPalette.GREEN + "lv " + guild.getBankLevel());
        lore.add(ChatPalette.GRAY + "Laboratory: " + ChatPalette.GREEN + "lv " + guild.getLabLevel());
        itemMeta.setLore(lore);
        info.setItemMeta(itemMeta);

        ItemStack hall = new ItemStack(Material.LIGHT_BLUE_WOOL);
        itemMeta = hall.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.BLUE_LIGHT + "Hall");
        lore = new ArrayList<>();
        lore.add("");
        lore.add("Increases guild member capacity");
        itemMeta.setLore(lore);
        hall.setItemMeta(itemMeta);

        ItemStack bank = new ItemStack(Material.LIGHT_BLUE_WOOL);
        itemMeta = bank.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.BLUE_LIGHT + "Bank");
        lore = new ArrayList<>();
        lore.add("");
        lore.add("Increases guild bank item capacity");
        itemMeta.setLore(lore);
        bank.setItemMeta(itemMeta);

        ItemStack lab = new ItemStack(Material.LIGHT_BLUE_WOOL);
        itemMeta = lab.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.BLUE_LIGHT + "Laboratory");
        lore = new ArrayList<>();
        lore.add("");
        lore.add("Increases guild buff");
        itemMeta.setLore(lore);
        lab.setItemMeta(itemMeta);

        GuiHelper.form27Small(this, new ItemStack[]{info, hall, bank, lab}, "Guild");
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

        int slot = event.getSlot();
        if (slot == 0) {
            new GuiGuild(player, guardianData).openInventory(player);
        }
    }
}
