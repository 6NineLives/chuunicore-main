package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.npc.speech.NPCSpeechManager;
import io.github.lix3nn53.guardiansofadelia.npc.speech.QuestSpeech;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class MyPlayerToggleSneakEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(PlayerToggleSneakEvent event) {
        if (!event.isSneaking()) {
            return;
        }

        QuestSpeech questSpeech = NPCSpeechManager.getQuestSpeech(event.getPlayer());
        if (questSpeech != null) {
            questSpeech.skip();
        }
    }
}
