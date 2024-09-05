package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticManager;
import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticRoom;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.Cosmetic;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CosmeticsConfiguration {

    private static final String filePath = ConfigManager.DATA_FOLDER + File.separator + "cosmetics";
    private static FileConfiguration configFile;

    static void createConfigs() {
        configFile = ConfigurationUtils.createConfig(filePath, "config.yml");
    }

    static void loadConfigs() {
        loadConfig();
        loadCosmetics();
    }

    private static void loadConfig() {
        ConfigurationSection room = configFile.getConfigurationSection("room");
        String worldString = room.getString("center.world");
        World world = Bukkit.getWorld(worldString);
        float x = (float) room.getDouble("center.x");
        float y = (float) room.getDouble("center.y");
        float z = (float) room.getDouble("center.z");
        float yaw = (float) room.getDouble("center.yaw");
        float pitch = (float) room.getDouble("center.pitch");
        Location center = new Location(world, x, y, z, yaw, pitch);
        CosmeticRoom.setLocation(center);

        worldString = room.getString("tp.world");
        world = Bukkit.getWorld(worldString);
        x = (float) room.getDouble("tp.x");
        y = (float) room.getDouble("tp.y");
        z = (float) room.getDouble("tp.z");
        yaw = (float) room.getDouble("tp.yaw");
        pitch = (float) room.getDouble("tp.pitch");
        Location tp = new Location(world, x, y, z, yaw, pitch);
        CosmeticRoom.setTpLocation(tp);
    }

    private static void loadCosmetics() {
        for (CosmeticType cosmeticType : CosmeticType.values()) {
            YamlConfiguration config = ConfigurationUtils.createConfig(filePath, cosmeticType.name() + ".yml");

            if (config == null) {
                continue;
            }

            int count = ConfigurationUtils.getChildComponentCount(config, "cosmetic");

            for (int i = 1; i <= count; i++) {
                ConfigurationSection section = config.getConfigurationSection("cosmetic" + i);

                Cosmetic load = cosmeticType.load(section);

                CosmeticManager.add(i - 1, load); // start at 0
            }
        }
    }
}
