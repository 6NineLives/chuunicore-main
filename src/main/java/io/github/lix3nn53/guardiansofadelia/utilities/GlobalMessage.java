package io.github.lix3nn53.guardiansofadelia.utilities;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class GlobalMessage {

    private final String message;
    private final TextComponent component;

    public GlobalMessage(String message, TextComponent component) {
        this.message = message;
        this.component = component;
    }

    public void sendMessage(Player player) {
        if (message != null) {
            player.sendMessage(message);
        }
        if (component != null) {
            player.spigot().sendMessage(component);
        }
    }

    public void sendAll() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

        for (Player player : onlinePlayers) {
            sendMessage(player);
        }
    }
}
