package io.github.lix3nn53.guardiansofadelia.quests.task;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.quests.actions.Action;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.lumine.mythic.api.mobs.MobManager;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class TaskKill extends TaskBase {

    private final int amountNeeded;

    private final String internalName;
    private final String mobName;
    private List<Action> onCompleteActions = new ArrayList<>();
    private int progress;

    public TaskKill(final String internalName, final int amountNeeded, Location customCompassTarget) {
        super(customCompassTarget);

        MobManager mobManager = MythicBukkit.inst().getMobManager();
        Optional<MythicMob> mythicMob = mobManager.getMythicMob(internalName);


        if (mythicMob.isPresent()) {
            this.mobName = mythicMob.get().getDisplayName().get();
            this.internalName = internalName;
            this.amountNeeded = amountNeeded;
            this.progress = 0;
        } else {
            throw new IllegalArgumentException("TaskKill: MythicMob not found: " + internalName);
        }

    }

    public TaskKill freshCopy() {
        TaskKill taskCopy = new TaskKill(this.internalName, this.amountNeeded, customCompassTarget);
        taskCopy.setOnCompleteActions(this.onCompleteActions);
        return taskCopy;
    }

    public String getTablistInfoString(String language) {
        ChatPalette chatPalette = getChatPalette();

        return chatPalette + Translation.t(language, "quest.task.kill.l1") + getProgress() + "/" +
                getRequiredProgress() + " " + ChatColor.stripColor(mobName);
    }

    public String getItemLoreString(GuardianData guardianData) {
        ChatPalette color;
        if (isCompleted()) {
            color = ChatPalette.GREEN_DARK;
        } else {
            color = ChatPalette.YELLOW;
        }
        return color + Translation.t(guardianData, "quest.task.kill.l1") + amountNeeded + " " + ChatColor.stripColor(mobName);
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

    public boolean progress(Player player, String internalName, int questID, int taskIndex, boolean ignorePrevent) {
        if (internalName.equals(this.internalName)) {
            return progress(player, questID, taskIndex, ignorePrevent);
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
