package io.github.lix3nn53.guardiansofadelia.guardian.skill.player;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;

import java.util.HashMap;
import java.util.Set;

public class SkillTreeData {
    private final HashMap<Integer, Integer> investedSkillPoints;

    public SkillTreeData(HashMap<Integer, Integer> investedSkillPoints) {
        this.investedSkillPoints = investedSkillPoints;
    }

    public SkillTreeData() {
        this.investedSkillPoints = new HashMap<>();
    }

    public int getInvestedSkillPoints(int skillId) {
        return investedSkillPoints.getOrDefault(skillId, 0);
    }

    public void setInvestedSkillPoint(int skillId, int investedSkillPoint) {
        this.investedSkillPoints.put(skillId, investedSkillPoint);
    }

    public int getTotalInvestedSkillPoints() {
        int total = 0;
        for (int investedSkillPoints : investedSkillPoints.values()) {
            total += investedSkillPoints;
        }
        return total;
    }

    public boolean containsSkill(int skillId) {
        return investedSkillPoints.containsKey(skillId);
    }

    public void reset() {
        investedSkillPoints.clear();
    }

    public int getCurrentSkillLevel(Skill skill) {
        int id = skill.getId();

        Integer invested = investedSkillPoints.get(id);
        if (invested == null) {
            return 0;
        }

        return skill.getCurrentSkillLevel(invested);
    }

    public Set<Integer> getSkillIds() {
        return investedSkillPoints.keySet();
    }
}
