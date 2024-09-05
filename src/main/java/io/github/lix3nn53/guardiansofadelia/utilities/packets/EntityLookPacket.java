package io.github.lix3nn53.guardiansofadelia.utilities.packets;

import com.comphenix.protocol.PacketType;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class EntityLookPacket extends AbstractPacket {

    private final Location location;

    public EntityLookPacket(int entityID, Location location) {
        super(entityID, PacketType.Play.Server.ENTITY_LOOK);
        this.location = location;
    }

    @Override
    public @NotNull
    AbstractPacket load() {
        packetContainer.getIntegers().write(0, entityID);
        packetContainer.getBytes().write(0, (byte) getCompressedAngle(location.getYaw()));
        packetContainer.getBytes().write(1, (byte) getCompressedAngle(location.getPitch()));

        return this;
    }

    private int getCompressedAngle(float value) {
        return (int) (value * 256.0F / 360.0F);
    }
}