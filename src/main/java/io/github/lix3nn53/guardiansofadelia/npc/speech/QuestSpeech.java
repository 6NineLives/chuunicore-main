package io.github.lix3nn53.guardiansofadelia.npc.speech;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.chat.SpeechBubble;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.managers.ActionBarInfoManager;
import io.github.lix3nn53.guardiansofadelia.menu.quest.GuiQuestList;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterMisc;
import io.github.lix3nn53.guardiansofadelia.utilities.hologram.FakeHologram;
import io.github.lix3nn53.guardiansofadelia.utilities.packets.EntityEquipmentPacket;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

public class QuestSpeech {

    private final Player player;
    private final Entity npc;
    private final List<String> messages;

    // State
    private int index = -1;
    private FakeHologram hologram;
    private BukkitTask toNext;

    public QuestSpeech(Player player, Entity npc, List<String> messages) {
        this.player = player;
        this.npc = npc;
        this.messages = messages;
    }

    public void startNextDialogue() {
        index++;
        if (index >= messages.size()) {
            NPCSpeechManager.onQuestDialogueEnd(player);

            // Open quest gui again
            if (GuardianDataManager.hasGuardianData(player)) {
                GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
                NPC byId = npcRegistry.getNPC(npc);
                GuiQuestList questGui = new GuiQuestList(byId, player, guardianData);
                questGui.openInventory(player);
            }

            ItemStack helmet = player.getEquipment().getHelmet();
            EntityEquipmentPacket entityEquipmentPacket = new EntityEquipmentPacket(player.getEntityId(), helmet);
            entityEquipmentPacket.load().send(player);

            return;
        }

        EntityEquipmentPacket entityEquipmentPacket = new EntityEquipmentPacket(player.getEntityId(), new ItemStack(Material.CARVED_PUMPKIN));
        entityEquipmentPacket.load().send(player);

        String message = CustomCharacterMisc.SPEECH_BUBBLE.toString() + ChatPalette.GRAY + messages.get(index);

        long duration = getDuration(message);
        hologram = SpeechBubble.entityNoFollow(npc, message, duration, player);
        // player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (duration + 10), 0));
        ActionBarInfoManager.customMessage(player, ChatPalette.YELLOW + "Sneak[shift] to skip dialogue", duration);

        toNext = new BukkitRunnable() {
            @Override
            public void run() {
                startNextDialogue();
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), duration);
    }

    public void skip() {
        player.sendMessage("debug: skip quest dialogue");
        toNext.cancel();
        hologram.destroy();
        startNextDialogue();
    }

    private void lookAtNPC() {
        Location start = player.getLocation();
        float height = (float) (npc.getHeight()) / 2f;
        Location targetLocation = npc.getLocation().add(0, height, 0);
        Vector vectorBetweenPoints = targetLocation.toVector().subtract(start.toVector());
        start.setDirection(vectorBetweenPoints);
        player.teleport(start);
    }

    private static long getDuration(String message) {
        return message.length() * 2L;
    }
}
