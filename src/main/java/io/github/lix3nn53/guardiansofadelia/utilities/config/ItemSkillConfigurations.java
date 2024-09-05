package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.SkillComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.config.SkillComponentLoader;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.WeaponGearType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.WeaponGearTypeSkillManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemSkillConfigurations {

    private static final String filePath = ConfigManager.DATA_FOLDER + File.separator + "items" + File.separator + "skillOnLeft";
    private static HashMap<String, YamlConfiguration> fileConfigurations;

    public static void createConfigs() {
        fileConfigurations = ConfigurationUtils.getAllConfigsInFile(filePath);
    }

    public static void loadConfigs() {
        load();
    }

    private static void load() {
        for (String key : fileConfigurations.keySet()) {
            WeaponGearType weaponGearType = WeaponGearType.valueOf(key);

            YamlConfiguration configurationSection = fileConfigurations.get(key);

            ConfigurationSection skillSection = configurationSection.getConfigurationSection("skill");
            int cooldown = skillSection.getInt("cooldown");
            ArrayList<Integer> cooldowns = new ArrayList<>();
            cooldowns.add(cooldown);

            List<String> description = skillSection.getStringList("description");

            Skill skill = new Skill(-1, "skillOnLeft", SkillType.NORMAL, 1, Material.IRON_HOE, 1, description,
                    new ArrayList<>(), new ArrayList<>(), cooldowns);

            SkillComponent triggerComponent = SkillComponentLoader.loadSection("skillOnLeft", skillSection.getConfigurationSection("trigger"), -1);
            skill.addTrigger(triggerComponent);

            int triggerCount = ConfigurationUtils.getChildComponentCount(skillSection, "trigger");
            for (int t = 1; t <= triggerCount; t++) {
                SkillComponent triggerComponentExtra = SkillComponentLoader.loadSection("skillOnLeft", skillSection.getConfigurationSection("trigger" + t), -1);
                skill.addTrigger(triggerComponentExtra);
            }

            WeaponGearTypeSkillManager.register(weaponGearType, skill);
        }
    }
}
