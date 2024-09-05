package io.github.lix3nn53.guardiansofadelia.guardian.skill.managers;

import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ShieldGearType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.WeaponGearType;

import java.util.HashMap;

public class GearMechanicSkillManager {

    private static final HashMap<String, HashMap<WeaponGearType, Integer>> classToWeaponToSkillId = new HashMap<>();
    private static final HashMap<String, HashMap<ShieldGearType, Integer>> classToShieldToSkillId = new HashMap<>();

    public static void register(String rpgClass, WeaponGearType weaponGearType, int skillId) {
        HashMap<WeaponGearType, Integer> weaponGearTypeIntegerHashMap = classToWeaponToSkillId.get(rpgClass);
        if (weaponGearTypeIntegerHashMap == null) {
            weaponGearTypeIntegerHashMap = new HashMap<>();
        }
        weaponGearTypeIntegerHashMap.put(weaponGearType, skillId);
        classToWeaponToSkillId.put(rpgClass, weaponGearTypeIntegerHashMap);
    }

    public static int getRequiredSkillId(String rpgClass, WeaponGearType weaponGearType) {
        HashMap<WeaponGearType, Integer> weaponGearTypeIntegerHashMap = classToWeaponToSkillId.get(rpgClass);
        if (weaponGearTypeIntegerHashMap == null) {
            return -1;
        }

        if (!weaponGearTypeIntegerHashMap.containsKey(weaponGearType)) {
            return -1;
        }

        return weaponGearTypeIntegerHashMap.get(weaponGearType);
    }

    public static void register(String rpgClass, ShieldGearType weaponGearType, int skillId) {
        HashMap<ShieldGearType, Integer> weaponGearTypeIntegerHashMap = classToShieldToSkillId.get(rpgClass);
        if (weaponGearTypeIntegerHashMap == null) {
            weaponGearTypeIntegerHashMap = new HashMap<>();
        }
        weaponGearTypeIntegerHashMap.put(weaponGearType, skillId);
        classToShieldToSkillId.put(rpgClass, weaponGearTypeIntegerHashMap);
    }

    public static int getRequiredSkillId(String rpgClass, ShieldGearType weaponGearType) {
        HashMap<ShieldGearType, Integer> weaponGearTypeIntegerHashMap = classToShieldToSkillId.get(rpgClass);
        if (weaponGearTypeIntegerHashMap == null) {
            return -1;
        }
        return weaponGearTypeIntegerHashMap.get(weaponGearType);
    }
}
