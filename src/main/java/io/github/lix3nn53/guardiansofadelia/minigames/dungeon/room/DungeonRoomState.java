package io.github.lix3nn53.guardiansofadelia.minigames.dungeon.room;

import io.github.lix3nn53.guardiansofadelia.minigames.dungeon.DungeonInstance;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;

public class DungeonRoomState {
    private int currentWave = 1;
    private boolean isClear = false;
    private final DungeonInstance dungeonInstance;

    public DungeonRoomState(DungeonInstance dungeonInstance) {
        this.dungeonInstance = dungeonInstance;
    }

    public DungeonInstance getDungeonInstance() {
        return dungeonInstance;
    }

    List<ArmorStand> skillsOnGroundArmorStands = new ArrayList<>();

    public int getCurrentWave() {
        return currentWave;
    }

    public void onNextWaveStart() {
        currentWave++;
    }

    public static boolean isWaveCompleted(List<DungeonRoomSpawnerState> spawnerStates) {
        for (DungeonRoomSpawnerState spawnerState : spawnerStates) {
            if (!spawnerState.isClear()) {
                return false;
            }
        }

        return true;
    }

    public boolean isClear() {
        return isClear;
    }

    public void onComplete() {
        isClear = true;
    }

    public void addSkillsOnGroundArmorStand(ArmorStand add) {
        skillsOnGroundArmorStands.add(add);
    }

    public void clearSkillsOnGroundArmorStands() {
        skillsOnGroundArmorStands.clear();
    }
}
