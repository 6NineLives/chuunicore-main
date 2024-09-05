package io.github.lix3nn53.guardiansofadelia.menu.start;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.menu.GuiHelper;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GuiClassSelect extends GuiGeneric {

    private final int charNo;

    private final List<RPGClass> values = new ArrayList<>();

    public GuiClassSelect(GuardianData guardianData, int charNo) {
        super(54, CustomCharacterGui.MENU_54.toString() + ChatPalette.BLACK +
                Translation.t(guardianData, "general.tutorial.classSelect") + " #" + charNo, 0);
        this.charNo = charNo;

        List<ItemStack> items = new ArrayList<>();

        Set<String> valuesStr = RPGClassManager.getClasses();
        for (String valueStr : valuesStr) {
            RPGClass value = RPGClassManager.getClass(valueStr);
            values.add(value);

            ItemStack itemStack = RPGClassManager.getClassIcon(value);

            items.add(itemStack);
        }

        ItemStack[] array = new ItemStack[items.size()];
        items.toArray(array); // fill the array

        GuiHelper.form54Big(this, array, null);
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

        String rpgClassStr = null;

        int slot = event.getSlot();

        for (int i = 0; i < values.size(); i++) {
            List<Integer> bigButtonIndexes = GuiHelper.get54BigButtonIndexes(i);

            if (bigButtonIndexes == null) {
                continue;
            }

            if (bigButtonIndexes.contains(slot)) {
                rpgClassStr = values.get(i).getClassEnum();
                break;
            }
        }

        if (rpgClassStr == null) return;

        GuiTutorialSkip gui = new GuiTutorialSkip(guardianData, charNo, rpgClassStr);
        gui.openInventory(player);
    }
}
