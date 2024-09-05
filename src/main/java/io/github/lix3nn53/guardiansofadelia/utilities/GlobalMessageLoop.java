package io.github.lix3nn53.guardiansofadelia.utilities;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class GlobalMessageLoop {

    private static final List<GlobalMessage> globalMessages = new ArrayList<>();
    private static int currentIndex = 0;

    public static void addGlobalMessage(GlobalMessage globalMessage) {
        globalMessages.add(globalMessage);
    }

    public static void removeGlobalMessage(GlobalMessage globalMessage) {
        globalMessages.remove(globalMessage);
    }

    public static void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(GuardiansOfAdelia.getInstance(), () -> {
            if (globalMessages.size() == 0) {
                return;
            }

            if (currentIndex >= globalMessages.size()) {
                currentIndex = 0;
            }

            globalMessages.get(currentIndex).sendAll();
            currentIndex++;
        }, 1200, 6000);
    }
}