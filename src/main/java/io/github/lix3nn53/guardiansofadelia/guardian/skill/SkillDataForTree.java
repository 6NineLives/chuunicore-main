package io.github.lix3nn53.guardiansofadelia.guardian.skill;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTreeDirection;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTreeOffset;

import java.util.HashMap;

public record SkillDataForTree(int parentId,
                               HashMap<Integer, SkillTreeDirection> childSkills,
                               SkillTreeOffset rootStartOffset) {

    public int getParentId() {
        return parentId;
    }

    public HashMap<Integer, SkillTreeDirection> getChildSkills() {
        return childSkills;
    }

    public SkillTreeOffset getRootStartOffset() {
        return rootStartOffset;
    }
}
