package io.github.lix3nn53.guardiansofadelia.utilities.hologram;

import io.github.lix3nn53.guardiansofadelia.utilities.packets.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class FakeHologram {

    protected static final int RANGE = 24;

    protected final int entityID;
    private Set<Player> viewing = new HashSet<>();
    protected String text;
    private final EntityDestroyPacket entityDestroyPacket;
    private final SpawnEntityLivingPacket spawnEntityLivingPacket;
    private final EntityMetadataPacket entityMetadataPacket;
    private Location location;

    public FakeHologram(int entityID, Location location, String text) {
        this.entityID = entityID;
        this.location = location;
        this.text = text;
        entityDestroyPacket = new EntityDestroyPacket(entityID);
        entityDestroyPacket.load();
        spawnEntityLivingPacket = new SpawnEntityLivingPacket(entityID, location);
        spawnEntityLivingPacket.load();
        entityMetadataPacket = new EntityMetadataPacket(entityID, text, true, true);
        entityMetadataPacket.load();
    }

    public void hide(@NotNull Player player) {
        entityDestroyPacket.send(player);
        viewing.remove(player);
    }

    public void show(@NotNull Player player) {
        spawnEntityLivingPacket.send(player);
        entityMetadataPacket.send(player);
        viewing.add(player);
    }

    public void showNearby() {
        location.getWorld().getNearbyEntities(location, RANGE, RANGE, RANGE).forEach(entity -> {
            if (entity instanceof Player player) {
                spawnEntityLivingPacket.send(player);
                entityMetadataPacket.send(player);
                viewing.add(player);
            }
        });
    }

    public void updateText(String text) {
        AbstractPacket load = new EntityMetadataPacket(entityID, text, true, true).load();

        for (Player player : viewing) {
            load.send(player);
        }

        this.text = text;
    }

    public void mount(@NotNull Player player, Player vehicle) {
        new PlayOutMountPacket(entityID, vehicle)
                .load()
                .send(player);
    }

    public void unmount(@NotNull Player player, Player vehicle) {
        new PlayOutMountPacket(-1, vehicle)
                .load()
                .send(player);
    }

    public void setHelmet(Player receiver, ItemStack itemStack) {
        new EntityEquipmentPacket(entityID, itemStack)
                .load()
                .send(receiver);
    }

    public void teleport(Location location) {
        AbstractPacket load = new EntityTeleportPacket(entityID, location).load();

        for (Player player : viewing) {
            load.send(player);
        }

        this.location = location;
    }

    public void look(Player receiver, Location location) {
        new EntityLookPacket(entityID, location)
                .load()
                .send(receiver);
    }

    public void rotate(Player receiver, Location location) {
        new EntityRotationPacket(entityID, location)
                .load()
                .send(receiver);
    }

    public void rotateHead(Player receiver, Location location) {
        new EntityHeadRotationPacket(entityID, location)
                .load()
                .send(receiver);
    }

    public void destroy() {
        if (viewing.isEmpty()) {
            return;
        }

        for (Player player : viewing) {
            entityDestroyPacket.send(player);
        }

        viewing.clear();
    }

    public boolean isViewing(Player player) {
        return viewing.contains(player);
    }

    public void setViewing(Set<Player> viewing) {
        // Send destroy packet to players that are in the old set but not in the new set
        for (Player player : this.viewing) {
            if (!viewing.contains(player)) {
                entityDestroyPacket.send(player);
            }
        }

        // Send spawn packet to players that are in the new set but not in the old set
        for (Player player : viewing) {
            if (!this.viewing.contains(player)) {
                spawnEntityLivingPacket.send(player);
                entityMetadataPacket.send(player);
            }
        }

        this.viewing = viewing;
    }
}
