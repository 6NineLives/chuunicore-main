package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.npc.merchant.MerchantManager;
import io.github.lix3nn53.guardiansofadelia.npc.merchant.MerchantMenu;
import io.github.lix3nn53.guardiansofadelia.npc.merchant.MerchantType;
import io.github.lix3nn53.guardiansofadelia.npc.speech.NPCSpeechManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MerchantConfiguration {

    private static final String filePath = ConfigManager.DATA_FOLDER + File.separator + "world";
    private static FileConfiguration merchantConfig;
    private static FileConfiguration merchantDialoguesConfig;

    static void createConfigs() {
        merchantConfig = ConfigurationUtils.createConfig(filePath, "merchants.yml");
        merchantDialoguesConfig = ConfigurationUtils.createConfig(filePath, "merchantDialogues.yml");
    }

    static void loadConfigs() {
        load();
    }

    private static void load() {
        int count = ConfigurationUtils.getChildComponentCount(merchantConfig, "merchant");

        HashMap<MerchantType, List<String>> merchantDialogues = new HashMap<>();

        for (MerchantType merchantType : MerchantType.values()) {
            merchantDialogues.put(merchantType, merchantDialoguesConfig.getStringList(merchantType.name()));
        }

        for (int i = 1; i <= count; i++) {
            ConfigurationSection section = merchantConfig.getConfigurationSection("merchant" + i);
            int npcId = section.getInt("npc");
            MerchantType type = MerchantType.valueOf(section.getString("type").toUpperCase());
            int level = section.getInt("level");

            MerchantMenu merchantMenu = new MerchantMenu(type, level, npcId);

            MerchantManager.setMerchant(npcId, merchantMenu);

            if (merchantDialogues.containsKey(type)) {
                NPCSpeechManager.setNpcDialogues(npcId, merchantDialogues.get(type));
            }
        }
    }
}
