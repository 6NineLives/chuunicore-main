package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger;

import io.github.lix3nn53.guardiansofadelia.commands.admin.CommandAdmin;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.TriggerComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.managers.TriggerListener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NormalAttackTrigger extends TriggerComponent {

    private final boolean melee;
    private final boolean projectile;

    public NormalAttackTrigger(ConfigurationSection configurationSection) {
        super(!configurationSection.contains("addLore") || configurationSection.getBoolean("addLore"),
                NormalAttackTrigger.class.getName(), configurationSection);

        this.melee = configurationSection.contains("melee") && configurationSection.getBoolean("melee");
        this.projectile = configurationSection.contains("projectile") && configurationSection.getBoolean("projectile");
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        if (targets.isEmpty()) return false;

        this.skillId = skillId;

        NormalAttackTrigger normalAttackTrigger = this;

        for (LivingEntity target : targets) {
            if (target instanceof Player) {
                TriggerListener.add((Player) target, normalAttackTrigger, skillId);
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
    public boolean callback(Player attacker, LivingEntity target, boolean isProjectile, int skillLevel, int castCounter) {
        if (CommandAdmin.DEBUG_MODE) attacker.sendMessage("NormalAttackTrigger callback, skillIndex: " + skillId);
        if (!(this.melee && this.projectile)) {
            if (this.melee && isProjectile) {
                return false;
            } else if (this.projectile && !isProjectile) {
                return false;
            }
        }

        ArrayList<LivingEntity> targets = new ArrayList<>();
        targets.add(target);

        return executeChildren(attacker, skillLevel, targets, castCounter, skillId);
    }
}
