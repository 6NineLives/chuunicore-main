package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.TriggerComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.managers.TriggerListener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TookDamageTrigger extends TriggerComponent {

    public TookDamageTrigger(ConfigurationSection configurationSection) {
        super(!configurationSection.contains("addLore") ||
                configurationSection.getBoolean("addLore"), TookDamageTrigger.class.getName(), configurationSection);
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        if (targets.isEmpty()) return false;
        super.execute(caster, skillLevel, targets, castCounter, skillId);

        this.skillId = skillId;

        this.saveTargets = targets;

        for (LivingEntity target : targets) {
            if (target instanceof Player) {
                TriggerListener.add((Player) target, this, skillId);
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
    public boolean callback(Player player, LivingEntity attacker, int skillLevel, int castCounter) {
        ArrayList<LivingEntity> targets = new ArrayList<>();
        targets.add(attacker);

        return executeChildren(player, skillLevel, targets, castCounter, skillId);
    }
}
