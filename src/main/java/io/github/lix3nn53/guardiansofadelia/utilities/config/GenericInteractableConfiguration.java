package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.MMSpawnerManager;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawnerOpenWorld;
import io.github.lix3nn53.guardiansofadelia.interactables.GenericInteractable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GenericInteractableConfiguration {

    private static final String filePath = ConfigManager.DATA_FOLDER + File.separator + "world" + File.separator + "interactables";
    private static HashMap<String, YamlConfiguration> allConfigsInFile;

    public static void createConfigs() {
        GenericInteractableConfiguration.allConfigsInFile = ConfigurationUtils.getAllConfigsInFile(filePath);
    }

    public static void loadConfigs() {
        load();
    }

    public static void writeConfigs() {
        write();
    }

    private static void load() {

        for (String fileName : allConfigsInFile.keySet()) {
            YamlConfiguration configFile = allConfigsInFile.get(fileName);

            int themeCount = ConfigurationUtils.getChildComponentCount(configFile, "interactable");

            for (int i = 1; i <= themeCount; i++) {
                ConfigurationSection section = configFile.getConfigurationSection("interactable" + i);
                String mobKey = section.getString("mobKey");
                String worldString = section.getString("world");
                World world = Bukkit.getWorld(worldString);
                float x = (float) section.getDouble("x");
                float y = (float) section.getDouble("y");
                float z = (float) section.getDouble("z");
                float yaw = (float) section.getDouble("yaw");
                float pitch = (float) section.getDouble("pitch");

                long cooldownMin = section.contains("cooldownMin") ? section.getLong("cooldownMin") : 20 * 60;
                long cooldownMax = section.contains("cooldownMax") ? section.getLong("cooldownMax") : 20 * 120;

                Location location = new Location(world, x, y, z, yaw, pitch);

                GenericInteractable genericInteractable = new GenericInteractable(Bukkit.getWorld(fileName), mobKey, location, cooldownMin, cooldownMax);

                MMSpawnerManager.addGlobalSpawner(genericInteractable);
            }
        }
    }

    private static void write() {
        allConfigsInFile.clear();

        HashMap<String, List<MMSpawnerOpenWorld>> chunkKeyToLootChests = MMSpawnerManager.getChunkKeyToSpawners();

        HashMap<String, Integer> fileNameToCount = new HashMap<>();
        for (String chunkKey : chunkKeyToLootChests.keySet()) {
            List<MMSpawnerOpenWorld> openWorldSpawners = chunkKeyToLootChests.get(chunkKey);

            for (MMSpawnerOpenWorld spawner : openWorldSpawners) {
                if (spawner instanceof GenericInteractable genericInteractable) {
                    String fileName = genericInteractable.getWorld().getName();
                    Location location = genericInteractable.getLocation();

                    int i = fileNameToCount.getOrDefault(fileName, 1);
                    YamlConfiguration configFile = allConfigsInFile.getOrDefault(fileName, new YamlConfiguration());

                    configFile.set("interactable" + i + ".mobKey", spawner.getMobKey());
                    configFile.set("interactable" + i + ".world", location.getWorld().getName());
                    configFile.set("interactable" + i + ".x", location.getX());
                    configFile.set("interactable" + i + ".y", location.getY());
                    configFile.set("interactable" + i + ".z", location.getZ());
                    configFile.set("interactable" + i + ".yaw", location.getYaw());
                    configFile.set("interactable" + i + ".pitch", location.getPitch());

                    configFile.set("interactable" + i + ".cooldownMin", spawner.getCooldownMin());
                    configFile.set("interactable" + i + ".cooldownMax", spawner.getCooldownMax());

                    allConfigsInFile.put(fileName, configFile);
                    fileNameToCount.put(fileName, i + 1);
                }
            }
        }

        for (String fileName : allConfigsInFile.keySet()) {
            YamlConfiguration configFile = allConfigsInFile.get(fileName);

            try {
                configFile.save(new File(filePath + File.separator + fileName + ".yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
