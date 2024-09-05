package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.MechanicComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillRPGClassData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillTreeData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class ChangeSkillLevelMechanic extends MechanicComponent {

    private final int skillId; // index of skill to get skill level of
    private final int value; // value to use if index == - 1

    public ChangeSkillLevelMechanic(ConfigurationSection configurationSection) {
        super(!configurationSection.contains("addLore") || configurationSection.getBoolean("addLore"));

        if (!configurationSection.contains("skillId") && !configurationSection.contains("value")) {
            configLoadError("skillId or value");
        }

        if (configurationSection.contains("skillId")) {
            this.skillId = configurationSection.getInt("skillId");
        } else {
            skillId = -1;
        }

        if (configurationSection.contains("value")) {
            this.value = configurationSection.getInt("value");
        } else {
            value = 1;
        }
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillIndex) {
        if (targets.isEmpty()) return false;

        int newSkillLevel = value;
        if (skillId > -1) {
            if (caster instanceof Player) {
                Player player = (Player) caster;
                if (GuardianDataManager.hasGuardianData(player)) {
                    GuardianData guardianData = GuardianDataManager.getGuardianData(player);

                    if (guardianData.hasActiveCharacter()) {
                        RPGCharacter activeCharacter = guardianData.getActiveCharacter();

                        String rpgClassStr = activeCharacter.getRpgClassStr();
                        RPGClass aClass = RPGClassManager.getClass(rpgClassStr);
                        SkillTree skillTree = aClass.getSkillTree();
                        Skill skill = skillTree.getSkill(skillId);

                        RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();
                        SkillRPGClassData skillRPGClassData = rpgClassStats.getSkillRPGClassData();
                        SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();

                        newSkillLevel = skillTreeData.getCurrentSkillLevel(skill);
                        if (newSkillLevel <= 0) {
                            newSkillLevel = 1;
                        }
                    }
                }
            }
        }

        return executeChildren(caster, newSkillLevel, targets, castCounter, skillIndex);
    }

    @Override
    public List<String> getSkillLoreAdditions(String lang, List<String> additions, int skillLevel) {

        return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);
    }
}
