package io.github.lix3nn53.guardiansofadelia.cosmetic.inner;

import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticManager;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public enum CosmeticColor {
    RED,
    ORANGE,
    YELLOW,
    PINK,
    WHITE,
    GREEN,
    PURPLE,
    BLUE,
    AQUA;

    public static List<Integer> getGuiIndex() {
        List<Integer> guiIndex = new ArrayList<>();

        guiIndex.add(12);
        guiIndex.add(13);
        guiIndex.add(14);
        guiIndex.add(21);
        guiIndex.add(22);
        guiIndex.add(23);
        guiIndex.add(30);
        guiIndex.add(31);
        guiIndex.add(32);

        return guiIndex;
    }

    public Color getTint(int index) {
        if (index > 6) {
            throw new IllegalArgumentException("index must be between 0 and 6");
        }

        List<Color> colors = new ArrayList<>();

        switch (this) {
            case RED -> {
                colors.add(Color.fromRGB(201, 80, 71));
                colors.add(Color.fromRGB(170, 82, 82));
                colors.add(Color.fromRGB(201, 59, 41));
                colors.add(Color.fromRGB(169, 46, 42));
                colors.add(Color.fromRGB(149, 17, 24));
                colors.add(Color.fromRGB(134, 57, 62));
                colors.add(Color.fromRGB(108, 34, 34));
            }
            case ORANGE -> {
                colors.add(Color.fromRGB(247, 153, 91));
                colors.add(Color.fromRGB(229, 130, 55));
                colors.add(Color.fromRGB(250, 127, 28));
                colors.add(Color.fromRGB(237, 93, 35));
                colors.add(Color.fromRGB(158, 99, 61));
                colors.add(Color.fromRGB(129, 83, 49));
                colors.add(Color.fromRGB(134, 74, 67));
            }
            case YELLOW -> {
                colors.add(Color.fromRGB(252, 223, 131));
                colors.add(Color.fromRGB(252, 197, 101));
                colors.add(Color.fromRGB(243, 221, 80));
                colors.add(Color.fromRGB(244, 190, 33));
                colors.add(Color.fromRGB(228, 159, 39));
                colors.add(Color.fromRGB(245, 155, 37));
                colors.add(Color.fromRGB(182, 115, 0));
            }
            case PINK -> {
                colors.add(Color.fromRGB(252, 166, 219));
                colors.add(Color.fromRGB(248, 140, 174));
                colors.add(Color.fromRGB(252, 101, 138));
                colors.add(Color.fromRGB(203, 84, 182));
                colors.add(Color.fromRGB(176, 107, 152));
                colors.add(Color.fromRGB(205, 69, 120));
                colors.add(Color.fromRGB(191, 50, 72));
            }
            case WHITE -> {
                colors.add(Color.fromRGB(252, 252, 252));
                colors.add(Color.fromRGB(202, 202, 202));
                colors.add(Color.fromRGB(176, 176, 176));
                colors.add(Color.fromRGB(151, 151, 151));
                colors.add(Color.fromRGB(101, 101, 101));
                colors.add(Color.fromRGB(75, 75, 75));
                colors.add(Color.fromRGB(50, 50, 50));
            }
            case GREEN -> {
                colors.add(Color.fromRGB(145, 184, 78));
                colors.add(Color.fromRGB(78, 179, 89));
                colors.add(Color.fromRGB(114, 177, 25));
                colors.add(Color.fromRGB(90, 119, 22));
                colors.add(Color.fromRGB(99, 121, 75));
                colors.add(Color.fromRGB(76, 121, 95));
                colors.add(Color.fromRGB(65, 84, 51));
            }
            case PURPLE -> {
                colors.add(Color.fromRGB(201, 101, 246));
                colors.add(Color.fromRGB(132, 66, 171));
                colors.add(Color.fromRGB(209, 151, 252));
                colors.add(Color.fromRGB(144, 113, 225));
                colors.add(Color.fromRGB(139, 98, 149));
                colors.add(Color.fromRGB(139, 68, 131));
                colors.add(Color.fromRGB(92, 55, 106));
            }
            case BLUE -> {
                colors.add(Color.fromRGB(104, 120, 223));
                colors.add(Color.fromRGB(57, 123, 163));
                colors.add(Color.fromRGB(53, 101, 159));
                colors.add(Color.fromRGB(92, 92, 157));
                colors.add(Color.fromRGB(77, 96, 125));
                colors.add(Color.fromRGB(64, 76, 153));
                colors.add(Color.fromRGB(53, 76, 103));
            }
            case AQUA -> {
                colors.add(Color.fromRGB(174, 220, 226));
                colors.add(Color.fromRGB(127, 213, 205));
                colors.add(Color.fromRGB(61, 182, 218));
                colors.add(Color.fromRGB(115, 162, 200));
                colors.add(Color.fromRGB(22, 153, 154));
                colors.add(Color.fromRGB(35, 143, 122));
                colors.add(Color.fromRGB(83, 115, 129));
            }
        }

        return colors.get(index);
    }

    public ItemStack getItem(int tintIndex) {
        ItemStack itemStack = new ItemStack(CosmeticManager.COSMETIC_MATERIAL);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        itemMeta.setCustomModelData(1);
        itemMeta.setDisplayName(this.toString());

        List<String> lore = new ArrayList<>();
        lore.add("");

        itemMeta.setLore(lore);
        itemMeta.setColor(this.getTint(tintIndex));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
