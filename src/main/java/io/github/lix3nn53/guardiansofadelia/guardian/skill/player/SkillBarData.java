package io.github.lix3nn53.guardiansofadelia.guardian.skill.player;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import org.bukkit.entity.Player;

public class SkillBarData {

    // Which skills to use in the skill bar
    private final int[] skillBar = new int[4];

    public SkillBarData(int one, int two, int three, int ultimate) {
        this.skillBar[0] = one;
        this.skillBar[1] = two;
        this.skillBar[2] = three;
        this.skillBar[3] = ultimate;
    }

    public SkillBarData() {
        this(-1, -1, -1, -1);
    }

    /**
     * @param i skill slot index
     * @return skillId assigned to slot
     */
    public int getSkillId(int i) {
        return skillBar[i];
    }

    public void set(Player player, SkillTree skillTree, int slot, int skillId) {
        Skill skill = skillTree.getSkill(skillId);
        SkillType skillType = skill.getSkillTier();

        if (skillType.equals(SkillType.PASSIVE)) {
            player.sendMessage("You cannot assign passive skills to the skill bar.");
            return;
        } else if (skillType.equals(SkillType.ULTIMATE)) {
            for (int i = 0; i < 4; i++) {
                if (i == slot) {
                    continue;
                }
                int skillIdOnSlot = getSkillId(i);
                if (skillIdOnSlot != -1) {
                    Skill skillOnSlot = skillTree.getSkill(skillIdOnSlot);
                    SkillType skillTypeOnSlot = skillOnSlot.getSkillTier();
                    if (skillTypeOnSlot.equals(SkillType.ULTIMATE)) {
                        player.sendMessage("You cannot assign more than one ultimate skill to the skill bar.");
                        return;
                    }
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            if (i == slot) {
                continue;
            }
            int skillIdOnSlot = getSkillId(i);
            if (skillIdOnSlot == skillId) {
                skillBar[i] = -1;
                break;
            }
        }

        skillBar[slot] = skillId;
    }

    public int skillToSlot(int skillId) {
        for (int i = 0; i < 4; i++) {
            if (skillBar[i] == skillId) {
                return i;
            }
        }

        return -1;
    }

    public int getFirstEmptySlot() {
        for (int i = 0; i < 4; i++) {
            if (skillBar[i] == -1) {
                return i;
            }
        }

        return -1;
    }
}
