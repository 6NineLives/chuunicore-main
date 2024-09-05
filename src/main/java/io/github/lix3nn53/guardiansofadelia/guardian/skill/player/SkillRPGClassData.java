package io.github.lix3nn53.guardiansofadelia.guardian.skill.player;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterExperienceManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillBar;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger.InitializeTrigger;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.managers.TriggerListener;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTreeDirection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SkillRPGClassData {

    private final SkillTreeData skillTreeData;
    private final SkillBarData skillBarData;

    public SkillRPGClassData(SkillTreeData skillTreeData, SkillBarData skillBarData) {
        this.skillTreeData = skillTreeData;
        this.skillBarData = skillBarData;
    }

    public SkillRPGClassData() {
        this.skillTreeData = new SkillTreeData();
        this.skillBarData = new SkillBarData();
    }

    public SkillTreeData getSkillTreeData() {
        return skillTreeData;
    }

    public SkillBarData getSkillBarData() {
        return skillBarData;
    }

    /**
     * @param skillId global if of skill
     */
    public boolean upgradeSkill(Player player, int skillId, SkillTree skillTree, RPGClassStats rpgClassStats, SkillBar skillBar, String lang) {
        if (!skillTree.containsSkill(skillId)) return false;

        Skill skill = skillTree.getSkill(skillId);

        int parentId = skill.getParentId();
        if (parentId > 0) {
            int parentInvested = skillTreeData.getInvestedSkillPoints(parentId);
            if (parentInvested < 1) {
                player.sendMessage("You need to invest at least 1 skill point in parent skill.");
                return false;
            }
        }

        int invested = skillTreeData.getInvestedSkillPoints(skillId);
        int currentSkillLevel = skill.getCurrentSkillLevel(invested);

        if (currentSkillLevel >= skill.getMaxSkillLevel()) {
            return false;
        }

        int reqSkillPoints = skill.getReqSkillPoints(currentSkillLevel);

        SkillTreeData skillTreeData = this.getSkillTreeData();
        SkillBarData skillBarData = this.getSkillBarData();

        if (getSkillPointsLeftToSpend(player) >= reqSkillPoints) {
            List<InitializeTrigger> initializeTriggers = skill.getInitializeTriggers();
            for (InitializeTrigger initializeTrigger : initializeTriggers) {
                int castCounter = skillBar.getCastCounter();
                TriggerListener.onSkillUpgrade(player, initializeTrigger, skillId, currentSkillLevel + 1, castCounter);
                skillBar.increaseCastCounter();
            }
            int newInvested = invested + reqSkillPoints;
            skillTreeData.setInvestedSkillPoint(skillId, newInvested);

            int slotIndex = skillBarData.skillToSlot(skillId);
            if (slotIndex != -1) {
                skillBar.remakeSkillBarIcon(slotIndex, skillTree, this, lang);
            } else { // skill not in skill bar so add it if there is an available slot
                int firstEmptySlot = skillBarData.getFirstEmptySlot();
                if (firstEmptySlot != -1) {
                    skillBarData.set(player, skillTree, firstEmptySlot, skillId);
                    skillBar.remakeSkillBarIcon(firstEmptySlot, skillTree, this, lang);
                }
            }

            return true;
        }

        return false;
    }

    /**
     * @param skillId global if of skill
     */
    public boolean downgradeSkill(Player player, int skillId, SkillTree skillTree, RPGClassStats rpgClassStats, SkillBar skillBar, String lang) {
        if (!skillTree.containsSkill(skillId)) return false;

        Skill skill = skillTree.getSkill(skillId);

        int invested = skillTreeData.getInvestedSkillPoints(skillId);
        int currentSkillLevel = skill.getCurrentSkillLevel(invested);

        if (currentSkillLevel <= 0) return false;

        if (currentSkillLevel == 1) {
            HashMap<Integer, SkillTreeDirection> childSkills = skill.getChildSkills();
            for (Integer childSkillId : childSkills.keySet()) {
                int investedInChild = skillTreeData.getInvestedSkillPoints(childSkillId);
                if (investedInChild > 0) {
                    player.sendMessage("You can't downgrade skill because you have invested in child skill.");
                    return false;
                }
            }
        }

        List<InitializeTrigger> initializeTriggers = skill.getInitializeTriggers();
        for (InitializeTrigger initializeTrigger : initializeTriggers) {
            int castCounter = skillBar.getCastCounter();
            TriggerListener.onSkillDowngrade(player, initializeTrigger, skillId, currentSkillLevel - 1, castCounter);
            skillBar.increaseCastCounter();
        }

        int reqSkillPoints = skill.getReqSkillPoints(currentSkillLevel - 1);

        int newInvested = invested - reqSkillPoints;
        skillTreeData.setInvestedSkillPoint(skillId, newInvested);
        int slotIndex = skillBarData.skillToSlot(skillId);
        if (slotIndex != -1) {
            skillBar.remakeSkillBarIcon(slotIndex, skillTree, this, lang);
        }

        return true;
    }

    public boolean resetSkillPoints(Player player, SkillTree skillTree, SkillBar skillBar, String lang) {
        Set<Integer> skillIds = this.skillTreeData.getSkillIds();

        for (int skillId : skillIds) {
            Skill skill = skillTree.getSkill(skillId);

            List<InitializeTrigger> initializeTriggers = skill.getInitializeTriggers();
            for (InitializeTrigger initializeTrigger : initializeTriggers) {
                int castCounter = skillBar.getCastCounter();
                TriggerListener.onSkillDowngrade(player, initializeTrigger, skillId, 0, castCounter);
                skillBar.increaseCastCounter();
            }
        }

        this.skillTreeData.reset();

        for (int i = 0; i < 4; i++) {
            skillBar.remakeSkillBarIcon(i, skillTree, this, lang);
        }

        return true;
    }

    public int getSkillPointsLeftToSpend(Player player) {
        int pointsTotal = 1;
        if (GuardianDataManager.hasGuardianData(player)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
            if (guardianData.hasActiveCharacter()) {
                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                RPGCharacterStats rpgCharacterStats = activeCharacter.getRpgCharacterStats();

                int totalExperience = rpgCharacterStats.getTotalExp();

                pointsTotal = RPGCharacterExperienceManager.getLevel(totalExperience);
            }
        }

        int totalInvestedSkillPoints = skillTreeData.getTotalInvestedSkillPoints();

        return pointsTotal - totalInvestedSkillPoints;
    }
}
