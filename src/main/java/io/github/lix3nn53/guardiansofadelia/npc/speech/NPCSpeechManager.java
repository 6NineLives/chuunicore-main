package io.github.lix3nn53.guardiansofadelia.npc.speech;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.chat.SpeechBubble;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterMisc;
import io.github.lix3nn53.guardiansofadelia.utilities.hologram.FakeHologram;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NPCSpeechManager {

    // STATE FOR ADD
    private static final int MAX_ORDER = 11; // included
    // Normal speech bubbles visible to everyone
    private final static HashMap<Integer, HashMap<Integer, List<String>>> orderToNpcNoToDialogue = new HashMap<>();
    private final static HashMap<Integer, FakeHologram> npcNoToActiveSpeech = new HashMap<>();
    // Special speech bubbles visible to specific players
    private final static List<Player> playersViewingSpecial = new ArrayList<>();
    private final static int DURATION_TICKS = 500;
    // QuestSpeech for skip
    private final static HashMap<Player, QuestSpeech> questSpeechHashMap = new HashMap<>();
    // RUNNER
    private static BukkitTask TASK;
    private static int ORDER_INDEX = 0;
    private static int currentOrder = 0;

    static {
        for (int i = 0; i <= MAX_ORDER; i++) {
            orderToNpcNoToDialogue.put(i, new HashMap<>());
        }
    }

    public static void startRunnable() {
        if (TASK != null) {
            TASK.cancel();
        }

        TASK = new BukkitRunnable() {

            @Override
            public void run() {
                HashMap<Integer, List<String>> npcNoToDialogue = orderToNpcNoToDialogue.get(ORDER_INDEX);

                for (int npcNo : npcNoToDialogue.keySet()) {
                    displayRandomSpeech(npcNo, npcNoToDialogue.get(npcNo));
                }

                ORDER_INDEX++;
                if (ORDER_INDEX >= orderToNpcNoToDialogue.size()) {
                    ORDER_INDEX = 0;
                }
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 200, 100);
    }

    public static void setNpcDialogues(int npcNo, List<String> dialogue) {
        if (dialogue.isEmpty()) {
            GuardiansOfAdelia.getInstance().getLogger().warning("NPC " + npcNo + " dialogue list is empty!");
            return;
        }

        HashMap<Integer, List<String>> npcNoToDialogue = orderToNpcNoToDialogue.get(currentOrder);

        npcNoToDialogue.put(npcNo, dialogue);
        orderToNpcNoToDialogue.put(currentOrder, npcNoToDialogue);

        currentOrder++;
        if (currentOrder > MAX_ORDER) {
            currentOrder = 0;
        }
    }

    private static void displayRandomSpeech(int npcNo, List<String> dialogues) {
        if (npcNoToActiveSpeech.containsKey(npcNo)) {
            FakeHologram armorStand = npcNoToActiveSpeech.get(npcNo);
            if (armorStand != null) {
                armorStand.destroy();
            }
            npcNoToActiveSpeech.remove(npcNo);
        }

        int randomIndex = (int) (Math.random() * dialogues.size());
        final String speech = CustomCharacterMisc.SPEECH_BUBBLE.toString() + ChatPalette.GRAY + dialogues.get(randomIndex);

        NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
        NPC npc = npcRegistry.getById(npcNo);
        if (npc == null) {
            GuardiansOfAdelia.getInstance().getLogger().warning("NPCSpeechManager: NPC with npcNo " + npcNo + " is null");
            return;
        }
        if (!npc.isSpawned()) {
            return;
        }
        Entity entity = npc.getEntity();

        FakeHologram armorStand = SpeechBubble.entityNoFollow(entity, speech, DURATION_TICKS);
        npcNoToActiveSpeech.put(npcNo, armorStand);

        // Remove the armor stand for players viewing the special speech
        for (Player player : playersViewingSpecial) {
            if (player.getWorld().equals(entity.getWorld())) {
                if (player.getLocation().distanceSquared(entity.getLocation()) <= 400) {
                    removeDefaultSpeechForPlayer(npcNo, player);
                }
            }
        }
    }

    public static void startQuestDialogue(Player player, int npcNo, List<String> messages) {
        if (playersViewingSpecial.contains(player)) {
            return;
        }

        NPCRegistry npcRegistry = CitizensAPI.getNPCRegistry();
        NPC npc = npcRegistry.getById(npcNo);
        if (npc == null) {
            GuardiansOfAdelia.getInstance().getLogger().warning("NPCSpeechManager: NPC with npcNo " + npcNo + " is null");
            return;
        }
        if (!npc.isSpawned()) {
            return;
        }
        Entity entity = npc.getEntity();

        QuestSpeech questSpeech = new QuestSpeech(player, entity, messages);
        questSpeechHashMap.put(player, questSpeech);
        questSpeech.startNextDialogue();

        playersViewingSpecial.add(player);

        removeDefaultSpeechForPlayer(npcNo, player);
    }

    public static void onQuestDialogueEnd(Player player) {
        playersViewingSpecial.remove(player);
        questSpeechHashMap.remove(player);
    }

    private static void removeDefaultSpeechForPlayer(int npcNo, Player player) {
        if (npcNoToActiveSpeech.containsKey(npcNo)) {
            FakeHologram armorStand = npcNoToActiveSpeech.get(npcNo);

            armorStand.hide(player);
        }
    }

    public static boolean isPlayerViewingSpecial(Player player) {
        return playersViewingSpecial.contains(player);
    }

    public static QuestSpeech getQuestSpeech(Player player) {
        return questSpeechHashMap.get(player);
    }
}
