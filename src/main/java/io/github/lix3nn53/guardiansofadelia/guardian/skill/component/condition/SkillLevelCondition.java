package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.condition;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.ConditionComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillRPGClassData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillTreeData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SkillLevelCondition extends ConditionComponent {

    private final int skillId;
    private final int minValue;
    private final int maxValue;

    public SkillLevelCondition(ConfigurationSection configurationSection) {
        super(!configurationSection.contains("addLore") || configurationSection.getBoolean("addLore"));

        if (!configurationSection.contains("skillId")) {
            configLoadError("skillId");
        }

        if (!configurationSection.contains("minValue")) {
            configLoadError("minValue");
        }

        if (!configurationSection.contains("maxValue")) {
            configLoadError("maxValue");
        }

        this.skillId = configurationSection.getInt("skillId");
        this.minValue = configurationSection.getInt("minValue");
        this.maxValue = configurationSection.getInt("maxValue");
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillIndex) {
        if (targets.isEmpty()) return false;

        boolean success = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player)) continue;
            Player player = (Player) target;

            if (GuardianDataManager.hasGuardianData(player)) {
                GuardianData guardianData = GuardianDataManager.getGuardianData(player);

                if (guardianData.hasActiveCharacter()) {
                    RPGCharacter activeCharacter = guardianData.getActiveCharacter();

                    RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();
                    SkillRPGClassData skillRPGClassData = rpgClassStats.getSkillRPGClassData();
                    SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();

                    String rpgClassStr = activeCharacter.getRpgClassStr();
                    RPGClass aClass = RPGClassManager.getClass(rpgClassStr);
                    SkillTree skillTree = aClass.getSkillTree();

                    int invested = skillTreeData.getInvestedSkillPoints(skillId);
                    Skill skill = skillTree.getSkill(skillId);
                    int value = skill.getCurrentSkillLevel(invested);

                    if (value >= minValue && value <= maxValue) {
                        success = executeChildren(caster, skillLevel, Collections.singletonList(target), castCounter, skillIndex) || success;
                    }
                }
            }
        }

        return success;
    }

    @Override
    public List<String> getSkillLoreAdditions(String lang, List<String> additions, int skillLevel) {
        return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);
    }
}
