package io.github.lix3nn53.guardiansofadelia.text;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;

public final class ChatPalette {

    private static final Map<String, ChatPalette> BY_NAME = new HashMap<>();

    public static final ChatPalette BROWN_DARK = new ChatPalette("#714150", "BROWN_DARK", ChatColor.DARK_GREEN);
    public static final ChatPalette BROWN = new ChatPalette("#93535a", "BROWN", ChatColor.DARK_GREEN);
    public static final ChatPalette BROWN_LIGHT = new ChatPalette("#b16f68", "BROWN_LIGHT", ChatColor.DARK_GREEN);
    public static final ChatPalette BROWN_LIGHTER = new ChatPalette("#ce977e", "BROWN_LIGHTER", ChatColor.DARK_GREEN);
    public static final ChatPalette CREAM_LIGHT = new ChatPalette("#eea160", "CREAM_LIGHT", ChatColor.WHITE);
    public static final ChatPalette CREAM_LIGHTER = new ChatPalette("#f4cca1", "CREAM_LIGHTER", ChatColor.WHITE);
    public static final ChatPalette GREEN_LIGHT = new ChatPalette("#52f91a", "GREEN_LIGHT", ChatColor.GREEN);
    public static final ChatPalette GREEN = new ChatPalette("#83ec13", "GREEN", ChatColor.GREEN);
    public static final ChatPalette GREEN_DARK = new ChatPalette("#14b830", "GREEN_DARK", ChatColor.DARK_GREEN);
    public static final ChatPalette BLUE_LIGHT = new ChatPalette("#7af8ff", "BLUE_LIGHT", ChatColor.AQUA);
    public static final ChatPalette BLUE = new ChatPalette("#10e2f9", "BLUE", ChatColor.BLUE);
    public static final ChatPalette BLUE_DARK = new ChatPalette("#067edb", "BLUE_DARK", ChatColor.DARK_BLUE);
    public static final ChatPalette BLUE_DARKER = new ChatPalette("#092caa", "BLUE_DARKER", ChatColor.DARK_BLUE);
    public static final ChatPalette PURPLE = new ChatPalette("#7e00cc", "PURPLE", ChatColor.DARK_PURPLE);
    public static final ChatPalette PURPLE_LIGHT = new ChatPalette("#c247eb", "PURPLE_LIGHT", ChatColor.DARK_PURPLE);
    public static final ChatPalette PINK = new ChatPalette("#fa3893", "PINK", ChatColor.LIGHT_PURPLE);
    public static final ChatPalette PINK_LIGHT = new ChatPalette("#f76eae", "PINK_LIGHT", ChatColor.LIGHT_PURPLE);
    public static final ChatPalette YELLOW = new ChatPalette("#ffff33", "YELLOW", ChatColor.YELLOW);
    public static final ChatPalette GOLD = new ChatPalette("#f9bc06", "GOLD", ChatColor.GOLD);
    public static final ChatPalette ORANGE = new ChatPalette("#fa9938", "ORANGE", ChatColor.GOLD);
    public static final ChatPalette RED = new ChatPalette("#ed4126", "RED", ChatColor.RED);
    public static final ChatPalette RED_DARK = new ChatPalette("#da0b0b", "RED_DARK", ChatColor.DARK_RED);
    public static final ChatPalette BLACK = new ChatPalette("#302c2e", "BLACK", ChatColor.BLACK);
    public static final ChatPalette GRAY_DARK = new ChatPalette("#6a6262", "GRAY_DARK", ChatColor.DARK_GRAY);
    public static final ChatPalette GRAY = new ChatPalette("#cfc6b8", "GRAY", ChatColor.GRAY);
    public static final ChatPalette WHITE = new ChatPalette("#dff6f5", "WHITE", ChatColor.WHITE);

    private final String toString;
    private final ChatColor chatColor;
    private final ChatColor oldColor;

    ChatPalette(String hex, String name, ChatColor oldColor) {
        this.chatColor = ChatColor.of(hex);
        String s = this.chatColor.toString().toUpperCase();
        this.toString = s.replaceFirst("X", "x");
        BY_NAME.put(name, this);
        this.oldColor = oldColor;
    }

    public static ChatPalette[] values() {
        return BY_NAME.values().toArray(new ChatPalette[0]);
    }

    public static ChatPalette valueOf(String name) {
        Preconditions.checkNotNull(name, "Name is null");
        ChatPalette defined = BY_NAME.get(name);
        Preconditions.checkArgument(defined != null, "No enum constant " + ChatPalette.class.getName() + "." + name);
        return defined;
    }

    @Override
    public String toString() {
        return toString;
    }

    public ChatColor toChatColor() {
        return chatColor;
    }

    public ChatColor toOldColor() {
        return oldColor;
    }
}
