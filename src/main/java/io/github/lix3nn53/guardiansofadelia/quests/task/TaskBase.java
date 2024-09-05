package io.github.lix3nn53.guardiansofadelia.quests.task;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.quests.actions.Action;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.utilities.managers.CompassManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TaskBase implements Task {

    protected final Location customCompassTarget;

    public TaskBase(Location customCompassTarget) {
        this.customCompassTarget = customCompassTarget;
    }

    @Override
    public String getTablistInfoString(String language) {
        return "ERROR UNKNOWN TASK";
    }

    @Override
    public String getItemLoreString(GuardianData guardianData) {
        return "ERROR UNKNOWN TASK";
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public boolean progress(Player player, int questID, int taskIndex, boolean ignorePrevent) {
        return false;
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public void setProgress(int progress) {

    }

    @Override
    public int getRequiredProgress() {
        return 0;
    }

    @Override
    public ChatPalette getChatPalette() {
        return null;
    }

    @Override
    public void addOnCompleteAction(Action action) {

    }

    @Override
    public Task freshCopy() {
        return null;
    }

    @Override
    public void setCompassTarget(Player player, String questName) {
        if (customCompassTarget != null) {
            String simpleName = this.getClass().getSimpleName();

            CompassManager.setCompassItemLocation(player, questName + "-" + simpleName, customCompassTarget);
        }
    }
}
