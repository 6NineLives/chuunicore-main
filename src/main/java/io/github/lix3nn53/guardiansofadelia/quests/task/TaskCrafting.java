package io.github.lix3nn53.guardiansofadelia.quests.task;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.jobs.crafting.CraftingType;
import io.github.lix3nn53.guardiansofadelia.quests.actions.Action;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class TaskCrafting extends TaskBase {

    private final int amountNeeded;
    private final CraftingType craftingType;
    private final int minCraftingLevel;
    private final String itemNameContains;
    private List<Action> onCompleteActions = new ArrayList<>();
    private int progress = 0;

    public TaskCrafting(final CraftingType craftingType, final int minCraftingLevel, final String itemNameContains,
                        final int amountNeeded, Location customCompassTarget) {
        super(customCompassTarget);
        this.craftingType = craftingType;
        this.minCraftingLevel = minCraftingLevel;
        this.itemNameContains = itemNameContains;
        this.amountNeeded = amountNeeded;
    }

    public TaskCrafting freshCopy() {
        TaskCrafting taskCopy = new TaskCrafting(this.craftingType, this.minCraftingLevel, this.itemNameContains,
                this.amountNeeded, customCompassTarget);
        taskCopy.setOnCompleteActions(this.onCompleteActions);
        return taskCopy;
    }

    public String getTablistInfoString(String language) {
        ChatPalette chatPalette = getChatPalette();

        String text1 = Translation.t(language, "quest.task.craft.l3");
        if (itemNameContains != null && !itemNameContains.equals("")) {
            text1 = itemNameContains;
        }

        return chatPalette + Translation.t(language, "quest.task.craft.l1") + getProgress() + "/" + getRequiredProgress() + " " + text1 + Translation.t(language, "quest.task.craft.l2") + craftingType.getName();
    }

    public String getItemLoreString(GuardianData guardianData) {
        ChatPalette color;
        if (isCompleted()) {
            color = ChatPalette.GREEN_DARK;
        } else {
            color = ChatPalette.YELLOW;
        }

        String text1 = Translation.t(guardianData, "quest.task.craft.l3");
        if (itemNameContains != null && !itemNameContains.equals("")) {
            text1 = itemNameContains;
        }

        return color + Translation.t(guardianData, "quest.task.craft.l1") + getRequiredProgress() + " " + text1 + Translation.t(guardianData, "quest.task.craft.l2") + craftingType.getName();
    }

    @Override
    public boolean isCompleted() {
        return progress >= amountNeeded;
    }

    @Override
    public boolean progress(Player player, int questID, int taskIndex, boolean ignorePrevent) {
        if (this.progress < this.amountNeeded) {
            this.progress++;
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
                    this.progress--;
                    return false;
                }

                for (Action action : onCompleteActions) {
                    action.perform(player, questID, taskIndex);
                }
            }
            return true;
        }
        return false;
    }

    public void progressBy(Player player, int progress, int questID, int taskIndex, boolean ignorePrevent) {
        this.progress += progress;
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
                this.progress--;
                return;
            }

            for (Action action : onCompleteActions) {
                action.perform(player, questID, taskIndex);
            }
        }
    }

    @Override
    public int getProgress() {
        return this.progress;
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public int getRequiredProgress() {
        return amountNeeded;
    }

    public boolean progress(CraftingType craftingType, String itemName, Player player, int questID, int taskIndex, boolean ignorePrevent) {
        if (craftingType.equals(this.craftingType)) {
            if (itemNameContains != null && !itemNameContains.equals("")) {
                if (!itemName.contains(this.itemNameContains)) {
                    return false;
                }
            }

            progressBy(player, 1, questID, taskIndex, ignorePrevent);
            return true;
        }

        return false;
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
