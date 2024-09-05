package io.github.lix3nn53.guardiansofadelia.utilities.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class PlayerInfoPacket extends AbstractPacket {

    private final Player player;

    public PlayerInfoPacket(Player player) {
        super(-1, PacketType.Play.Server.PLAYER_INFO);
        this.player = player;
    }

    @Override
    public @NotNull
    AbstractPacket load() {
        packetContainer.getIntegers().write(0, 1);
        packetContainer.getIntegers().write(0, 1);
        packetContainer.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        WrappedGameProfile wrappedGameProfile = new WrappedGameProfile(player.getUniqueId(), player.getDisplayName());
        packetContainer.getPlayerInfoDataLists().write(0, Collections.singletonList(
                new PlayerInfoData(wrappedGameProfile, 0, EnumWrappers.NativeGameMode.ADVENTURE,
                        WrappedChatComponent.fromText(player.getDisplayName()))));

        return this;
    }
}