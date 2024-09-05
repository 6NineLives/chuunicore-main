package io.github.lix3nn53.guardiansofadelia.guardian.skill;

import java.util.ArrayList;
import java.util.List;

public enum SkillType {
    NORMAL,
    PASSIVE,
    ULTIMATE;

    public List<Integer> getDefaultReqPoints() {
        List<Integer> reqPoints = new ArrayList<>();

        switch (this) {
            case NORMAL -> {
                reqPoints.add(1);
                reqPoints.add(1);
                reqPoints.add(1);
                reqPoints.add(1);
            }
            case PASSIVE -> {
                reqPoints.add(2);
                reqPoints.add(2);
                reqPoints.add(2);
                reqPoints.add(2);
            }
            case ULTIMATE -> {
                reqPoints.add(3);
                reqPoints.add(3);
                reqPoints.add(3);
                reqPoints.add(3);
            }
        }

        return reqPoints;
    }
}
