package io.github.lix3nn53.guardiansofadelia.utilities.packets;

import com.comphenix.protocol.PacketType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SpawnEntityLivingPacket extends AbstractPacket {

    private final Location location;

    public SpawnEntityLivingPacket(int entityID, @NotNull Location location) {
        super(entityID, PacketType.Play.Server.SPAWN_ENTITY);
        this.location = location;
    }

    @Override
    public @NotNull
    AbstractPacket load() {
        final int entityType = 2;
        final int extraData = 1;
        packetContainer.getIntegers().write(0, this.entityID);
        packetContainer.getIntegers().write(1, entityType);
        packetContainer.getIntegers().write(2, extraData);

        packetContainer.getUUIDs().write(0, UUID.randomUUID());

        packetContainer.getDoubles().write(0, this.location.getX());
        packetContainer.getDoubles().write(1, this.location.getY());
        packetContainer.getDoubles().write(2, this.location.getZ());

        packetContainer.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);

        return this;
    }
}