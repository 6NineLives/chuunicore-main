package io.github.lix3nn53.guardiansofadelia.cosmetic.gui;

import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticManager;
import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticRoom;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.Cosmetic;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticColor;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticType;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.items.list.OtherItems;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class CosmeticRoomGui extends GuiGeneric {

    private static final int[] ITEM_SLOTS = {20, 21, 22, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42};
    private final HashMap<Integer, Integer> slotToCosmetic = new HashMap<>();
    private CosmeticType selectedType;
    private int pageIndex;

    public CosmeticRoomGui(GuardianData guardianData, CosmeticType selectedType, int pageIndex) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + "Cosmetic Room", 0);

        this.selectedType = selectedType;
        this.pageIndex = pageIndex;

        ItemStack weapons = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = weapons.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setCustomModelData(57);
        itemMeta.setDisplayName(ChatPalette.BLUE + Translation.t(guardianData, "cosmetic.weapon.name"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.weapon.l1"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.weapon.l2"));
        itemMeta.setLore(lore);
        weapons.setItemMeta(itemMeta);
        this.setItem(2, weapons);

        ItemStack helmet = new ItemStack(Material.WOODEN_PICKAXE);
        itemMeta.setCustomModelData(56);
        itemMeta.setDisplayName(ChatPalette.BLUE + Translation.t(guardianData, "cosmetic.helmet.name"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.helmet.l1"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.helmet.l2"));
        itemMeta.setLore(lore);
        helmet.setItemMeta(itemMeta);
        this.setItem(4, helmet);

        ItemStack back = new ItemStack(Material.WOODEN_PICKAXE);
        itemMeta.setCustomModelData(55);
        itemMeta.setDisplayName(ChatPalette.BLUE + Translation.t(guardianData, "cosmetic.back.name"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.back.l1"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.back.l2"));
        itemMeta.setLore(lore);
        back.setItemMeta(itemMeta);
        this.setItem(6, back);

        ItemStack reset = new ItemStack(Material.RED_WOOL);
        itemMeta.setCustomModelData(55);
        itemMeta.setDisplayName(ChatPalette.RED + Translation.t(guardianData, "cosmetic.reset.name"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.reset.l1"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.reset.l2"));
        itemMeta.setLore(lore);
        reset.setItemMeta(itemMeta);
        this.setItem(27, reset);

        ItemStack apply = new ItemStack(Material.LIME_WOOL);
        itemMeta.setCustomModelData(55);
        itemMeta.setDisplayName(ChatPalette.GREEN + Translation.t(guardianData, "cosmetic.apply.name"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.apply.l1"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.apply.l2"));
        itemMeta.setLore(lore);
        apply.setItemMeta(itemMeta);
        this.setItem(35, apply);

        ItemStack prev = OtherItems.getArrowLeft();
        itemMeta = prev.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + Translation.t(guardianData, "cosmetic.prev.name"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.prev.l1"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.prev.l2"));
        itemMeta.setLore(lore);
        prev.setItemMeta(itemMeta);
        this.setItem(47, prev);

        ItemStack next = OtherItems.getArrowRight();
        itemMeta = next.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.YELLOW + Translation.t(guardianData, "cosmetic.next.name"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.next.l1"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "cosmetic.next.l2"));
        itemMeta.setLore(lore);
        next.setItemMeta(itemMeta);
        this.setItem(51, next);

        setCosmetics(selectedType, pageIndex);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GuardianData guardianData;
        RPGCharacter rpgCharacter;
        if (GuardianDataManager.hasGuardianData(player)) {
            guardianData = GuardianDataManager.getGuardianData(player);

            if (guardianData.hasActiveCharacter()) {
                rpgCharacter = guardianData.getActiveCharacter();
            } else {
                return;
            }
        } else {
            return;
        }

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;

        int slot = event.getSlot();
        if (slot == 2) {
            new CosmeticRoomGuiWeapon(guardianData, selectedType, pageIndex).openInventory(player);
            return;
        } else if (slot == 4) {
            selectedType = CosmeticType.HELMET_SKIN;
        } else if (slot == 6) {
            selectedType = CosmeticType.COSMETIC_BACK;
        } else if (slot == 27) {
            // todo reset
        } else if (slot == 35) {
            CosmeticRoom.apply(player);
            player.sendMessage(ChatPalette.GREEN + Translation.t(guardianData, "cosmetic.apply.success"));
        } else if (slot == 46) {
            if (pageIndex > 0) {
                pageIndex--;
            }
        } else if (slot == 52) {
            pageIndex++;
        } else if (slotToCosmetic.containsKey(slot)) {
            // click on cosmetic
            int cosmeticId = slotToCosmetic.get(slot);
            if (selectedType.canChangeColor()) {
                new CosmeticRoomGuiColor(guardianData, cosmeticId).openInventory(player);
            } else {
                CosmeticRoom.setCosmetic(player, cosmeticId, null, 0);
                player.closeInventory();
            }
            return;
        }

        setCosmetics(selectedType, pageIndex);
    }

    private void setCosmetics(CosmeticType cosmeticType, int page) {
        int start = ITEM_SLOTS.length * page;

        start = start + cosmeticType.getIdOffset();

        slotToCosmetic.clear();
        for (int i = 0; i < ITEM_SLOTS.length; i++) {
            int slot = ITEM_SLOTS[i];

            int cosmeticId = start + i;
            Cosmetic cosmetic = CosmeticManager.get(cosmeticId);

            if (cosmetic != null) {
                this.setItem(slot, cosmetic.getShowcase(CosmeticColor.RED, 2));
                slotToCosmetic.put(slot, cosmeticId);
            } else {
                this.setItem(slot, new ItemStack(Material.AIR));
            }
        }
    }
}
