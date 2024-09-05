package io.github.lix3nn53.guardiansofadelia.chat;


import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacter;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterChatTag;

public enum StaffRank {
    OWNER,
    ADMIN,
    DEVELOPER,
    BUILDER,
    ARTIST,
    CONTENT,
    COMMUNITY,
    YOUTUBER;

    public ChatPalette getChatPalette() {
        return switch (this) {
            case OWNER -> ChatPalette.RED_DARK;
            case CONTENT -> ChatPalette.BLUE_LIGHT;
            case COMMUNITY -> ChatPalette.BLUE_DARK;
            case ADMIN, YOUTUBER -> ChatPalette.RED;
            case DEVELOPER -> ChatPalette.PURPLE;
            case BUILDER -> ChatPalette.BLUE;
            case ARTIST -> ChatPalette.PURPLE_LIGHT;
        };
    }

    public CustomCharacter getCustomCharacter() {
        return switch (this) {
            case OWNER -> CustomCharacterChatTag.OWNER;
            case ADMIN -> CustomCharacterChatTag.ADMIN;
            case DEVELOPER -> CustomCharacterChatTag.DEVELOPER;
            case BUILDER -> CustomCharacterChatTag.BUILDER;
            case ARTIST -> CustomCharacterChatTag.ARTIST;
            case CONTENT -> CustomCharacterChatTag.CONTENT;
            case COMMUNITY -> CustomCharacterChatTag.COMMUNITY;
            case YOUTUBER -> CustomCharacterChatTag.YOUTUBER;
        };

    }

    public boolean canBuild() {
        switch (this) {
            case OWNER, BUILDER, ADMIN -> {
                return true;
            }
        }

        return false;
    }
}
