package io.github.lix3nn53.guardiansofadelia.chat;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterMisc;
import io.github.lix3nn53.guardiansofadelia.utilities.hologram.FakeHologram;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class SpeechBubble {

    private static final HashMap<Player, FakeHologram> chatHolograms = new HashMap<>();

    public static void player(Player player, String message) {
        float height = (float) player.getHeight();
        Location location = player.getLocation().clone().add(0, height + 0.4, 0);

        int entityId = GuardiansOfAdelia.getEntityId();
        FakeHologram armorStand = new FakeHologram(entityId, location, message);
        armorStand.showNearby();

        final int period = 2;
        final int ticksLimit = 100;

        new BukkitRunnable() {

            int ticksPass = 0;

            @Override
            public void run() {
                if (ticksPass == ticksLimit) {
                    cancel();
                    armorStand.destroy();
                    chatHolograms.remove(player);
                } else {
                    if (ticksPass == 0) {
                        FakeHologram old = chatHolograms.get(player);
                        if (old != null) {
                            old.destroy();
                        }

                        chatHolograms.put(player, armorStand);
                    }

                    Location location = player.getLocation().clone().add(0, height + 0.4, 0);
                    armorStand.teleport(location);
                    ticksPass += period;
                }
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 0L, period);
    }

    public static void entity(Entity entity, String message, int durationTicks, float offsetY) {
        float height = (float) entity.getHeight();
        Location location = entity.getLocation().clone().add(0, height + 0.4 + offsetY, 0);

        int entityId = GuardiansOfAdelia.getEntityId();
        FakeHologram armorStand = new FakeHologram(entityId, location, message);
        armorStand.showNearby();

        new BukkitRunnable() {

            int ticksPass = 0;

            @Override
            public void run() {
                if (ticksPass == durationTicks) {
                    cancel();
                    armorStand.destroy();
                } else {
                    Location location = entity.getLocation().clone().add(0, height + 0.4 + offsetY, 0);
                    armorStand.teleport(location);
                    ticksPass += 2;
                }
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 0L, 2L);
    }

    public static FakeHologram entityNoFollow(Entity entity, String message, long durationTicks) {
        float height = (float) entity.getHeight();
        Location location = entity.getLocation().clone().add(0, height + 0.4, 0);

        int entityId = GuardiansOfAdelia.getEntityId();
        FakeHologram fakeHologram = new FakeHologram(entityId, location, message);

        fakeHologram.showNearby();

        new BukkitRunnable() {

            @Override
            public void run() {
                fakeHologram.destroy();
            }
        }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), durationTicks);

        return fakeHologram;
    }

    public static FakeHologram entityNoFollow(Entity entity, String message, long durationTicks, Player player) {
        float height = (float) entity.getHeight();
        Location location = entity.getLocation().clone().add(0, height + 0.4, 0);

        int entityId = GuardiansOfAdelia.getEntityId();
        FakeHologram fakeHologram = new FakeHologram(entityId, location, message);

        fakeHologram.show(player);

        new BukkitRunnable() {

            @Override
            public void run() {
                fakeHologram.destroy();
            }
        }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), durationTicks);

        return fakeHologram;
    }

    public static void countdown(Entity entity, String message, int durationTicks, float offsetY) {
        float height = (float) entity.getHeight();
        Location location = entity.getLocation().clone().add(0, height + 0.4 + offsetY, 0);

        int entityId = GuardiansOfAdelia.getEntityId();
        FakeHologram armorStand = new FakeHologram(entityId, location, message + ChatPalette.YELLOW + " in " + durationTicks);
        armorStand.showNearby();

        new BukkitRunnable() {

            int ticksPass = 0;

            @Override
            public void run() {
                if (ticksPass == durationTicks) {
                    cancel();
                    armorStand.destroy();
                } else {
                    armorStand.updateText(message + ChatPalette.YELLOW + " in " + (durationTicks - ticksPass));
                    Location location = entity.getLocation().clone().add(0, height + 0.4 + offsetY, 0);
                    armorStand.teleport(location);
                    ticksPass += 2;
                }
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 0L, 2L);
    }

    private static FakeHologram getHologram(Location location, String message) {
        int entityId = GuardiansOfAdelia.getEntityId();

        return new FakeHologram(entityId, location, CustomCharacterMisc.SPEECH_BUBBLE.toString() + ChatPalette.GRAY + message);
    }
}
