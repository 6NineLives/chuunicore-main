package io.github.lix3nn53.guardiansofadelia.utilities.packets;

import com.comphenix.protocol.PacketType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class PlayOutMountPacket extends AbstractPacket {

    private final LivingEntity vehicle;

    public PlayOutMountPacket(int entityID, LivingEntity vehicle) {
        super(entityID, PacketType.Play.Server.MOUNT);
        this.vehicle = vehicle;
    }

    @Override
    public @NotNull
    AbstractPacket load() {
        if (entityID == -1) {
            packetContainer.getIntegers().write(0, vehicle.getEntityId());
            packetContainer.getIntegerArrays().write(0, new int[]{});
        } else {
            packetContainer.getIntegers().write(0, vehicle.getEntityId());
            packetContainer.getIntegerArrays().write(0, new int[]{entityID});
        }

        return this;
    }
}