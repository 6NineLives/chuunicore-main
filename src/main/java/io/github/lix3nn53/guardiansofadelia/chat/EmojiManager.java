package io.github.lix3nn53.guardiansofadelia.chat;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.ChatColor;

import java.util.HashMap;

public class EmojiManager {

    private static final HashMap<String, String> keyToEmoji = new HashMap<>();
    private static String emojiGuideMessage = "";

    public static void addEmoji(String key, String emoji) {
        key = key.toLowerCase();
        keyToEmoji.put(key, emoji);
        GuardiansOfAdelia.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Added emoji: " + key + " " + emoji);

        if (!emojiGuideMessage.isEmpty()) {
            emojiGuideMessage += " ";
        }
        emojiGuideMessage += ":" + key + ":" + emoji;
    }

    public static String replaceEmojisInMessage(String message, ChatColor messageColor) {
        for (String key : keyToEmoji.keySet()) {
            message = message.replace(":" + key + ":", ChatColor.WHITE + keyToEmoji.get(key) + messageColor);
        }

        return message;
    }

    public static String getEmojiGuideMessage() {
        return emojiGuideMessage;
    }
}
