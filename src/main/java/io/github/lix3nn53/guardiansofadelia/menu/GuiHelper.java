package io.github.lix3nn53.guardiansofadelia.menu;

import io.github.lix3nn53.guardiansofadelia.items.list.OtherItems;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class GuiHelper {

    private static final List<Integer> Big54Button0 = Arrays.asList(18, 19, 9, 10);
    private static final List<Integer> Big54Button1 = Arrays.asList(20, 21, 11, 12);
    private static final List<Integer> Big54Button2 = Arrays.asList(23, 24, 14, 15);
    private static final List<Integer> Big54Button3 = Arrays.asList(25, 26, 16, 17);
    private static final List<Integer> Big54Button4 = Arrays.asList(45, 46, 36, 37);
    private static final List<Integer> Big54Button5 = Arrays.asList(47, 48, 38, 39);
    private static final List<Integer> Big54Button6 = Arrays.asList(50, 51, 41, 42);
    private static final List<Integer> Big54Button7 = Arrays.asList(52, 53, 43, 44);
    private static final int SmallButton0 = 9;
    private static final int SmallButton1 = 11;
    private static final int SmallButton2 = 13;
    private static final int SmallButton3 = 15;
    private static final int SmallButton4 = 17;
    // 27 ends here
    private static final int SmallButton5 = 27;
    private static final int SmallButton6 = 29;
    private static final int SmallButton7 = 31;
    private static final int SmallButton8 = 33;
    private static final int SmallButton9 = 35;
    private static final int SmallButton10 = 45;
    private static final int SmallButton11 = 47;
    private static final int SmallButton12 = 49;
    private static final int SmallButton13 = 51;
    private static final int SmallButton14 = 53;

    public static void form54Big(GuiGeneric guiGeneric, ItemStack[] buttons, String backTo) {
        if (backTo != null) {
            ItemStack backButton = OtherItems.getBackButton(backTo);
            guiGeneric.setItem(0, backButton);
        }

        int i = 0;
        for (ItemStack button : buttons) {
            if (i > 7) break;
            if (button != null) {
                add54BigButton(guiGeneric, button, get54BigButtonIndexes(i));
            }
            i++;
        }
    }

    private static void add54BigButton(GuiGeneric guiGeneric, ItemStack button, List<Integer> indexes) {
        ItemMeta itemMeta = button.getItemMeta();
        String displayName = itemMeta.getDisplayName();
        List<String> lore = itemMeta.getLore();
        guiGeneric.setItem(indexes.get(0), button);

        ItemStack empty = button;
        if (button.getItemMeta().hasCustomModelData()) {
            empty = OtherItems.getEmpty(displayName, lore);
        }

        for (int i = 1; i < indexes.size(); i++) {
            guiGeneric.setItem(indexes.get(i), empty);
        }
    }

    public static List<Integer> get54BigButtonIndexes(int itemIndex) {
        if (itemIndex == 0) {
            return Big54Button0;
        } else if (itemIndex == 1) {
            return Big54Button1;
        } else if (itemIndex == 2) {
            return Big54Button2;
        } else if (itemIndex == 3) {
            return Big54Button3;
        } else if (itemIndex == 4) {
            return Big54Button4;
        } else if (itemIndex == 5) {
            return Big54Button5;
        } else if (itemIndex == 6) {
            return Big54Button6;
        } else if (itemIndex == 7) {
            return Big54Button7;
        }

        return null;
    }

    public static void form27Small(GuiGeneric guiGeneric, ItemStack[] buttons, String backTo) {
        if (backTo != null) {
            ItemStack backButton = OtherItems.getBackButton(backTo);
            guiGeneric.setItem(0, backButton);
        }

        int i = 0;
        for (ItemStack button : buttons) {
            if (i > 4) break;
            if (button != null) {
                add27SmallButton(guiGeneric, button, get27SmallButtonIndex(i));
                i++;
            }
        }
    }

    private static void add27SmallButton(GuiGeneric guiGeneric, ItemStack button, int index) {
        guiGeneric.setItem(index, button);
    }

    public static int get27SmallButtonIndex(int itemIndex) {
        if (itemIndex == 0) {
            return SmallButton0;
        } else if (itemIndex == 1) {
            return SmallButton1;
        } else if (itemIndex == 2) {
            return SmallButton2;
        } else if (itemIndex == 3) {
            return SmallButton3;
        } else if (itemIndex == 4) {
            return SmallButton4;
        }

        return 0;
    }

    public static void form54Small(GuiGeneric guiGeneric, ItemStack[] buttons, String backTo) {
        if (backTo != null) {
            ItemStack backButton = OtherItems.getBackButton(backTo);
            guiGeneric.setItem(0, backButton);
        }

        int i = 0;
        for (ItemStack button : buttons) {
            if (i > 14) break;
            if (button != null) {
                add54SmallButton(guiGeneric, button, get54SmallButtonIndex(i));
                i++;
            }
        }
    }

    private static void add54SmallButton(GuiGeneric guiGeneric, ItemStack button, int index) {
        guiGeneric.setItem(index, button);
    }

    public static int get54SmallButtonIndex(int itemIndex) {
        if (itemIndex == 0) {
            return SmallButton0;
        } else if (itemIndex == 1) {
            return SmallButton1;
        } else if (itemIndex == 2) {
            return SmallButton2;
        } else if (itemIndex == 3) {
            return SmallButton3;
        } else if (itemIndex == 4) {
            return SmallButton4;
        } else if (itemIndex == 5) {
            return SmallButton5;
        } else if (itemIndex == 6) {
            return SmallButton6;
        } else if (itemIndex == 7) {
            return SmallButton7;
        } else if (itemIndex == 8) {
            return SmallButton8;
        } else if (itemIndex == 9) {
            return SmallButton9;
        } else if (itemIndex == 10) {
            return SmallButton10;
        } else if (itemIndex == 11) {
            return SmallButton11;
        } else if (itemIndex == 12) {
            return SmallButton12;
        } else if (itemIndex == 13) {
            return SmallButton13;
        } else if (itemIndex == 14) {
            return SmallButton14;
        }

        return 0;
    }
}
