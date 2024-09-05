package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.MMSpawnerManager;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawnerOpenWorld;
import io.github.lix3nn53.guardiansofadelia.interactables.chest.LootChest;
import io.github.lix3nn53.guardiansofadelia.interactables.chest.LootChestTier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LootChestConfiguration {

    private static FileConfiguration lootChestsConfig;
    private static final String filePath = ConfigManager.DATA_FOLDER + File.separator + "rewards";

    static void createConfigs() {
        lootChestsConfig = ConfigurationUtils.createConfig(filePath, "lootChests.yml");
    }

    static void loadConfigs() {
        loadLootChests();
    }

    static void writeConfigs() {
        writeLootChests("lootChests.yml");
    }

    private static void loadLootChests() {
        int themeCount = ConfigurationUtils.getChildComponentCount(lootChestsConfig, "chest");

        for (int i = 1; i <= themeCount; i++) {
            ConfigurationSection section = lootChestsConfig.getConfigurationSection("chest" + i);
            String worldString = section.getString("world");
            World world = Bukkit.getWorld(worldString);
            float x = (float) section.getDouble("x");
            float y = (float) section.getDouble("y");
            float z = (float) section.getDouble("z");
            float yaw = (float) section.getDouble("yaw");
            float pitch = (float) section.getDouble("pitch");
            Location location = new Location(world, x, y, z, yaw, pitch);
            String tierStr = section.getString("tier");
            LootChestTier lootChestTier = LootChestTier.valueOf(tierStr);

            long cooldownMin = section.contains("cooldownMin") ? section.getLong("cooldownMin") : 20 * 180;
            long cooldownMax = section.contains("cooldownMax") ? section.getLong("cooldownMax") : 20 * 600;

            LootChest lootChest = new LootChest(location, lootChestTier, cooldownMin, cooldownMax);

            MMSpawnerManager.addGlobalSpawner(lootChest);
        }
    }

    private static void writeLootChests(String fileName) {
        HashMap<String, List<MMSpawnerOpenWorld>> chunkKeyToLootChests = MMSpawnerManager.getChunkKeyToSpawners();

        int i = 1;
        for (String chunkKey : chunkKeyToLootChests.keySet()) {
            List<MMSpawnerOpenWorld> lootChests = chunkKeyToLootChests.get(chunkKey);

            for (MMSpawnerOpenWorld spawner : lootChests) {
                if (spawner instanceof LootChest lootChest) {
                    Location location = lootChest.getLocation();
                    LootChestTier lootChestTier = lootChest.getLootChestTier();

                    lootChestsConfig.set("chest" + i + ".world", location.getWorld().getName());
                    lootChestsConfig.set("chest" + i + ".x", location.getX());
                    lootChestsConfig.set("chest" + i + ".y", location.getY());
                    lootChestsConfig.set("chest" + i + ".z", location.getZ());
                    lootChestsConfig.set("chest" + i + ".yaw", location.getYaw());
                    lootChestsConfig.set("chest" + i + ".pitch", location.getPitch());
                    lootChestsConfig.set("chest" + i + ".tier", lootChestTier.name());

                    i++;
                }
            }
        }

        try {
            lootChestsConfig.save(filePath + File.separator + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
