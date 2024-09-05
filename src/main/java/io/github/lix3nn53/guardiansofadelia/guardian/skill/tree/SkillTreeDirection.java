package io.github.lix3nn53.guardiansofadelia.guardian.skill.tree;

import java.util.ArrayList;
import java.util.List;

public enum SkillTreeDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    UP_LEFT,
    UP_RIGHT,
    DOWN_LEFT,
    DOWN_RIGHT;

    public SkillTreeOffset getOffset(SkillTreeOffset parentOffset) {
        return new SkillTreeOffset(getOffsetX(parentOffset.getX()), getOffsetY(parentOffset.getY()));
    }

    private int getOffsetX(int parentX) {
        switch (this) {
            case UP, DOWN -> {
                return parentX;
            }
            case LEFT, DOWN_LEFT, UP_LEFT -> {
                return parentX - 2;
            }
            case RIGHT, UP_RIGHT, DOWN_RIGHT -> {
                return parentX + 2;
            }
        }

        return parentX;
    }

    private int getOffsetY(int parentY) {
        switch (this) {
            case UP -> {
                return parentY + 2;
            }
            case UP_RIGHT, UP_LEFT -> {
                return parentY + 1;
            }
            case DOWN -> {
                return parentY - 2;
            }
            case DOWN_RIGHT, DOWN_LEFT -> {
                return parentY - 1;
            }
            case LEFT, RIGHT -> {
                return parentY;
            }
        }

        return parentY;
    }

    public List<SkillTreeArrowWithOffset> getArrows(SkillTreeOffset parentOffset) {
        List<SkillTreeArrowWithOffset> result = new ArrayList<>();

        int parentX = parentOffset.getX();
        int parentY = parentOffset.getY();

        switch (this) {
            case UP -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.UP, new SkillTreeOffset(parentX, parentY + 1)));
            }
            case DOWN -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.DOWN, new SkillTreeOffset(parentX, parentY - 1)));
            }
            case LEFT -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.LEFT, new SkillTreeOffset(parentX - 1, parentY)));
            }
            case RIGHT -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.RIGHT, new SkillTreeOffset(parentX + 1, parentY)));
            }
            case UP_LEFT -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.UP_LEFT, new SkillTreeOffset(parentX - 1, parentY + 1)));
            }
            case DOWN_LEFT -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.DOWN_LEFT, new SkillTreeOffset(parentX - 1, parentY - 1)));
            }
            case UP_RIGHT -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.UP_RIGHT, new SkillTreeOffset(parentX + 1, parentY + 1)));
            }
            case DOWN_RIGHT -> {
                result.add(new SkillTreeArrowWithOffset(SkillTreeArrow.DOWN_RIGHT, new SkillTreeOffset(parentX + 1, parentY - 1)));
            }
        }

        return result;
    }
}
