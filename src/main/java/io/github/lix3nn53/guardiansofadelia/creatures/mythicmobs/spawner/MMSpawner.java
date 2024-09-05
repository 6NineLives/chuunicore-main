package io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.MMSpawnerManager;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public abstract class MMSpawner {

    private final String mobKey;
    private Location location;
    private LivingEntity spawned;

    public MMSpawner(String mobKey, Location location) {
        this.mobKey = mobKey;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void spawn() {
        if (spawned != null) {
            spawned.remove();
        }

        MMSpawner mmSpawner = this;

        BukkitAPIHelper apiHelper = MythicBukkit.inst().getAPIHelper();
        Entity entity = null;
        try {
            entity = apiHelper.spawnMythicMob(mobKey, location, 1);
        } catch (InvalidMobTypeException e) {
            GuardiansOfAdelia.getInstance().getLogger().info("LootChest mythicmob code error: " + mobKey);
            e.printStackTrace();
        }
        if (entity == null) return;
        if (!(entity instanceof LivingEntity chest)) {
            GuardiansOfAdelia.getInstance().getLogger().info("LootChest is not LivingEntity, code: " + mobKey);
            return;
        }

        spawned = chest;

        MMSpawnerManager.onSpawn(mmSpawner);
    }

    public LivingEntity getSpawned() {
        return spawned;
    }

    public void despawn() {
        onDespawn();

        if (spawned != null) {
            spawned.remove();
            spawned = null;
        }
    }

    private void onDespawn() {
        if (spawned != null) {
            MMSpawnerManager.onDespawn(spawned);
        }
    }

    public void onDeath() {
        onDespawn();
    }

    public void rotate() {
        float yaw = location.getYaw();
        yaw += 45;
        location.setYaw(yaw);

        if (spawned != null && spawned.isValid()) {
            spawned.teleport(location);
        }
    }

    public String getMobKey() {
        return mobKey;
    }
}
