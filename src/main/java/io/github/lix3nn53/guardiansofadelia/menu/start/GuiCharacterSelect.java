package io.github.lix3nn53.guardiansofadelia.menu.start;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.towns.Town;
import io.github.lix3nn53.guardiansofadelia.towns.TownManager;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import io.github.lix3nn53.guardiansofadelia.utilities.managers.CharacterSelectionScreenManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiCharacterSelect extends GuiGeneric {

    private final int charNo;
    private final HashMap<Integer, Integer> slotNoToTownNo = new HashMap<>();

    public GuiCharacterSelect(GuardianData guardianData, int charNo) {
        super(27, CustomCharacterGui.MENU_27_FLAT.toString() + ChatPalette.BLACK +
                Translation.t(guardianData, "character.name") + " " + charNo
                + " " + Translation.t(guardianData, "character.selection.name"), 0);
        this.charNo = charNo;

        ItemStack lastLocation = new ItemStack(Material.MAGENTA_WOOL);
        ItemMeta itemMeta = lastLocation.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.setDisplayName(ChatPalette.PURPLE_LIGHT + Translation.t(guardianData, "character.selection.last_loc"));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        lastLocation.setItemMeta(itemMeta);
        this.setItem(11, lastLocation);

        int index = 13;
        HashMap<Integer, Town> towns = TownManager.getTowns();
        for (int key : towns.keySet()) {
            Town town = towns.get(key);
            ItemStack itemStack = new ItemStack(Material.LIGHT_BLUE_WOOL);
            itemMeta = itemStack.getItemMeta();

            ArrayList<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatPalette.GRAY + Translation.t(guardianData, "character.selection.l1"));
            lore.add("");
            lore.add(ChatPalette.GRAY + Translation.t(guardianData, "condition.level") + ": " + town.getLevel());
            itemMeta.setLore(lore);

            itemMeta.setDisplayName(ChatPalette.BLUE_LIGHT + town.getName() + " #" + key);
            itemStack.setItemMeta(itemMeta);

            this.setItem(index, itemStack);

            slotNoToTownNo.put(index, key);
        }

        ItemStack remove = new ItemStack(Material.RED_WOOL);
        itemMeta = remove.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Remove character at slot " + charNo);
        lore.add("");
        lore.add(ChatPalette.RED + "WARNING: This action cannot be undone!");
        lore.add("");
        lore.add(ChatPalette.GRAY + "If you don't want to lose your items...");
        lore.add(ChatPalette.GRAY + "...you can put them in storage.");
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(ChatPalette.RED + "Remove character");
        remove.setItemMeta(itemMeta);
        this.setItem(26, remove);
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

        if (slot == 11) {
            Location charLocation = CharacterSelectionScreenManager.getCharLocation(player, charNo);
            if (charLocation != null) {
                CharacterSelectionScreenManager.selectCharacter(player, charNo, charLocation);
            } else {
                player.sendMessage(ChatPalette.RED + Translation.t(guardianData, "character.selection.error"));
            }
        } else if (slot == 26) {
            CharacterSelectionScreenManager.setToBeRemoved(player, charNo);
            player.closeInventory();
        } else {
            if (!slotNoToTownNo.containsKey(slot)) return;
            int townNo = slotNoToTownNo.get(slot);

            Town town = TownManager.getTown(townNo);

            int charLevel = CharacterSelectionScreenManager.getCharLevel(player, charNo);
            if (charLevel < town.getLevel()) return;

            Location location = town.getLocation();

            CharacterSelectionScreenManager.selectCharacter(player, charNo, location);
        }
    }
}
