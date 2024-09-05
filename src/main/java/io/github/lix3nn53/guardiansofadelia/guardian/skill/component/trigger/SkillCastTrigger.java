package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger;

import io.github.lix3nn53.guardiansofadelia.commands.admin.CommandAdmin;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.TriggerComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.managers.TriggerListener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkillCastTrigger extends TriggerComponent {

    public SkillCastTrigger(ConfigurationSection configurationSection) {
        super(!configurationSection.contains("addLore") ||
                configurationSection.getBoolean("addLore"), SkillCastTrigger.class.getName(), configurationSection);
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        if (targets.isEmpty()) return false;

        this.skillId = skillId;

        SkillCastTrigger skillCastTrigger = this;

        for (LivingEntity target : targets) {
            if (target instanceof Player playerTarget) {
                TriggerListener.add(playerTarget, skillCastTrigger, skillId);
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
    public boolean callback(Player caster, int skillLevel, int castCounter) {
        ArrayList<LivingEntity> targets = new ArrayList<>();
        targets.add(caster);
        if (CommandAdmin.DEBUG_MODE) caster.sendMessage("skillCast callback");

        return executeChildren(caster, skillLevel, targets, castCounter, skillId);
    }
}
