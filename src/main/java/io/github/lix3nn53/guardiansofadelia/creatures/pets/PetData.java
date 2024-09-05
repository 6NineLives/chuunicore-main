package io.github.lix3nn53.guardiansofadelia.creatures.pets;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.SkillComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.config.SkillComponentLoader;
import io.github.lix3nn53.guardiansofadelia.items.GearLevel;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.Egg;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ItemTier;
import io.github.lix3nn53.guardiansofadelia.rpginventory.slots.EggSlot;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.utilities.config.ConfigurationUtils;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PetData {

    private final String key;

    private final int speed; // speed potion level modifier
    HashMap<Integer, Skill> skills = new HashMap<>();

    // Egg
    private final List<GearLevel> gearLevels; // lowest gear level is for requirement, others for item generation/drop
    private final int customModelData;
    private final ItemTier itemTier;

    public PetData(String key, ConfigurationSection configurationSection) {
        this.key = key;

        if (!configurationSection.contains("speed")) {
            configLoadError("speed");
        }
        if (!configurationSection.contains("gearLevel")) {
            configLoadError("gearLevel");
        }
        if (!configurationSection.contains("itemTier")) {
            configLoadError("itemTier");
        }
        if (!configurationSection.contains("customModelData")) {
            configLoadError("customModelData");
        }

        this.speed = configurationSection.getInt("speed");

        List<String> gearLevels = configurationSection.getStringList("gearLevels");
        this.gearLevels = new ArrayList<>();
        for (String gearLevel : gearLevels) {
            this.gearLevels.add(GearLevel.valueOf(gearLevel));
        }
        this.customModelData = configurationSection.getInt("customModelData");
        this.itemTier = ItemTier.valueOf(configurationSection.getString("itemTier"));

        int skillCount = ConfigurationUtils.getChildComponentCount(configurationSection, "skill");
        if (skillCount > 0) {
            for (int i = 1; i <= skillCount; i++) {
                ConfigurationSection skillSection = configurationSection.getConfigurationSection("skill" + i);
                int cooldown = skillSection.getInt("cooldown");
                ArrayList<Integer> cooldowns = new ArrayList<>();
                cooldowns.add(cooldown);

                List<String> description = skillSection.getStringList("description");

                Skill skill = new Skill(-1, "petskill", SkillType.NORMAL, 1, Material.IRON_HOE, 1, description,
                        new ArrayList<>(), new ArrayList<>(), cooldowns);

                SkillComponent triggerComponent = SkillComponentLoader.loadSection("pet", skillSection.getConfigurationSection("trigger"), -1);
                skill.addTrigger(triggerComponent);

                int triggerCount = ConfigurationUtils.getChildComponentCount(skillSection, "trigger");
                for (int t = 1; t <= triggerCount; t++) {
                    SkillComponent triggerComponentExtra = SkillComponentLoader.loadSection("pet", skillSection.getConfigurationSection("trigger" + t), -1);
                    skill.addTrigger(triggerComponentExtra);
                }

                int level = skillSection.getInt("level");
                this.skills.put(level, skill);
            }
        }
    }

    public Skill getSkill(int index) {
        for (int i = index; i > 0; i--) {
            if (skills.containsKey(i)) {
                return skills.get(i);
            }
        }

        return skills.get(1);
    }

    public int getSpeed() {
        return speed;
    }

    public HashMap<Integer, Skill> getSkills() {
        return skills;
    }

    public void configLoadError(String section) {
        GuardiansOfAdelia.getInstance().getLogger().info(ChatPalette.RED + "ERROR WHILE LOADING PET: ");
        GuardiansOfAdelia.getInstance().getLogger().info(ChatPalette.RED + "Section: " + section);
    }

    public List<GearLevel> getGearLevels() {
        return gearLevels;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public ItemTier getItemTier() {
        return itemTier;
    }

    public ItemStack getEgg(int petLevel) {
        Egg egg = new Egg(key, itemTier, EggSlot.EGG_MATERIAL, customModelData,
                gearLevels.get(0).getMinLevel(), petLevel, "");

        return egg.getItemStack();
    }

    public boolean isMythicMob() {
        Optional<MythicMob> mythicMob = MythicBukkit.inst().getMobManager().getMythicMob(key);
        if (!mythicMob.isPresent()) {
            GuardiansOfAdelia.getInstance().getLogger().info(ChatPalette.RED + "Eggs mythicMob null: " + key);

            return false;
        }
        String displayName = mythicMob.get().getDisplayName().get();
        GuardiansOfAdelia.getInstance().getLogger().info(ChatPalette.GREEN_DARK + "Eggs MM: " + key + "-" + displayName);

        return true;
    }
}
