package io.github.lix3nn53.guardiansofadelia.cosmetic;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.Cosmetic;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ShieldGearType;
import io.github.lix3nn53.guardiansofadelia.items.stats.GearStatType;
import io.github.lix3nn53.guardiansofadelia.items.stats.StatUtils;
import io.github.lix3nn53.guardiansofadelia.utilities.PersistentDataContainerUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CosmeticManager {

    public static final Material COSMETIC_MATERIAL = Material.LEATHER_HORSE_ARMOR;
    public static final Material PREMIUM_CONSUMABLE_MATERIAL = Material.BLACK_DYE;

    private static final HashMap<Integer, Cosmetic> globalCosmeticMap = new HashMap<>();

    public static void add(int id, Cosmetic cosmetic) {
        CosmeticType type = cosmetic.getType();

        int idOffset = type.getIdOffset();

        id = id + idOffset;

        if (globalCosmeticMap.containsKey(id)) {
            GuardiansOfAdelia.getInstance().getLogger().warning("Cosmetic id " + id + " already exists!");
            return;
        }

        GuardiansOfAdelia.getInstance().getLogger().info("Added cosmetic " + cosmetic.getName() + " with id " + id);

        globalCosmeticMap.put(id, cosmetic);
    }

    public static Cosmetic get(int id) {
        return globalCosmeticMap.get(id);
    }

    public static boolean isCosmeticItem(ItemStack itemStack) {
        return itemStack.getType().equals(COSMETIC_MATERIAL);
    }

    public static CosmeticType getCosmeticType(ItemStack itemStack) {
        if (itemStack.getType().equals(COSMETIC_MATERIAL)) {
            if (PersistentDataContainerUtil.hasString(itemStack, "cosmetic")) {
                int cosmeticId = PersistentDataContainerUtil.getInteger(itemStack, "cosmetic");

                if (!globalCosmeticMap.containsKey(cosmeticId)) {
                    return null;
                }

                return globalCosmeticMap.get(cosmeticId).getType();
            }
        }

        return null;
    }

    public static boolean isAppliedHelmetSkin(ItemStack itemStack) {
        CosmeticType cosmeticType = getCosmeticType(itemStack);

        if (cosmeticType == null) {
            return false;
        }

        return cosmeticType.equals(CosmeticType.HELMET_SKIN);
    }

    public static boolean isAppliedWeaponSkin(ItemStack itemStack) {
        if (StatUtils.getStatType(itemStack).equals(GearStatType.WEAPON_GEAR)) { // Weapon skin is weapon material unlike other cosmetics
            int customModelData = itemStack.getItemMeta().getCustomModelData();

            return customModelData > 1; // First slot is for basic model
        }

        return false;
    }

    public static boolean isAppliedShieldSkin(ItemStack itemStack) {
        ShieldGearType gearType = ShieldGearType.fromMaterial(itemStack.getType());

        if (gearType != null) { // Shield skin is weapon material unlike other cosmetics
            int customModelData = itemStack.getItemMeta().getCustomModelData();

            return customModelData > 1; // First slot is for basic model
        }

        return false;
    }
}
