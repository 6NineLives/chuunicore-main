package io.github.lix3nn53.guardiansofadelia.cosmetic;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticColor;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticSlot;
import org.bukkit.Location;

import java.util.HashMap;

public class CosmeticRoomData {

    private final Location backLocation;
    private final HashMap<CosmeticSlot, CosmeticRecord> cosmetics = new HashMap<>();

    public CosmeticRoomData(Location backLocation) {
        this.backLocation = backLocation;
    }

    public Location getBackLocation() {
        return backLocation;
    }

    public void setCosmetic(CosmeticSlot slot, int cosmeticId, CosmeticColor color, int tintIndex) {
        GuardiansOfAdelia.getInstance().getLogger().info("Setting cosmetic " + slot + " to " + cosmeticId + " with color " + color + " and tint " + tintIndex);
        CosmeticRecord record = new CosmeticRecord(cosmeticId, color, tintIndex);
        cosmetics.put(slot, record);
    }

    public HashMap<CosmeticSlot, CosmeticRecord> getCosmetics() {
        return cosmetics;
    }
}
