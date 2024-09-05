package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.MechanicComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.managers.GearMechanicSkillManager;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ShieldGearType;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class ShieldGearMechanic extends MechanicComponent {

    private final ShieldGearType shieldGearType;

    public ShieldGearMechanic(String rpgClass, ConfigurationSection configurationSection, int skillId) {
        super(!configurationSection.contains("addLore") || configurationSection.getBoolean("addLore"));

        if (!configurationSection.contains("shieldGearType")) {
            configLoadError("shieldGearType");
        }

        this.shieldGearType = ShieldGearType.valueOf(configurationSection.getString("shieldGearType"));

        GearMechanicSkillManager.register(rpgClass, shieldGearType, skillId);
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        if (targets.isEmpty()) return false;

        for (LivingEntity target : targets) {
            if (target instanceof Player player) {
                player.sendMessage(ChatPalette.YELLOW + "You have unlocked a new gear: " + ChatPalette.BLUE_LIGHT + shieldGearType.toString());
            }
        }

        return true;
    }

    @Override
    public List<String> getSkillLoreAdditions(String lang, List<String> additions, int skillLevel) {
        if (!this.addLore) return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);

        if (skillLevel == 0) {
            additions.add(ChatPalette.BLUE_LIGHT + "Learn using weapon: " + ChatPalette.YELLOW + shieldGearType);
        }

        return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);
    }
}
