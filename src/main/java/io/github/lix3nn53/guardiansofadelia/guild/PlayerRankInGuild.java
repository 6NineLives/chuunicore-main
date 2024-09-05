package io.github.lix3nn53.guardiansofadelia.guild;

import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;

public enum PlayerRankInGuild {
    LEADER,
    COMMANDER,
    SERGEANT,
    CORPORAL,
    SOLDIER;

    public boolean canInvite() {
        return this == LEADER || this == COMMANDER || this == SERGEANT;
    }

    public boolean canSetAnnouncement() {
        return this == LEADER || this == COMMANDER;
    }

    public boolean canKick() {
        return this == LEADER || this == COMMANDER;
    }

    public String getName() {
        return switch (this) {
            case LEADER -> ChatPalette.RED + "Leader";
            case COMMANDER -> ChatPalette.ORANGE + "Commander";
            case SERGEANT -> ChatPalette.BLUE_LIGHT + "Sergeant";
            case CORPORAL -> ChatPalette.YELLOW + "Corporal";
            case SOLDIER -> ChatPalette.GREEN + "Soldier";
        };
    }
}
