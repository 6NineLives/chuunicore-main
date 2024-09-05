package io.github.lix3nn53.guardiansofadelia.interactables.chest;

import io.github.lix3nn53.guardiansofadelia.creatures.drops.MobDropGenerator;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawnerOpenWorld;
import io.github.lix3nn53.guardiansofadelia.items.GearLevel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LootChest extends MMSpawnerOpenWorld {

    private final LootChestTier lootChestTier;

    public LootChest(Location location, LootChestTier lootChestTier, long cooldownMin, long cooldownMax) {
        super(lootChestTier.getMobKey(), location, cooldownMin, cooldownMax);
        this.lootChestTier = lootChestTier;
    }

    public LootChestTier getLootChestTier() {
        return lootChestTier;
    }

    @Override
    public void onDeath() {
        super.onDeath();
        List<ItemStack> result = lootChestTier.getLoot();

        GearLevel gearLevel = this.lootChestTier.getRandomGearLevel();

        List<ItemStack> drops = MobDropGenerator.getDrops(gearLevel, false, true);

        result.addAll(drops);

        Location location = getLocation();
        World world = location.getWorld();
        for (ItemStack itemStack : result) {
            world.dropItemNaturally(location, itemStack);
        }
    }
}
