package io.github.lix3nn53.guardiansofadelia.utilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.Particle;

public class MyPacketListeners {

    public static void addPacketListeners() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(GuardiansOfAdelia.getInstance(), ListenerPriority.NORMAL,
                PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                if (event.getPacketType() != PacketType.Play.Server.WORLD_PARTICLES)
                    return;

                if (packet.getNewParticles().read(0).getParticle() == Particle.DAMAGE_INDICATOR)
                    event.setCancelled(true);
            }
        });
    }
}
