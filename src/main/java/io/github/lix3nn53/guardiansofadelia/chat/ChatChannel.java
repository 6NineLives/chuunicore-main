package io.github.lix3nn53.guardiansofadelia.chat;

import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;

public enum ChatChannel {
    PRIVATE,
    PARTY,
    GUILD,
    TRADE,
    LANG_TR,
    LANG_FR,
    LANG_ES;

    public String toString() {
        return switch (this) {
            case PRIVATE -> "Private";
            case PARTY -> "Party";
            case GUILD -> "Guild";
            case TRADE -> "Trade";
            case LANG_TR -> "Turkish";
            case LANG_FR -> "French";
            case LANG_ES -> "Spanish";
        };
    }

    public String getPrefix() {
        switch (this) {
            case PRIVATE -> {
                return "Private";
            }
            case PARTY -> {
                return "Party";
            }
            case GUILD -> {
                return "Guild";
            }
            case TRADE -> {
                return "Trade";
            }
            case LANG_TR -> {
                return "TR";
            }
            case LANG_FR -> {
                return "FR";
            }
            case LANG_ES -> {
                return "ES";
            }
        }

        return "ERR ChatChannel prefix";
    }

    public ChatPalette getPalette() {
        switch (this) {
            case PRIVATE -> {
                return ChatPalette.RED;
            }
            case PARTY -> {
                return ChatPalette.BLUE;
            }
            case GUILD -> {
                return ChatPalette.PURPLE;
            }
            case TRADE -> {
                return ChatPalette.GOLD;
            }
            case LANG_TR, LANG_ES, LANG_FR -> {
                return ChatPalette.YELLOW;
            }
        }

        return null;
    }

    public boolean isDefault() {
        switch (this) {
            case PRIVATE, GUILD, PARTY, TRADE -> {
                return true;
            }
        }

        return false;
    }

    public boolean canStopListening() {
        switch (this) {
            case GUILD, PARTY -> {
                return false;
            }
        }

        return true;
    }
}
