package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.utilities.GlobalMessage;
import io.github.lix3nn53.guardiansofadelia.utilities.GlobalMessageLoop;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class GlobalMessageConfigurations {

    private static final String filePath = ConfigManager.DATA_FOLDER.toString();
    private static FileConfiguration fileConfiguration;

    static void createConfig() {
        fileConfiguration = ConfigurationUtils.createConfig(filePath, "globalMessages.yml");
    }

    static void loadConfig() {
        int count = ConfigurationUtils.getChildComponentCount(fileConfiguration, "message");

        for (int i = 1; i <= count; i++) {
            ConfigurationSection current = fileConfiguration.getConfigurationSection("message" + i);

            String normal = current.getString("text");
            if (normal != null) {
                normal = ChatColor.translateAlternateColorCodes('&', normal);
            }
            TextComponent message = null;

            if (current.contains("component")) {
                ConfigurationSection component = current.getConfigurationSection("component");

                String text = component.getString("text");
                if (text != null) {
                    text = ChatColor.translateAlternateColorCodes('&', text);
                }

                message = new TextComponent(text);

                String open_url = component.contains("open_url") ? component.getString("open_url") : null;
                if (open_url != null) {
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, open_url));
                }

                String hover = component.contains("hover") ? component.getString("hover") : null;
                if (hover != null) {
                    hover = ChatColor.translateAlternateColorCodes('&', hover);
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
                }
            }

            GlobalMessage globalMessage = new GlobalMessage(normal, message);
            GlobalMessageLoop.addGlobalMessage(globalMessage);
        }
    }
}
