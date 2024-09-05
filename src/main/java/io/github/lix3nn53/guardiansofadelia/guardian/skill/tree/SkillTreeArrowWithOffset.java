package io.github.lix3nn53.guardiansofadelia.guardian.skill.tree;

public class SkillTreeArrowWithOffset {

    private final SkillTreeArrow skillTreeArrow;
    private final SkillTreeOffset skillTreeOffset;

    public SkillTreeArrowWithOffset(SkillTreeArrow skillTreeArrow, SkillTreeOffset skillTreeOffset) {
        this.skillTreeArrow = skillTreeArrow;
        this.skillTreeOffset = skillTreeOffset;
    }

    public SkillTreeArrow getSkillTreeArrow() {
        return skillTreeArrow;
    }

    public SkillTreeOffset getSkillTreeOffset() {
        return skillTreeOffset;
    }
}
