package io.github.lix3nn53.guardiansofadelia.quests.task;

import io.github.lix3nn53.guardiansofadelia.commands.admin.CommandAdmin;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.quests.actions.Action;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.centermessage.MessageUtils;
import io.github.lix3nn53.guardiansofadelia.utilities.managers.CompassManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class TaskReach extends TaskBase {

    private final Location blockLoc;
    private final Material blockMat;
    private List<Action> onCompleteActions = new ArrayList<>();
    private int completed = 0;

    public TaskReach(final Location blockLoc, final Material blockMat, Location customCompassTarget) {
        super(customCompassTarget);

        this.blockLoc = blockLoc;
        this.blockMat = blockMat;
    }

    public TaskReach freshCopy() {
        TaskReach taskCopy = new TaskReach(this.blockLoc, this.blockMat, customCompassTarget);
        taskCopy.setOnCompleteActions(this.onCompleteActions);
        return taskCopy;
    }

    public String getTablistInfoString(String language) {
        ChatPalette chatPalette = getChatPalette();

        return chatPalette + Translation.t(language, "quest.task.reach.l1") + " x: " + blockLoc.getBlockX() +
                " y: " + blockLoc.getBlockY() + " z: " + blockLoc.getBlockZ() + Translation.t(language, "quest.task.reach.l2")
                + blockLoc.getWorld().getName() + Translation.t(language, "quest.task.reach.l3") + blockMat.toString();
    }

    public String getItemLoreString(GuardianData guardianData) {
        ChatPalette color;
        if (isCompleted()) {
            color = ChatPalette.GREEN_DARK;
        } else {
            color = ChatPalette.YELLOW;
        }
        return color + Translation.t(guardianData, "quest.task.reach.l1") + " x: " + blockLoc.getBlockX() +
                " y: " + blockLoc.getBlockY() + " z: " + blockLoc.getBlockZ() + Translation.t(guardianData, "quest.task.reach.l2")
                + blockLoc.getWorld().getName() + Translation.t(guardianData, "quest.task.reach.l3") + blockMat.toString();
    }

    @Override
    public boolean isCompleted() {
        return completed > 0;
    }

    @Override
    public boolean progress(Player player, int questID, int taskIndex, boolean ignorePrevent) {
        if (this.completed == 0) {
            this.completed = 1;
            if (isCompleted()) {
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
                    this.completed = 0;
                    return true;
                }

                for (Action action : onCompleteActions) {
                    action.perform(player, questID, taskIndex);
                }
            }
            return true;
        }
        return false;
    }

    public boolean progress(Player player, Location targetBlockLoc, int questID, int taskIndex, boolean ignorePrevent) {
        float distanceSquared = (float) targetBlockLoc.distanceSquared(this.blockLoc);

        int maxDistance = 81;

        if (CommandAdmin.DEBUG_MODE) player.sendMessage("distanceSquared: " + distanceSquared);

        if (distanceSquared <= maxDistance) {
            if (progress(player, questID, taskIndex, ignorePrevent)) {
                MessageUtils.sendCenteredMessage(player, ChatPalette.PURPLE_LIGHT + "Quest reach " + this.blockLoc);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getProgress() {
        return this.completed;
    }

    @Override
    public void setProgress(int progress) {
        this.completed = progress;
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

    public Location getBlockLoc() {
        return blockLoc;
    }

    @Override
    public void setCompassTarget(Player player, String questName) {
        if (customCompassTarget != null) {
            super.setCompassTarget(player, questName);
            return;
        }

        Location blockLoc = this.getBlockLoc();
        CompassManager.setCompassItemLocation(player, questName + "-Reach", blockLoc);
    }
}
