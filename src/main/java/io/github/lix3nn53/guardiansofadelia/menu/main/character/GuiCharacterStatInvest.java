package io.github.lix3nn53.guardiansofadelia.menu.main.character;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.attribute.Attribute;
import io.github.lix3nn53.guardiansofadelia.guardian.attribute.AttributeType;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.items.list.OtherItems;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiCharacter;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
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
import java.util.List;

public class GuiCharacterStatInvest extends GuiGeneric {

    public GuiCharacterStatInvest(int pointsLeft, GuardianData guardianData, RPGCharacterStats rpgCharacterStats) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + "Stat Points (Points: " + pointsLeft + ")", 0);

        RPGCharacter activeCharacter = guardianData.getActiveCharacter();
        RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();

        Attribute bonusElementDamage = rpgCharacterStats.getAttribute(AttributeType.BONUS_ELEMENT_DAMAGE);
        int invested = rpgClassStats.getInvested(AttributeType.BONUS_ELEMENT_DAMAGE);
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(AttributeType.BONUS_ELEMENT_DAMAGE.getCustomName() + " (Invested: " + invested + ")");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.YELLOW + bonusElementDamage.getAttributeType().getDescription());
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "general.click.left") + ": +1");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "general.click.right") + ": -1");
        lore.add(ChatPalette.GRAY + "Shift + " + Translation.t(guardianData, "general.click.left") + ": +5");
        lore.add(ChatPalette.GRAY + "Shift + " + Translation.t(guardianData, "general.click.right") + ": -5");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        this.setItem(10, itemStack);

        Attribute bonusElementDefense = rpgCharacterStats.getAttribute(AttributeType.BONUS_ELEMENT_DEFENSE);
        invested = rpgClassStats.getInvested(AttributeType.BONUS_ELEMENT_DEFENSE);
        itemMeta.setDisplayName(AttributeType.BONUS_ELEMENT_DEFENSE.getCustomName() + " (Invested: " + invested + ")");
        lore.set(1, ChatPalette.YELLOW + bonusElementDefense.getAttributeType().getDescription());
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        this.setItem(13, itemStack);

        Attribute bonusMaxHealth = rpgCharacterStats.getAttribute(AttributeType.BONUS_MAX_HEALTH);
        invested = rpgClassStats.getInvested(AttributeType.BONUS_MAX_HEALTH);
        itemMeta.setDisplayName(AttributeType.BONUS_MAX_HEALTH.getCustomName() + " (Invested: " + invested + ")");
        lore.set(1, ChatPalette.YELLOW + bonusMaxHealth.getAttributeType().getDescription());
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        this.setItem(16, itemStack);

        Attribute bonusMaxMana = rpgCharacterStats.getAttribute(AttributeType.BONUS_MAX_MANA);
        invested = rpgClassStats.getInvested(AttributeType.BONUS_MAX_MANA);
        itemMeta.setDisplayName(AttributeType.BONUS_MAX_MANA.getCustomName() + " (Invested: " + invested + ")");
        lore.set(1, ChatPalette.YELLOW + bonusMaxMana.getAttributeType().getDescription());
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        this.setItem(28, itemStack);

        Attribute bonusCriticalChance = rpgCharacterStats.getAttribute(AttributeType.BONUS_CRITICAL_CHANCE);
        invested = rpgClassStats.getInvested(AttributeType.BONUS_CRITICAL_CHANCE);
        itemMeta.setDisplayName(AttributeType.BONUS_CRITICAL_CHANCE.getCustomName() + " (Invested: " + invested + ")");
        lore.set(1, ChatPalette.YELLOW + bonusCriticalChance.getAttributeType().getDescription());
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        this.setItem(31, itemStack);

        Attribute bonusCriticalDamage = rpgCharacterStats.getAttribute(AttributeType.BONUS_CRITICAL_DAMAGE);
        invested = rpgClassStats.getInvested(AttributeType.BONUS_CRITICAL_DAMAGE);
        itemMeta.setDisplayName(AttributeType.BONUS_CRITICAL_DAMAGE.getCustomName() + " (Invested: " + invested + ")");
        lore.set(1, ChatPalette.YELLOW + bonusCriticalDamage.getAttributeType().getDescription());
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        this.setItem(34, itemStack);

        ItemStack backButton = OtherItems.getBackButton("Character Menu");
        this.setItem(0, backButton);
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

        int slot = event.getSlot();

        if (rpgCharacter != null) {
            RPGCharacterStats rpgCharacterStats = rpgCharacter.getRpgCharacterStats();
            RPGClassStats rpgClassStats = rpgCharacter.getRPGClassStats();
            Attribute attr = null;
            if (slot == 0) {
                GuiCharacter gui = new GuiCharacter(guardianData);
                gui.openInventory(player);
                return;
            } else if (slot == 10) {
                attr = rpgCharacterStats.getAttribute(AttributeType.BONUS_ELEMENT_DAMAGE);
            } else if (slot == 13) {
                attr = rpgCharacterStats.getAttribute(AttributeType.BONUS_ELEMENT_DEFENSE);
            } else if (slot == 16) {
                attr = rpgCharacterStats.getAttribute(AttributeType.BONUS_MAX_HEALTH);
            } else if (slot == 28) {
                attr = rpgCharacterStats.getAttribute(AttributeType.BONUS_MAX_MANA);
            } else if (slot == 31) {
                attr = rpgCharacterStats.getAttribute(AttributeType.BONUS_CRITICAL_CHANCE);
            } else if (slot == 34) {
                attr = rpgCharacterStats.getAttribute(AttributeType.BONUS_CRITICAL_DAMAGE);
            }

            if (attr != null) {
                if (event.isLeftClick()) {
                    int pointsLeftToSpend = rpgClassStats.getAttributePointsLeftToSpend(rpgCharacterStats);
                    if (pointsLeftToSpend > 0) {
                        int amount = 1;
                        if (event.isShiftClick()) {
                            if (pointsLeftToSpend >= 5) {
                                amount = 5;
                            } else {
                                amount = pointsLeftToSpend;
                            }
                        }

                        rpgClassStats.investPoint(attr, amount, rpgCharacterStats, true);
                        int pointsLeft = rpgClassStats.getAttributePointsLeftToSpend(rpgCharacterStats);
                        GuiCharacterStatInvest gui = new GuiCharacterStatInvest(pointsLeft, guardianData, rpgCharacterStats);
                        gui.openInventory(player);

                        List<Quest> questList = rpgCharacter.getQuestList();
                        for (Quest quest : questList) {
                            quest.progressUpgradeStatTasks(player, attr);
                        }
                    }
                } else if (event.isRightClick()) {
                    int invested = rpgClassStats.getInvested(attr.getAttributeType());
                    if (invested > 0) {
                        int amount = 1;
                        if (event.isShiftClick()) {
                            if (invested >= 5) {
                                amount = 5;
                            } else {
                                amount = invested;
                            }
                        }

                        rpgClassStats.downgradePoint(attr, amount, rpgCharacterStats, true);
                        int pointsLeft = rpgClassStats.getAttributePointsLeftToSpend(rpgCharacterStats);
                        GuiCharacterStatInvest gui = new GuiCharacterStatInvest(pointsLeft, guardianData, rpgCharacterStats);
                        gui.openInventory(player);
                    }
                }
            }
        }
    }
}
