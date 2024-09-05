package io.github.lix3nn53.guardiansofadelia.cosmetic.gui;

import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticType;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ShieldGearType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.WeaponGearType;
import io.github.lix3nn53.guardiansofadelia.menu.GuiHelper;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CosmeticRoomGuiWeapon extends GuiGeneric {

    private final int pageIndex;
    private CosmeticType selectedType;

    public CosmeticRoomGuiWeapon(GuardianData guardianData, CosmeticType selectedType, int pageIndex) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + "Cosmetic Weapon", 0);

        this.selectedType = selectedType;
        this.pageIndex = pageIndex;

        List<ItemStack> list = new ArrayList<>();

        for (WeaponGearType weaponGearType : WeaponGearType.values()) {
            Material material = weaponGearType.getMaterial();
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(1);
            itemMeta.setDisplayName(ChatPalette.YELLOW + weaponGearType.getDisplayName());
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(itemMeta);
            list.add(itemStack);
        }

        Material material = ShieldGearType.SHIELD.getMaterial();
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(1);
        itemMeta.setDisplayName(ChatPalette.YELLOW + ShieldGearType.SHIELD.getDisplayName());
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        list.add(itemStack);

        ItemStack[] array = new ItemStack[list.size()];
        list.toArray(array); // fill the array

        GuiHelper.form54Small(this, array, "Cosmetic Menu");
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
        if (slot == 0) {
            new CosmeticRoomGui(guardianData, selectedType, pageIndex).openInventory(player);
            return;
        }

        WeaponGearType[] values = WeaponGearType.values();

        if (GuiHelper.get54SmallButtonIndex(values.length) == slot) {
            selectedType = CosmeticType.SHIELD_SKIN;
            new CosmeticRoomGui(guardianData, selectedType, 0).openInventory(player);
            return;
        }

        int i = 0;
        for (WeaponGearType weaponGearType : values) {
            if (GuiHelper.get54SmallButtonIndex(i) == slot) {
                selectedType = CosmeticType.valueOf("WEAPON_SKIN_" + weaponGearType.name());
                new CosmeticRoomGui(guardianData, selectedType, 0).openInventory(player);
                return;
            }
            i++;
        }
    }
}
