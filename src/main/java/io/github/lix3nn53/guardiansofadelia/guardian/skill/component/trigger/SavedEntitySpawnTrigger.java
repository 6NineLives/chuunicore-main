package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.TriggerComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.managers.TriggerListener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SavedEntitySpawnTrigger extends TriggerComponent {

    public SavedEntitySpawnTrigger(ConfigurationSection configurationSection) {
        super(!configurationSection.contains("addLore") ||
                configurationSection.getBoolean("addLore"), SavedEntitySpawnTrigger.class.getName(), configurationSection);
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        if (targets.isEmpty()) return false;

        this.skillId = skillId;

        SavedEntitySpawnTrigger savedEntitySpawnTrigger = this;

        for (LivingEntity target : targets) {
            if (target instanceof Player) {
                TriggerListener.add((Player) target, savedEntitySpawnTrigger, skillId);
            }
        }

        return true;
    }

    @Override
    public List<String> getSkillLoreAdditions(String lang, List<String> additions, int skillLevel) {
        return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);
    }

    /**
     * The callback when player lands that applies child components
     */
    public boolean callback(Player player, LivingEntity created, int skillLevel, int castCounter) {
        List<LivingEntity> targets = new ArrayList<>();
        targets.add(created);

        return executeChildren(player, skillLevel, targets, castCounter, skillId);
    }
}
