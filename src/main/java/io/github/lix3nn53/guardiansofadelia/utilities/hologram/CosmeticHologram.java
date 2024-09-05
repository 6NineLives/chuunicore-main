package io.github.lix3nn53.guardiansofadelia.utilities.hologram;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Set;
import java.util.stream.Collectors;

public class CosmeticHologram extends FakeHologram {

    private BukkitTask rotationTask;
    private final ItemStack helmet;

    public CosmeticHologram(int entityID, Location location, ItemStack helmet) {
        super(entityID, location, "");

        this.helmet = helmet;
    }

    public void start(Player source) {
        show(source);
        mount(source, source);
        setHelmet(source, helmet);

        source.getNearbyEntities(RANGE, RANGE, RANGE).forEach(entity -> {
            if (entity instanceof Player player) {
                show(player);
                mount(player, source);
                setHelmet(player, helmet);
            }
        });

        startRotationRunnable(source);
    }

    private void startRotationRunnable(Player source) {
        if (rotationTask != null) {
            rotationTask.cancel();
        }

        this.rotationTask = new BukkitRunnable() {

            @Override
            public void run() {
                if (!source.isOnline()) {
                    cancel();
                    return;
                }

                Location location = source.getLocation();

                look(source, location);
                rotateHead(source, location);

                Set<Player> nearbyPlayers = source.getNearbyEntities(RANGE, RANGE, RANGE).stream()
                        .filter(entity -> entity instanceof Player)
                        .map(entity -> (Player) entity)
                        .collect(Collectors.toSet());

                for (Player player : nearbyPlayers) {
                    if (!isViewing(player)) {
                        show(player);
                        mount(player, source);
                        setHelmet(player, helmet);
                    }
                    look(player, location);
                    rotateHead(player, location);
                }

                nearbyPlayers.add(source);

                setViewing(nearbyPlayers);
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 2L, 10L);
    }

    @Override
    public void destroy() {
        super.destroy();

        if (rotationTask != null) {
            rotationTask.cancel();
            rotationTask = null;
        }
    }
}
