package io.github.lix3nn53.guardiansofadelia.quests.actions;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.items.config.WeaponReferenceData;
import io.github.lix3nn53.guardiansofadelia.menu.quest.GuiQuestTaskPrizeSelect;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WeaponSelectOneOfAction implements Action {

    private final WeaponReferenceData weaponPrizesSelectOneOf;

    public WeaponSelectOneOfAction(WeaponReferenceData weaponPrizesSelectOneOf) {
        this.weaponPrizesSelectOneOf = weaponPrizesSelectOneOf;
    }

    @Override
    public void perform(Player player, int questNo, int taskIndex) {
        if (taskIndex < 0) {
            player.sendMessage(ChatPalette.RED_DARK + "Configuration error, please report to a developer!");
            player.sendMessage(ChatPalette.RED_DARK + "You can't use WeaponSelectOneOfAction as quest action, only task action");
        }

        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            if (guardianData.hasActiveCharacter()) {
                RPGCharacter rpgCharacter = guardianData.getActiveCharacter();
                // GUISIZE
                /*int guiSize = 18;
                if (weaponPrizesSelectOneOf != null) {
                    guiSize += 9;
                }*/

                String rpgClassStr = rpgCharacter.getRpgClassStr();
                RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);

                List<ItemStack> items = weaponPrizesSelectOneOf.getItems(rpgClass);
                GuiQuestTaskPrizeSelect gui = new GuiQuestTaskPrizeSelect(guardianData, questNo, 0, taskIndex, items);
                gui.openInventory(player);
            }
        }
    }

    @Override
    public boolean preventTaskCompilation() {
        return true;
    }
}
