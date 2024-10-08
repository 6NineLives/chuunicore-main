package io.github.lix3nn53.guardiansofadelia.menu.quest;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.items.GearLevel;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ItemTier;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.gearset.GearSetManager;
import io.github.lix3nn53.guardiansofadelia.items.config.ArmorReferenceData;
import io.github.lix3nn53.guardiansofadelia.items.config.WeaponReferenceData;
import io.github.lix3nn53.guardiansofadelia.items.stats.GearStatType;
import io.github.lix3nn53.guardiansofadelia.items.stats.StatUtils;
import io.github.lix3nn53.guardiansofadelia.npc.QuestNPCManager;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacter;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.InventoryUtils;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiQuestPrizeSelect extends GuiGeneric {

    private final int questNo;
    private final int resourceNPC;

    public GuiQuestPrizeSelect(int guiSize, CustomCharacter customCharacter, int questNo, int resourceNPC, List<ItemStack> itemPrizesSelectOneOf,
                               WeaponReferenceData weaponPrizesSelectOneOf, ArmorReferenceData armorPrizesSelectOneOf,
                               RPGCharacter rpgCharacter, GuardianData guardianData) {
        super(guiSize, customCharacter.toString() + ChatPalette.BLACK +
                Translation.t(guardianData, "quest.prize.selection") + " #" + questNo, resourceNPC);
        this.questNo = questNo;
        this.resourceNPC = resourceNPC;

        // ITEM SLOTS
        List<Integer> slotsToUse = new ArrayList<>();
        slotsToUse.add(10);
        slotsToUse.add(12);
        slotsToUse.add(14);
        slotsToUse.add(16);

        slotsToUse.add(19);
        slotsToUse.add(21);
        slotsToUse.add(23);
        slotsToUse.add(25);

        slotsToUse.add(28);
        slotsToUse.add(30);
        slotsToUse.add(32);
        slotsToUse.add(34);

        slotsToUse.add(28);
        slotsToUse.add(30);
        slotsToUse.add(32);
        slotsToUse.add(34);

        int normalSelectOneOfSize = itemPrizesSelectOneOf.size();

        // PLACE ITEMS
        int index = 0;
        for (int i = index; i < normalSelectOneOfSize; i++) {
            ItemStack itemStack = itemPrizesSelectOneOf.get(i);
            Integer slotNo = slotsToUse.get(i);
            this.setItem(slotNo, itemStack);
            index++;
        }

        String rpgClassStr = rpgCharacter.getRpgClassStr();
        RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);
        if (weaponPrizesSelectOneOf != null) {
            List<ItemStack> items = weaponPrizesSelectOneOf.getItems(rpgClass);
            for (ItemStack itemStack : items) {
                Integer slotNo = slotsToUse.get(index);
                this.setItem(slotNo, itemStack);
                index++;
            }
        }

        if (armorPrizesSelectOneOf != null) {
            List<ItemStack> items = armorPrizesSelectOneOf.getItems(rpgClass);
            for (ItemStack itemStack : items) {
                Integer slotNo = slotsToUse.get(index);
                this.setItem(slotNo, itemStack);
                index++;
            }
        }
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

        ItemStack current = this.getItem(event.getSlot());

        //give item
        GearStatType gearStatType = StatUtils.getStatType(current);
        if (gearStatType != null) {
            GearLevel gearLevel = GearLevel.getGearLevel(current);
            ItemTier itemTier = ItemTier.getItemTierOfItemStack(current);
            StatUtils.addRandomPassiveStats(current, gearLevel, itemTier);
            GearSetManager.addRandomGearEffect(current);
        }
        InventoryUtils.giveItemToPlayer(player, current);

        //turnin quest
        boolean didTurnIn = rpgCharacter.turnInQuest(questNo, player, false, resourceNPC);
        player.closeInventory();
        if (didTurnIn) {
            Quest questCopyById = QuestNPCManager.getQuestCopyById(questNo);
            List<String> turnInDialogue = questCopyById.getTurnInDialogue();

            if (turnInDialogue == null || turnInDialogue.isEmpty()) {
                NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
                NPC byId = npcRegistry.getById(this.getResourceNPC());
                GuiQuestList questGui = new GuiQuestList(byId, player, guardianData);
                questGui.openInventory(player);
            }
        } else {
            player.sendMessage(ChatPalette.RED + "Couldn't turn in the quest ERROR report pls");
        }
    }
}
