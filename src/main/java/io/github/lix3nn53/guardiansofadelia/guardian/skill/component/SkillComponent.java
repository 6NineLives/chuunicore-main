package io.github.lix3nn53.guardiansofadelia.guardian.skill.component;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class SkillComponent {

    private final List<SkillComponent> children = new ArrayList<>();
    public final boolean addLore;

    protected SkillComponent(boolean addLore) {
        this.addLore = addLore;
    }

    public abstract boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId);

    /**
     * Use this in #execute method of SkillComponents
     *
     * @param caster
     * @param skillLevel
     * @param targets
     * @param skillId
     * @return
     */
    protected boolean executeChildren(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        if (targets.isEmpty()) return false;

        boolean worked = false;
        for (SkillComponent child : children) {
            boolean passed = child.execute(caster, skillLevel, targets, castCounter, skillId);
            worked = passed || worked;
        }
        return worked;
    }

    public void addChildren(SkillComponent child) {
        children.add(child);
    }

    public abstract List<String> getSkillLoreAdditions(String lang, List<String> additions, int skillLevel);

    public List<String> getSkillLoreAdditionsOfChildren(String lang, List<String> additions, int skillLevel) {
        if (children.isEmpty()) return additions;

        for (SkillComponent child : children) {
            additions = child.getSkillLoreAdditions(lang, additions, skillLevel);
        }

        return additions;
    }

    public void configLoadError(String section) {
        GuardiansOfAdelia.getInstance().getLogger().info(ChatPalette.RED + "ERROR WHILE LOADING SKILL: ");
        GuardiansOfAdelia.getInstance().getLogger().info(ChatPalette.RED + "Section: " + section);
    }
}
