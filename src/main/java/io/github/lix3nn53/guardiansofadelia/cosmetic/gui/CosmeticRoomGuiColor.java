package io.github.lix3nn53.guardiansofadelia.cosmetic.gui;

import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticManager;
import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticRoom;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.Cosmetic;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticColor;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CosmeticRoomGuiColor extends GuiGeneric {

    private final int cosmeticId;
    private CosmeticColor selectedColor = CosmeticColor.RED;
    private int selectedTint = 2;

    public CosmeticRoomGuiColor(GuardianData guardianData, int cosmeticId) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + "Cosmetic Color", 0);

        this.cosmeticId = cosmeticId;

        List<Integer> guiIndex = CosmeticColor.getGuiIndex();
        for (CosmeticColor color : CosmeticColor.values()) {
            ItemStack item = color.getItem(2);
            this.setItem(guiIndex.get(color.ordinal()), item);
        }

        int index = 37;

        for (int i = 0; i < 7; i++) {
            ItemStack item = selectedColor.getItem(i);
            this.setItem(index + i, item);
        }

        Cosmetic cosmetic = CosmeticManager.get(cosmeticId);
        ItemStack showcase = cosmetic.getShowcase(selectedColor, selectedTint);
        this.setItem(25, showcase);
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

        List<Integer> guiIndex = CosmeticColor.getGuiIndex();

        int slot = event.getSlot();
        if (guiIndex.contains(slot)) {
            int colorIndex = guiIndex.indexOf(slot);
            selectedColor = CosmeticColor.values()[colorIndex];

            int index = 37;

            for (int i = 0; i < 7; i++) {
                ItemStack item = selectedColor.getItem(i);
                this.setItem(index + i, item);
            }

            Cosmetic cosmetic = CosmeticManager.get(cosmeticId);
            ItemStack showcase = cosmetic.getShowcase(selectedColor, selectedTint);
            this.setItem(25, showcase);
        } else if (slot >= 37 && slot < 44) {
            selectedTint = slot - 37;

            Cosmetic cosmetic = CosmeticManager.get(cosmeticId);
            ItemStack showcase = cosmetic.getShowcase(selectedColor, selectedTint);
            this.setItem(25, showcase);
        } else if (slot == 25) {
            CosmeticRoom.setCosmetic(player, cosmeticId, selectedColor, selectedTint);
            player.closeInventory();
        }
    }
}
