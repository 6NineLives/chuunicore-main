package io.github.lix3nn53.guardiansofadelia.quests.task;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.quests.actions.Action;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class TaskTriggerDeath extends TaskBase {

    private List<Action> onCompleteActions = new ArrayList<>();
    private boolean triggered = false;


    public TaskTriggerDeath(Location customCompassTarget) {
        super(customCompassTarget);
    }

    public TaskTriggerDeath freshCopy() {
        TaskTriggerDeath taskCopy = new TaskTriggerDeath(customCompassTarget);
        taskCopy.setOnCompleteActions(this.onCompleteActions);
        return taskCopy;
    }

    public String getTablistInfoString(String language) {
        ChatPalette chatPalette = getChatPalette();

        return chatPalette + Translation.t(language, "quest.task.triggerDeath.l1") + getProgress() + "/" + getRequiredProgress() + Translation.t(language, "quest.task.triggerDeath.l2");
    }

    public String getItemLoreString(GuardianData guardianData) {
        ChatPalette color;
        if (isCompleted()) {
            color = ChatPalette.GREEN_DARK;
        } else {
            color = ChatPalette.YELLOW;
        }

        return color + Translation.t(guardianData, "quest.task.triggerDeath.l1") + Translation.t(guardianData, "quest.task.triggerDeath.l2");
    }

    @Override
    public boolean isCompleted() {
        return triggered;
    }

    @Override
    public boolean progress(Player player, int questID, int taskIndex, boolean ignorePrevent) {
        if (!triggered) {
            this.triggered = true;

            boolean prevent = false;
            if (!ignorePrevent) {
                for (Action action : onCompleteActions) {
                    boolean b = action.preventTaskCompilation();
                    if (b) {
                        prevent = true;
                        action.perform(player, questID, taskIndex);
                        break;
                    }
                }
            }

            if (prevent) {
                this.triggered = false;
                return false;
            }

            for (Action action : onCompleteActions) {
                action.perform(player, questID, taskIndex);
            }

            return true;
        }
        return false;
    }

    @Override
    public int getProgress() {
        return this.triggered ? 1 : 0;
    }

    @Override
    public void setProgress(int progress) {
        this.triggered = progress > 0;
    }

    @Override
    public int getRequiredProgress() {
        return 1;
    }

    @Override
    public void addOnCompleteAction(Action action) {
        onCompleteActions.add(action);
    }

    public void setOnCompleteActions(List<Action> onCompleteActions) {
        this.onCompleteActions = onCompleteActions;
    }

    @Override
    public ChatPalette getChatPalette() {
        if (isCompleted()) return ChatPalette.GREEN_DARK;

        return ChatPalette.RED;
    }
}
