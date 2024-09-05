package io.github.lix3nn53.guardiansofadelia.guardian.skill.tree;

import io.github.lix3nn53.guardiansofadelia.items.list.OtherItems;
import org.bukkit.inventory.ItemStack;

public enum SkillTreeArrow {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    UP_LEFT,
    UP_RIGHT,
    DOWN_LEFT,
    DOWN_RIGHT;

    public ItemStack getItemStack() {
        switch (this) {
            case UP -> {
                return OtherItems.getArrowUp();
            }
            case DOWN -> {
                return OtherItems.getArrowDown();
            }
            case LEFT -> {
                return OtherItems.getArrowLeft();
            }
            case RIGHT -> {
                return OtherItems.getArrowRight();
            }
            case UP_LEFT -> {
                return OtherItems.getArrowUpLeft();
            }
            case UP_RIGHT -> {
                return OtherItems.getArrowUpRight();
            }
            case DOWN_LEFT -> {
                return OtherItems.getArrowDownLeft();
            }
            case DOWN_RIGHT -> {
                return OtherItems.getArrowDownRight();
            }
        }

        return null;
    }
}
