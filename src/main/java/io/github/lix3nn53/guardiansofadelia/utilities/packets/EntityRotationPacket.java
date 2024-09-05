package io.github.lix3nn53.guardiansofadelia.utilities.packets;

import com.comphenix.protocol.PacketType;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class EntityRotationPacket extends AbstractPacket {

    private final Location location;

    public EntityRotationPacket(int entityID, Location location) {
        super(entityID, PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);
        this.location = location;
    }

    @Override
    public @NotNull
    AbstractPacket load() {
        packetContainer.getIntegers().write(0, entityID);
        packetContainer.getShorts().write(0, (short) 0);
        packetContainer.getShorts().write(1, (short) 0);
        packetContainer.getShorts().write(2, (short) 0);
        packetContainer.getBytes().write(0, (byte) getCompressedAngle(location.getYaw()));
        packetContainer.getBytes().write(1, (byte) getCompressedAngle(location.getPitch()));

        return this;
    }

    private int getCompressedAngle(float value) {
        return (int) (value * 256.0F / 360.0F);
    }
}