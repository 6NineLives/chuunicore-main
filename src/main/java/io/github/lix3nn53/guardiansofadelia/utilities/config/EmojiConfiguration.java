package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.chat.EmojiManager;
import org.bukkit.configuration.file.FileConfiguration;

public class EmojiConfiguration {

    private static final String filePath = ConfigManager.DATA_FOLDER.toString();
    private static FileConfiguration configFile;

    static void createConfigs() {
        configFile = ConfigurationUtils.createConfig(filePath, "emojis.yml");
    }

    static void loadConfigs() {
        loadDatabaseConfig();
    }

    private static void loadDatabaseConfig() {
        int emojis = ConfigurationUtils.getChildComponentCount(configFile, "emoji");

        for (int i = 1; i <= emojis; i++) {
            String key = configFile.getString("emoji" + i + ".key");
            String icon = configFile.getString("emoji" + i + ".icon");

            EmojiManager.addEmoji(key, icon);
        }
    }
}
