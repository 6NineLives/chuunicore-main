package io.github.lix3nn53.guardiansofadelia.utilities.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class EntityMetadataPacket extends AbstractPacket {

    private final boolean setInvisible;
    private final boolean setMarker;
    private final String text;

    public EntityMetadataPacket(int entityID, @NotNull String text, boolean setInvisible, boolean setMarker) {
        super(entityID, PacketType.Play.Server.ENTITY_METADATA);
        this.setInvisible = setInvisible;
        this.text = text;
        this.setMarker = setMarker;
    }

    public EntityMetadataPacket(int entityID) {
        super(entityID, PacketType.Play.Server.ENTITY_METADATA);
        this.setInvisible = true;
        this.text = "";
        this.setMarker = true;
    }

    @Override
    public @NotNull
    AbstractPacket load() {
        packetContainer.getIntegers().write(0, entityID);

        WrappedDataWatcher watcher = new WrappedDataWatcher();

        if (setInvisible) {
            WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
            watcher.setObject(visible, (byte) 0x20);
        }

        if (!text.equals("")) {
            Optional<?> opt = Optional
                    .of(WrappedChatComponent
                            .fromChatMessage(text)[0].getHandle());
            watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt);

            WrappedDataWatcher.WrappedDataWatcherObject nameVisible = new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class));
            watcher.setObject(nameVisible, true);
        }

        if (setMarker) {
            WrappedDataWatcher.WrappedDataWatcherObject visible = new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class));
            watcher.setObject(visible, (byte) 0x10); // marker
        }

        packetContainer.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
        return this;
    }
}