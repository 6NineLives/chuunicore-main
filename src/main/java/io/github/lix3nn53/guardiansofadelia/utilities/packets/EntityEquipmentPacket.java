package io.github.lix3nn53.guardiansofadelia.utilities.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EntityEquipmentPacket extends AbstractPacket {

    private final ItemStack helmet;

    public EntityEquipmentPacket(int entityID, ItemStack helmet) {
        super(entityID, PacketType.Play.Server.ENTITY_EQUIPMENT);
        this.helmet = helmet;
    }

    @Override
    public @NotNull
    AbstractPacket load() {
        packetContainer.getIntegers().write(0, entityID);
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairs = new ArrayList<>();

        pairs.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, helmet));
        /*
        ItemStack itemStack = new ItemStack(Material.AIR);
        pairs.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, itemStack));
        pairs.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, itemStack));
        pairs.add(new Pair<>(EnumWrappers.ItemSlot.FEET, itemStack));
        pairs.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, itemStack));
        pairs.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, itemStack));*/

        packetContainer.getSlotStackPairLists().writeSafely(0, pairs);

        return this;
    }
}