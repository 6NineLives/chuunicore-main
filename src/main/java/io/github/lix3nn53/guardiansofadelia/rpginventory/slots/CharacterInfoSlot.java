package io.github.lix3nn53.guardiansofadelia.rpginventory.slots;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.attribute.AttributeType;
import io.github.lix3nn53.guardiansofadelia.guardian.character.*;
import io.github.lix3nn53.guardiansofadelia.guardian.element.Element;
import io.github.lix3nn53.guardiansofadelia.guardian.element.ElementType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillBar;
import io.github.lix3nn53.guardiansofadelia.items.stats.StatUtils;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CharacterInfoSlot {

    private final Player player;

    public CharacterInfoSlot(Player player) {
        this.player = player;
    }

    public ItemStack getItem(GuardianData viewerData) {
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData targetData = GuardianDataManager.getGuardianData(player);
            if (targetData.hasActiveCharacter()) {
                String language = viewerData.getLanguage();

                RPGCharacter activeCharacter = targetData.getActiveCharacter();
                RPGCharacterStats rpgCharacterStats = activeCharacter.getRpgCharacterStats();
                RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();

                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                PlayerProfile playerProfile = player.getPlayerProfile();
                skullMeta.setOwnerProfile(playerProfile);
                skullMeta.setDisplayName(ChatPalette.YELLOW + Translation.t(viewerData, "character.info"));

                String rpgClassStr = activeCharacter.getRpgClassStr();
                RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);
                String className = rpgClass.getClassString();
                int mana = rpgCharacterStats.getCurrentMana();
                int maxMana = rpgCharacterStats.getTotalMaxMana(rpgClassStats);
                final int level = player.getLevel();
                int totalExp = rpgCharacterStats.getTotalExp();
                int exp = RPGCharacterExperienceManager.getCurrentExperience(totalExp, level);
                int expReq = RPGCharacterExperienceManager.getRequiredExperience(level);
                int maxHealth = (int) (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 0.5);
                int health = (int) (player.getHealth() + 0.5);

                float criticalChance = rpgCharacterStats.getTotalCriticalChance(rpgClassStats) * 100;
                int critChanceAttr = rpgClassStats.getInvested(AttributeType.BONUS_CRITICAL_CHANCE) + rpgCharacterStats.getAttribute(AttributeType.BONUS_CRITICAL_CHANCE).getBonusFromEquipment();
                float criticalDamage = rpgCharacterStats.getTotalCriticalDamage(rpgClassStats) * 100;
                int critDamageAttr = rpgClassStats.getInvested(AttributeType.BONUS_CRITICAL_DAMAGE) + rpgCharacterStats.getAttribute(AttributeType.BONUS_CRITICAL_DAMAGE).getBonusFromEquipment();

                final int totalDefense = rpgCharacterStats.getTotalElementDefense(rpgClassStats);
                float defenseReduction = StatUtils.getDefenseReduction(totalDefense);

                float abilityHaste = rpgCharacterStats.getTotalAbilityHaste();
                float cooldownReduction = SkillBar.abilityHasteToMultiplier(abilityHaste);

                ArrayList<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(ChatPalette.PURPLE_LIGHT + Translation.t(viewerData, "character.class.name") + ": " + ChatPalette.GRAY + "" + className);
                lore.add(ChatPalette.GOLD + Translation.t(viewerData, "general.level") + ": " + ChatPalette.GRAY + "" + level);
                lore.add(ChatPalette.YELLOW + Translation.t(viewerData, "general.experience.name") + ": " + ChatPalette.GRAY + exp + "/" + expReq);
                lore.add(ChatPalette.GREEN_DARK + "❤ " + Translation.t(viewerData, "attribute.max_health") + ": " + ChatPalette.GRAY + "" + health + "/" + maxHealth);
                lore.add(ChatPalette.BLUE_LIGHT + "✦ " + Translation.t(viewerData, "attribute.max_mana") + ": " + ChatPalette.GRAY + "" + mana + "/" + maxMana);
                lore.add("");
                lore.add(ChatPalette.RED + "✦ " + Translation.t(viewerData, "attribute.element_damage") + ": " + ChatPalette.GRAY + rpgCharacterStats.getTotalElementDamage(player, rpgClassStr, rpgClassStats));
                lore.add(ChatPalette.BLUE_LIGHT + "■ " + Translation.t(viewerData, "attribute.element_defense") + ": " + ChatPalette.GRAY + totalDefense + " (" + new DecimalFormat("##.##").format((1.0 - defenseReduction) * 100) + "% " + Translation.t(viewerData, "attribute.reduction") + ")");
                lore.add(ChatPalette.GOLD + "☆ " + Translation.t(viewerData, "attribute.crit_chance") + ": " + ChatPalette.GRAY + critChanceAttr + " (" + new DecimalFormat("##.##").format(criticalChance) + "%)");
                lore.add("");
                lore.add(ChatPalette.GOLD + "? " + Translation.t(viewerData, "attribute.crit_damage") + ": " + ChatPalette.GRAY + critDamageAttr + " (" + new DecimalFormat("##.##").format(criticalDamage) + "%)");
                lore.add(ChatPalette.BLUE_LIGHT + "? " + Translation.t(viewerData, "attribute.ability_haste") + ": " + ChatPalette.GRAY + abilityHaste + " (" + new DecimalFormat("##.##").format((1.0 - cooldownReduction) * 100) + "% " + Translation.t(viewerData, "attribute.reduction") + ")");

                /*lore.add("");
                lore.add(ChatPalette.GRAY + "(equipment + level + invested points)");
                // Add attributes to lore (equipment + level + invested points)
                for (AttributeType attributeType : AttributeType.values()) {
                    io.github.lix3nn53.guardiansofadelia.guardian.attribute.Attribute attribute = rpgCharacterStats.getAttribute(attributeType);
                    lore.add(attributeType.getCustomName() + ": " + ChatPalette.GRAY + attribute.getBonusFromEquipment() + " + " + attribute.getBonusFromLevel(level, rpgClassStr) + " + " + attribute.getInvested());
                }*/

                lore.add("");
                for (ElementType elementType : ElementType.values()) {
                    Element element = rpgCharacterStats.getElement(elementType);
                    lore.add(elementType.getFullName(language) + ": " + ChatPalette.GRAY + element.getTotal());
                }
                skullMeta.setLore(lore);
                itemStack.setItemMeta(skullMeta);
                return itemStack;
            }
        }
        return new ItemStack(Material.AIR);
    }
}
