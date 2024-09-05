package io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MMSpawnerOpenWorld extends MMSpawner {

    private final long cooldownMin;
    private final long cooldownMax;

    private BukkitTask cooldownTask;

    public MMSpawnerOpenWorld(String mobKey, Location location, long cooldownMin, long cooldownMax) {
        super(mobKey, location);
        this.cooldownMin = cooldownMin;
        this.cooldownMax = cooldownMax;
    }

    @Override
    public void spawn() {
        if (cooldownTask != null && !cooldownTask.isCancelled()) return;

        super.spawn();
    }

    @Override
    public void onDeath() {
        super.onDeath();

        final long finalCooldown = (long) (cooldownMin + (Math.random() * (cooldownMax - cooldownMin)));

        if (cooldownTask != null) {
            cooldownTask.cancel();
        }

        cooldownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (getLocation().getChunk().isLoaded()) {
                    cancel();
                    spawn();
                }
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), finalCooldown);
    }

    public long getCooldownMin() {
        return cooldownMin;
    }

    public long getCooldownMax() {
        return cooldownMax;
    }
}
