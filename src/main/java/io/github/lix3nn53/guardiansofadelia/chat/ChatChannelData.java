package io.github.lix3nn53.guardiansofadelia.chat;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatChannelData {

    private final List<ChatChannel> listeningTo = new ArrayList<>();
    private ChatChannel textingTo;
    private Player privateChatTo;

    public ChatChannelData() {
        for (ChatChannel chatChannel : ChatChannel.values()) {
            if (chatChannel.isDefault()) {
                listeningTo.add(chatChannel);
            }
        }
    }

    public void addListening(ChatChannel channel) {
        listeningTo.add(channel);
    }

    public void removeListening(ChatChannel channel) {
        listeningTo.remove(channel);
    }

    public void toggleListening(ChatChannel channel) {
        if (textingTo != null && textingTo.equals(channel)) {
            return;
        }

        if (listeningTo.contains(channel)) {
            removeListening(channel);
        } else {
            addListening(channel);
        }
    }

    public boolean isListening(ChatChannel channel) {
        return listeningTo.contains(channel);
    }

    public Player getPrivateChatTo() {
        return privateChatTo;
    }

    public void setPrivateChatTo(Player privateChatTo) {
        this.privateChatTo = privateChatTo;
    }

    public ChatChannel getTextingTo() {
        return textingTo;
    }

    public void setTextingTo(ChatChannel textingTo) {
        this.textingTo = textingTo;

        if (textingTo != null) {
            if (!listeningTo.contains(textingTo)) {
                listeningTo.add(textingTo);
            }
        }
    }
}
