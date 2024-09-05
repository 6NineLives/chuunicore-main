package io.github.lix3nn53.guardiansofadelia.guardian.character;

import io.github.lix3nn53.guardiansofadelia.guardian.attribute.AttributeType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ArmorGearType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ShieldGearType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.WeaponGearType;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RPGClassManager {

    private static final HashMap<String, RPGClass> rpgClassMap = new HashMap<>();

    public static void setClass(String className, RPGClass rpgClass) {
        rpgClassMap.put(className.toUpperCase(), rpgClass);
    }

    public static boolean hasClass(String className) {
        return rpgClassMap.containsKey(className.toUpperCase());
    }

    public static RPGClass getClass(String className) {
        return rpgClassMap.get(className.toUpperCase());
    }

    public static void clearClasses() {
        rpgClassMap.clear();
    }

    public static Set<String> getClasses() {
        return rpgClassMap.keySet();
    }

    public static ItemStack getClassIcon(RPGClass rpgClass) {
        ItemStack itemStack = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setCustomModelData(rpgClass.getClassIconCustomModelData());
        String classString = rpgClass.getClassString();
        itemMeta.setDisplayName(classString.toUpperCase());

        List<String> description = rpgClass.getDescription();
        List<WeaponGearType> weaponGearTypes = rpgClass.getWeaponGearTypes();
        List<ArmorGearType> armorGearTypes = rpgClass.getArmorGearTypes();
        List<ShieldGearType> shieldGearTypes = rpgClass.getShieldGearTypes();
        HashMap<AttributeType, Integer> attributeTiers = rpgClass.getAttributeTiers();

        List<String> lore = new ArrayList<>(description);
        lore.add("");

        lore.add(ChatPalette.GREEN_DARK + "Attributes");
        StringBuilder bonusAttributes = new StringBuilder(" ");
        for (AttributeType attributeType : AttributeType.values()) {
            int bonus = attributeTiers.get(attributeType);
            String shortName = attributeType.getShortName();
            bonusAttributes.append(shortName).append("=").append(bonus).append(" ");
        }
        lore.add(bonusAttributes.toString());

        lore.add(ChatPalette.RED + "Weapons");
        for (WeaponGearType type : weaponGearTypes) {
            lore.add("  - " + type.getDisplayName());
        }
        lore.add(ChatPalette.BLUE_LIGHT + "Armors");
        for (ArmorGearType type : armorGearTypes) {
            lore.add("  - " + type.getDisplayName());
        }
        if (!shieldGearTypes.isEmpty()) {
            lore.add(ChatPalette.BLUE + "Shields");
            for (ShieldGearType type : shieldGearTypes) {
                lore.add("  - " + type.getDisplayName());
            }
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
