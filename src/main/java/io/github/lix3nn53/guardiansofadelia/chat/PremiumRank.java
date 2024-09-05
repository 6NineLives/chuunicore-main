package io.github.lix3nn53.guardiansofadelia.chat;


import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacter;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterChatTag;

public enum PremiumRank {
    HERO,
    LEGEND,
    TITAN;

    /*public ChatPalette getChatPalette() {
        switch (this) {
            case HERO:
                return ChatPalette.BLUE_LIGHT;
            case LEGEND:
                return ChatPalette.GOLD;
            case TITAN:
                return ChatPalette.PURPLE_LIGHT;
        }
        return ChatPalette.GRAY;
    }*/

    public CustomCharacter getCustomCharacter() {
        switch (this) {
            case HERO:
                return CustomCharacterChatTag.HERO;
            case LEGEND:
                return CustomCharacterChatTag.LEGEND;
            case TITAN:
                return CustomCharacterChatTag.TITAN;
        }

        return null;
    }
}
