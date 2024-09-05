package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetData;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetDataManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class PetConfigurations {

    private static final String filePath = ConfigManager.DATA_FOLDER + File.separator + "pets";
    private static HashMap<String, YamlConfiguration> fileConfigurations;

    public static void createConfigs() {
        fileConfigurations = ConfigurationUtils.getAllConfigsInFile(filePath);
    }

    public static void loadConfigs() {
        load();
    }

    private static void load() {
        for (String key : fileConfigurations.keySet()) {
            YamlConfiguration fileConfiguration = fileConfigurations.get(key);

            PetData petData = new PetData(key, fileConfiguration);
            PetDataManager.put(key, petData);
        }
    }
}
