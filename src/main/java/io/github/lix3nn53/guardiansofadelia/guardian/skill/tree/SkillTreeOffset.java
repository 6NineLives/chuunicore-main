package io.github.lix3nn53.guardiansofadelia.guardian.skill.tree;

public class SkillTreeOffset {
    private int x;
    private int y;

    public SkillTreeOffset(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean inBounds(int maxX, int maxY, int minX, int minY) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }
}
