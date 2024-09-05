package io.github.lix3nn53.guardiansofadelia.interactables;

import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawnerOpenWorld;
import org.bukkit.Location;
import org.bukkit.World;

public class GenericInteractable extends MMSpawnerOpenWorld {

    private final World world;

    public GenericInteractable(World world, String mobKey, Location location, long cooldownMin, long cooldownMax) {
        super(mobKey, location, cooldownMin, cooldownMax);
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

}
