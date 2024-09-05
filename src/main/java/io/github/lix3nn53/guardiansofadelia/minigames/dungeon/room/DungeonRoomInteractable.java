package io.github.lix3nn53.guardiansofadelia.minigames.dungeon.room;

import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawner;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class DungeonRoomInteractable extends MMSpawner {

    private final Vector offset;
    private final float pitch;
    private float yaw;

    public DungeonRoomInteractable(String mobKey, Vector offset, float yaw, float pitch) {
        super(mobKey, null);
        this.offset = offset;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void spawn() {
        throw new IllegalArgumentException("This method is not implemented for DungeonRoomExplosiveBarrel");
    }

    public boolean spawnWithChance(Location dungeonStart, int roomNo, int roomCount) {
        float random = (float) Math.random();

        float chance = ((float) roomNo) / roomCount;

        if (chance < random) {
            return false;
        }

        spawn(dungeonStart);

        return true;
    }

    public void spawn(Location dungeonStart) {
        Location add = dungeonStart.clone().add(offset);

        add.setYaw(yaw);
        add.setPitch(pitch);

        super.setLocation(add);

        super.spawn();
    }

    public Vector getOffset() {
        return offset;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    @Override
    public void rotate() {
        super.rotate();

        this.yaw += 45;
    }
}
