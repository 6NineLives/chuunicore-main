package io.github.lix3nn53.guardiansofadelia.utilities.config;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.onground.RandomSkillOnGroundWithOffset;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.onground.SkillOnGround;
import io.github.lix3nn53.guardiansofadelia.interactables.chest.LootChestTier;
import io.github.lix3nn53.guardiansofadelia.items.GearLevel;
import io.github.lix3nn53.guardiansofadelia.minigames.MiniGameManager;
import io.github.lix3nn53.guardiansofadelia.minigames.checkpoint.Checkpoint;
import io.github.lix3nn53.guardiansofadelia.minigames.dungeon.DungeonInstance;
import io.github.lix3nn53.guardiansofadelia.minigames.dungeon.DungeonTheme;
import io.github.lix3nn53.guardiansofadelia.minigames.dungeon.room.*;
import io.github.lix3nn53.guardiansofadelia.transportation.portals.PortalColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DungeonConfiguration {

    private static final String basePath = ConfigManager.DATA_FOLDER + File.separator + "dungeons";
    private static FileConfiguration dungeonInstancesConfig;
    private static FileConfiguration dungeonGatesConfig;
    private static final String themePath = basePath + File.separator + "themes";
    private static HashMap<String, YamlConfiguration> dungeonThemesConfigs;

    public static void createConfigs() {
        dungeonInstancesConfig = ConfigurationUtils.createConfig(basePath, "dungeonInstances.yml");
        dungeonGatesConfig = ConfigurationUtils.createConfig(basePath, "dungeonGates.yml");

        dungeonThemesConfigs = ConfigurationUtils.getAllConfigsInFile(themePath);
    }

    public static void loadConfigs() {
        loadDungeonThemes();
        loadDungeonGates();
        loadInstances();
    }

    public static void writeConfigs() {
        writeDungeonThemes();
        writeInstances("dungeonInstances.yml");
    }

    private static void loadDungeonThemes() {
        for (String code : dungeonThemesConfigs.keySet()) {
            YamlConfiguration section = dungeonThemesConfigs.get(code);

            String name = section.getString("name");
            int gearLevelIndex = section.getInt("gearLevel");
            GearLevel gearLevel = GearLevel.values()[gearLevelIndex];
            String portalColorStr = section.getString("portalColor");
            PortalColor portalColor = PortalColor.valueOf(portalColorStr);

            int levelReq = section.getInt("levelReq");
            int timeLimitInMinutes = section.getInt("timeLimitInMinutes");
            String bossInternalName = section.getString("bossInternalName");
            List<String> monsterPool = section.getStringList("monsterPool");

            HashMap<Integer, DungeonRoom> dungeonRooms = new HashMap<>();
            for (int roomIndex = 1; roomIndex <= 999; roomIndex++) {
                if (!section.contains("room" + roomIndex)) break;

                List<DungeonRoomDoor> dungeonRoomDoors = new ArrayList<>();
                for (int doorIndex = 1; doorIndex <= 999; doorIndex++) {
                    if (!section.contains("room" + roomIndex + ".door" + doorIndex)) break;

                    String materialStr = section.getString("room" + roomIndex + ".door" + doorIndex + ".material");
                    Material doorMaterial = Material.valueOf(materialStr);

                    float x1 = (float) section.getDouble("room" + roomIndex + ".door" + doorIndex + ".box" + ".x1");
                    float y1 = (float) section.getDouble("room" + roomIndex + ".door" + doorIndex + ".box" + ".y1");
                    float z1 = (float) section.getDouble("room" + roomIndex + ".door" + doorIndex + ".box" + ".z1");
                    float x2 = (float) section.getDouble("room" + roomIndex + ".door" + doorIndex + ".box" + ".x2");
                    float y2 = (float) section.getDouble("room" + roomIndex + ".door" + doorIndex + ".box" + ".y2");
                    float z2 = (float) section.getDouble("room" + roomIndex + ".door" + doorIndex + ".box" + ".z2");

                    BoundingBox boundingBox = new BoundingBox(x1, y1, z1, x2, y2, z2);

                    DungeonRoomDoor dungeonRoomDoor = new DungeonRoomDoor(doorMaterial, boundingBox);
                    dungeonRoomDoors.add(dungeonRoomDoor);
                }

                HashMap<Integer, List<DungeonRoomSpawner>> waveToSpawners = new HashMap<>();
                for (int waveIndex = 1; waveIndex <= 999; waveIndex++) {
                    if (!section.contains("room" + roomIndex + ".wave" + waveIndex)) break;

                    List<DungeonRoomSpawner> spawners = new ArrayList<>();
                    for (int spawnerIndex = 1; spawnerIndex <= 999; spawnerIndex++) {
                        if (!section.contains("room" + roomIndex + ".wave" + waveIndex + ".spawner" + spawnerIndex))
                            break;

                        String bossPath = "room" + roomIndex + ".wave" + waveIndex + ".spawner" + spawnerIndex + ".isBoss";
                        boolean isBoss = section.contains(bossPath) && section.getBoolean(bossPath);

                        int amount = section.getInt("room" + roomIndex + ".wave" + waveIndex + ".spawner" + spawnerIndex + ".amount");

                        float x = (float) section.getDouble("room" + roomIndex + ".wave" + waveIndex + ".spawner" + spawnerIndex + ".offset" + ".x");
                        float y = (float) section.getDouble("room" + roomIndex + ".wave" + waveIndex + ".spawner" + spawnerIndex + ".offset" + ".y");
                        float z = (float) section.getDouble("room" + roomIndex + ".wave" + waveIndex + ".spawner" + spawnerIndex + ".offset" + ".z");

                        Vector offset = new Vector(x, y, z);

                        DungeonRoomSpawner spawner = new DungeonRoomSpawner(amount, offset, isBoss);
                        spawners.add(spawner);
                    }

                    waveToSpawners.put(waveIndex, spawners);
                }

                List<RandomSkillOnGroundWithOffset> skillsOnGround = new ArrayList<>();
                for (int groundIndex = 1; groundIndex <= 999; groundIndex++) {
                    if (!section.contains("room" + roomIndex + ".skillOnGround" + groundIndex)) break;

                    float x = (float) section.getDouble("room" + roomIndex + ".skillOnGround" + groundIndex + ".loc" + ".x");
                    float y = (float) section.getDouble("room" + roomIndex + ".skillOnGround" + groundIndex + ".loc" + ".y");
                    float z = (float) section.getDouble("room" + roomIndex + ".skillOnGround" + groundIndex + ".loc" + ".z");

                    Vector vector = new Vector(x, y, z);

                    ArrayList<SkillOnGround> skillList = new ArrayList<>();
                    for (int skillIndex = 1; skillIndex <= 999; skillIndex++) {
                        if (!section.contains("room" + roomIndex + ".skillOnGround" + groundIndex + ".skill" + skillIndex))
                            break;

                        ConfigurationSection skillSection = section.getConfigurationSection("room" + roomIndex + ".skillOnGround" + groundIndex + ".skill" + skillIndex);
                        SkillOnGround skillOnGround = new SkillOnGround(skillSection);
                        skillList.add(skillOnGround);
                    }
                    RandomSkillOnGroundWithOffset skillOnGroundWithOffset = new RandomSkillOnGroundWithOffset(skillList, vector);

                    skillsOnGround.add(skillOnGroundWithOffset);
                }

                List<DungeonRoomLootChest> lootChests = new ArrayList<>();
                for (int groundIndex = 1; groundIndex <= 999; groundIndex++) {
                    if (!section.contains("room" + roomIndex + ".lootChest" + groundIndex)) break;

                    float x = (float) section.getDouble("room" + roomIndex + ".lootChest" + groundIndex + ".x");
                    float y = (float) section.getDouble("room" + roomIndex + ".lootChest" + groundIndex + ".y");
                    float z = (float) section.getDouble("room" + roomIndex + ".lootChest" + groundIndex + ".z");
                    float yaw = (float) section.getDouble("room" + roomIndex + ".lootChest" + groundIndex + ".yaw");
                    float pitch = (float) section.getDouble("room" + roomIndex + ".lootChest" + groundIndex + ".pitch");

                    Vector vector = new Vector(x, y, z);
                    LootChestTier lootChestTier = LootChestTier.fromLevel(levelReq);
                    lootChests.add(new DungeonRoomLootChest(lootChestTier, vector, yaw, pitch));
                }

                List<DungeonRoomInteractable> interactables = new ArrayList<>();
                for (int groundIndex = 1; groundIndex <= 999; groundIndex++) {
                    if (!section.contains("room" + roomIndex + ".interactable" + groundIndex)) break;

                    String mobKey = section.getString("room" + roomIndex + ".interactable" + groundIndex + ".mobKey");
                    float x = (float) section.getDouble("room" + roomIndex + ".interactable" + groundIndex + ".x");
                    float y = (float) section.getDouble("room" + roomIndex + ".interactable" + groundIndex + ".y");
                    float z = (float) section.getDouble("room" + roomIndex + ".interactable" + groundIndex + ".z");
                    float yaw = (float) section.getDouble("room" + roomIndex + ".interactable" + groundIndex + ".yaw");
                    float pitch = (float) section.getDouble("room" + roomIndex + ".interactable" + groundIndex + ".pitch");

                    Vector vector = new Vector(x, y, z);
                    interactables.add(new DungeonRoomInteractable(mobKey, vector, yaw, pitch));
                }

                List<Integer> nextRooms = section.getIntegerList("room" + roomIndex + ".nextRooms");

                DungeonRoom dungeonRoom = new DungeonRoom(nextRooms, dungeonRoomDoors, waveToSpawners, skillsOnGround,
                        lootChests, interactables);
                dungeonRooms.put(roomIndex, dungeonRoom);
            }

            List<Integer> startingRooms = section.getIntegerList("startingRooms");

            // Checkpoints
            List<Vector> checkpoints = new ArrayList<>();
            int checkpointCount = section.getInt("checkpoints.count");
            for (int checkpointNumber = 1; checkpointNumber <= checkpointCount; checkpointNumber++) {

                float xC = (float) section.getDouble("checkpoints.loc" + checkpointNumber + ".x");
                float yC = (float) section.getDouble("checkpoints.loc" + checkpointNumber + ".y");
                float zC = (float) section.getDouble("checkpoints.loc" + checkpointNumber + ".z");

                Vector vector = new Vector(xC, yC, zC);

                checkpoints.add(vector);
            }

            Vector prizeChestCenterOffset = new Vector(0, 0, 0);
            if (section.contains("prizeChestCenter.x")) {
                float x = (float) section.getDouble("prizeChestCenter.x");
                float y = (float) section.getDouble("prizeChestCenter.y");
                float z = (float) section.getDouble("prizeChestCenter.z");
                prizeChestCenterOffset = new Vector(x, y, z);
            }

            /*List<RandomSkillOnGroundWithOffset> skillsOnGround = new ArrayList<>();
            for (int groundIndex = 1; groundIndex <= 999; groundIndex++) {
                if (!section.contains("skillOnGround" + groundIndex)) break;

                float x = (float) section.getDouble("skillOnGround" + groundIndex + ".loc" + ".x");
                float y = (float) section.getDouble("skillOnGround" + groundIndex + ".loc" + ".y");
                float z = (float) section.getDouble("skillOnGround" + groundIndex + ".loc" + ".z");

                Vector vector = new Vector(x, y, z);

                ArrayList<SkillOnGround> skillList = new ArrayList<>();
                for (int skillIndex = 1; skillIndex <= 999; skillIndex++) {
                    if (!section.contains("skillOnGround" + groundIndex + ".skill" + skillIndex)) break;

                    ConfigurationSection skillSection = section.getConfigurationSection("skillOnGround" + groundIndex + ".skill" + skillIndex);
                    SkillOnGround skillOnGround = new SkillOnGround(skillSection);
                    skillList.add(skillOnGround);
                }
                RandomSkillOnGroundWithOffset skillOnGroundWithOffset = new RandomSkillOnGroundWithOffset(skillList, vector);

                skillsOnGround.add(skillOnGroundWithOffset);
            }*/

            float x1 = (float) section.getDouble("bossRoom" + ".x1");
            float y1 = (float) section.getDouble("bossRoom" + ".y1");
            float z1 = (float) section.getDouble("bossRoom" + ".z1");
            float x2 = (float) section.getDouble("bossRoom" + ".x2");
            float y2 = (float) section.getDouble("bossRoom" + ".y2");
            float z2 = (float) section.getDouble("bossRoom" + ".z2");

            BoundingBox bossRoomBox = new BoundingBox(x1, y1, z1, x2, y2, z2);

            DungeonTheme dungeonTheme = new DungeonTheme(code, name, gearLevel, portalColor, levelReq, timeLimitInMinutes,
                    monsterPool, bossInternalName, dungeonRooms, startingRooms, checkpoints, prizeChestCenterOffset, bossRoomBox);

            MiniGameManager.addDungeonTheme(code, dungeonTheme);
        }
    }

    private static void writeDungeonThemes() {
        HashMap<String, DungeonTheme> dungeonThemes = MiniGameManager.getDungeonThemes();

        for (String code : dungeonThemes.keySet()) {
            YamlConfiguration currentThemeConfig = dungeonThemesConfigs.get(code);

            if (currentThemeConfig == null) continue;

            DungeonTheme theme = dungeonThemes.get(code);

            currentThemeConfig.set("name", theme.getName().replaceAll("§", "&"));
            currentThemeConfig.set("gearLevel", theme.getGearLevel().ordinal());
            currentThemeConfig.set("portalColor", theme.getPortalColor().name());

            currentThemeConfig.set("levelReq", theme.getLevelReq());
            currentThemeConfig.set("timeLimitInMinutes", theme.getTimeLimitInMinutes());
            currentThemeConfig.set("bossInternalName", theme.getBossInternalName());
            currentThemeConfig.set("monsterPool", theme.getMonsterPool());

            Set<Integer> roomKeys = theme.getDungeonRoomKeys();
            for (int roomKey : roomKeys) {
                DungeonRoom dungeonRoom = theme.getDungeonRoom(roomKey);

                List<DungeonRoomDoor> doors = dungeonRoom.getDoors();
                int doorIndex = 1;
                for (DungeonRoomDoor door : doors) {

                    currentThemeConfig.set("room" + roomKey + ".door" + doorIndex + ".material", door.getMaterial().name());

                    BoundingBox boundingBox = door.getBoundingBox();

                    currentThemeConfig.set("room" + roomKey + ".door" + doorIndex + ".box" + ".x1", boundingBox.getMinX());
                    currentThemeConfig.set("room" + roomKey + ".door" + doorIndex + ".box" + ".y1", boundingBox.getMinY());
                    currentThemeConfig.set("room" + roomKey + ".door" + doorIndex + ".box" + ".z1", boundingBox.getMinZ());
                    currentThemeConfig.set("room" + roomKey + ".door" + doorIndex + ".box" + ".x2", boundingBox.getMaxX());
                    currentThemeConfig.set("room" + roomKey + ".door" + doorIndex + ".box" + ".y2", boundingBox.getMaxY());
                    currentThemeConfig.set("room" + roomKey + ".door" + doorIndex + ".box" + ".z2", boundingBox.getMaxZ());

                    doorIndex++;
                }

                HashMap<Integer, List<DungeonRoomSpawner>> waveToSpawners = dungeonRoom.getWaveToSpawners();
                for (int waveIndex : waveToSpawners.keySet()) {
                    List<DungeonRoomSpawner> dungeonRoomSpawners = waveToSpawners.get(waveIndex);

                    int spawnerIndex = 1;
                    for (DungeonRoomSpawner spawner : dungeonRoomSpawners) {
                        currentThemeConfig.set("room" + roomKey + ".wave" + waveIndex + ".spawner" + spawnerIndex + ".amount", spawner.getAmount());

                        Vector offset = spawner.getOffset();

                        currentThemeConfig.set("room" + roomKey + ".wave" + waveIndex + ".spawner" + spawnerIndex + ".offset" + ".x", offset.getX());
                        currentThemeConfig.set("room" + roomKey + ".wave" + waveIndex + ".spawner" + spawnerIndex + ".offset" + ".y", offset.getY());
                        currentThemeConfig.set("room" + roomKey + ".wave" + waveIndex + ".spawner" + spawnerIndex + ".offset" + ".z", offset.getZ());

                        spawnerIndex++;
                    }
                }

                List<RandomSkillOnGroundWithOffset> skillsOnGround = dungeonRoom.getSkillsOnGround();
                int skillIndex = 1;
                for (RandomSkillOnGroundWithOffset skillOnGround : skillsOnGround) {

                    Vector offset = skillOnGround.getOffset();

                    currentThemeConfig.set("room" + roomKey + ".skillOnGround" + skillIndex + ".loc" + ".x", offset.getX());
                    currentThemeConfig.set("room" + roomKey + ".skillOnGround" + skillIndex + ".loc" + ".y", offset.getY());
                    currentThemeConfig.set("room" + roomKey + ".skillOnGround" + skillIndex + ".loc" + ".z", offset.getZ());

                    skillIndex++;
                }

                List<DungeonRoomLootChest> lootChests = dungeonRoom.getLootChests();
                int lootChestIndex = 1;
                for (DungeonRoomLootChest lootChest : lootChests) {

                    Vector offset = lootChest.getOffset();

                    currentThemeConfig.set("room" + roomKey + ".lootChest" + lootChestIndex + ".x", offset.getX());
                    currentThemeConfig.set("room" + roomKey + ".lootChest" + lootChestIndex + ".y", offset.getY());
                    currentThemeConfig.set("room" + roomKey + ".lootChest" + lootChestIndex + ".z", offset.getZ());
                    currentThemeConfig.set("room" + roomKey + ".lootChest" + lootChestIndex + ".yaw", lootChest.getYaw());
                    currentThemeConfig.set("room" + roomKey + ".lootChest" + lootChestIndex + ".pitch", lootChest.getPitch());

                    lootChestIndex++;
                }

                List<DungeonRoomInteractable> interactables = dungeonRoom.getInteractables();
                int interactableIndex = 1;
                for (DungeonRoomInteractable interactable : interactables) {
                    Vector offset = interactable.getOffset();

                    currentThemeConfig.set("room" + roomKey + ".interactable" + interactableIndex + ".mobKey", interactable.getMobKey());
                    currentThemeConfig.set("room" + roomKey + ".interactable" + interactableIndex + ".x", offset.getX());
                    currentThemeConfig.set("room" + roomKey + ".interactable" + interactableIndex + ".y", offset.getY());
                    currentThemeConfig.set("room" + roomKey + ".interactable" + interactableIndex + ".z", offset.getZ());
                    currentThemeConfig.set("room" + roomKey + ".interactable" + interactableIndex + ".yaw", interactable.getYaw());
                    currentThemeConfig.set("room" + roomKey + ".interactable" + interactableIndex + ".pitch", interactable.getPitch());

                    interactableIndex++;
                }

                currentThemeConfig.set("room" + roomKey + ".nextRooms", dungeonRoom.getNextRooms());
            }

            currentThemeConfig.set("startingRooms", theme.getStartingRooms());

            // Checkpoints
            List<Vector> checkpointOffsets = theme.getCheckpointOffsets();
            currentThemeConfig.set("checkpoints.count", checkpointOffsets.size());
            int checkpointNumber = 1;
            for (Vector checkpointOffset : checkpointOffsets) {
                currentThemeConfig.set("checkpoints.loc" + checkpointNumber + ".x", checkpointOffset.getX());
                currentThemeConfig.set("checkpoints.loc" + checkpointNumber + ".y", checkpointOffset.getY());
                currentThemeConfig.set("checkpoints.loc" + checkpointNumber + ".z", checkpointOffset.getZ());

                checkpointNumber++;
            }

            Vector prizeChestCenterOffset = theme.getPrizeChestCenterOffset();
            currentThemeConfig.set("prizeChestCenter.x", prizeChestCenterOffset.getX());
            currentThemeConfig.set("prizeChestCenter.y", prizeChestCenterOffset.getY());
            currentThemeConfig.set("prizeChestCenter.z", prizeChestCenterOffset.getZ());

            /*List<RandomSkillOnGroundWithOffset> skillsOnGround = theme.getSkillsOnGround();
            int skillIndex = 1;
            for (RandomSkillOnGroundWithOffset skillOnGround : skillsOnGround) {

                Vector offset = skillOnGround.getOffset();

                currentThemeConfig.set("skillOnGround" + skillIndex + ".loc" + ".x", offset.getX());
                currentThemeConfig.set("skillOnGround" + skillIndex + ".loc" + ".y", offset.getY());
                currentThemeConfig.set("skillOnGround" + skillIndex + ".loc" + ".z", offset.getZ());

                skillIndex++;
            }*/

            BoundingBox bossRoomBox = theme.getBossRoomBox();
            currentThemeConfig.set("bossRoom" + ".x1", bossRoomBox.getMinX());
            currentThemeConfig.set("bossRoom" + ".y1", bossRoomBox.getMinY());
            currentThemeConfig.set("bossRoom" + ".z1", bossRoomBox.getMinZ());
            currentThemeConfig.set("bossRoom" + ".x2", bossRoomBox.getMaxX());
            currentThemeConfig.set("bossRoom" + ".y2", bossRoomBox.getMaxY());
            currentThemeConfig.set("bossRoom" + ".z2", bossRoomBox.getMaxZ());

            try {
                currentThemeConfig.save(themePath + File.separator + code + ".yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadDungeonGates() {
        HashMap<String, DungeonTheme> dungeonThemes = MiniGameManager.getDungeonThemes();
        for (String code : dungeonThemes.keySet()) {
            String worldString = dungeonGatesConfig.getString(code + ".world");
            World world = Bukkit.getWorld(worldString);
            float x = (float) dungeonGatesConfig.getDouble(code + ".x");
            float y = (float) dungeonGatesConfig.getDouble(code + ".y");
            float z = (float) dungeonGatesConfig.getDouble(code + ".z");
            float yaw = (float) dungeonGatesConfig.getDouble(code + ".yaw");
            float pitch = (float) dungeonGatesConfig.getDouble(code + ".pitch");
            Location location = new Location(world, x, y, z, yaw, pitch);
            MiniGameManager.addDungeonPortal(location, code);
        }
    }

    private static void loadInstances() {
        HashMap<String, DungeonTheme> dungeonThemes = MiniGameManager.getDungeonThemes();
        for (String themeCode : dungeonThemes.keySet()) {
            int roomCount = ConfigurationUtils.getChildComponentCount(dungeonInstancesConfig, themeCode);

            for (int i = 1; i <= roomCount; i++) {
                String code = themeCode + i;
                String worldString = dungeonInstancesConfig.getString(code + ".start.world");
                World world = Bukkit.getWorld(worldString);
                float x = (float) dungeonInstancesConfig.getDouble(code + ".start.x");
                float y = (float) dungeonInstancesConfig.getDouble(code + ".start.y");
                float z = (float) dungeonInstancesConfig.getDouble(code + ".start.z");
                float yaw = (float) dungeonInstancesConfig.getDouble(code + ".start.yaw");
                float pitch = (float) dungeonInstancesConfig.getDouble(code + ".start.pitch");
                Location start = new Location(world, x, y, z, yaw, pitch);

                List<Location> locations = new ArrayList<>();
                locations.add(start);

                DungeonTheme dungeonTheme = dungeonThemes.get(themeCode);

                List<Vector> checkpointOffsets = dungeonTheme.getCheckpointOffsets();

                List<Checkpoint> checkpoints = new ArrayList<>();
                for (Vector offset : checkpointOffsets) {
                    Location add = start.clone().add(offset);
                    Checkpoint checkpoint = new Checkpoint(add);
                    checkpoints.add(checkpoint);
                }

                DungeonInstance dungeonInstance = new DungeonInstance(dungeonTheme, i, locations, checkpoints);
                MiniGameManager.addDungeonInstance(themeCode, i, dungeonInstance);
            }
        }
    }

    private static void writeInstances(String fileName) {
        HashMap<String, DungeonTheme> dungeonThemes = MiniGameManager.getDungeonThemes();

        for (String themeCode : dungeonThemes.keySet()) {
            for (int i = 1; i < 999; i++) {
                if (MiniGameManager.dungeonInstanceExists(themeCode, i)) {
                    DungeonInstance dungeonInstance = MiniGameManager.getDungeonInstance(themeCode, i);

                    String code = themeCode + i;

                    Location startLocation = dungeonInstance.getStartLocation(1);
                    dungeonInstancesConfig.set(code + ".start.world", startLocation.getWorld().getName());
                    dungeonInstancesConfig.set(code + ".start.x", startLocation.getX());
                    dungeonInstancesConfig.set(code + ".start.y", startLocation.getY());
                    dungeonInstancesConfig.set(code + ".start.z", startLocation.getZ());
                    dungeonInstancesConfig.set(code + ".start.yaw", startLocation.getYaw());
                    dungeonInstancesConfig.set(code + ".start.pitch", startLocation.getPitch());
                } else {
                    break;
                }
            }
        }

        try {
            dungeonInstancesConfig.save(basePath + File.separator + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
