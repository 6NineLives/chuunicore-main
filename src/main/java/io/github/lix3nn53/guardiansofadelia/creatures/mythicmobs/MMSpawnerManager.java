package io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs;

import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawner;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.spawner.MMSpawnerOpenWorld;
import io.github.lix3nn53.guardiansofadelia.utilities.LocationUtils;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MMSpawnerManager {

    private static final HashMap<String, List<MMSpawnerOpenWorld>> chunkKeyToSpawners = new HashMap<>();
    private static final HashMap<LivingEntity, MMSpawner> spawnedToSpawners = new HashMap<>();

    public static void onChunkLoad(String chunkKey) {
        if (chunkKeyToSpawners.containsKey(chunkKey)) {
            List<MMSpawnerOpenWorld> lootChests = chunkKeyToSpawners.get(chunkKey);

            for (MMSpawnerOpenWorld spawner : lootChests) {
                spawner.spawn();
            }
        }
    }

    public static void onChunkUnload(String chunkKey) {
        if (chunkKeyToSpawners.containsKey(chunkKey)) {
            List<MMSpawnerOpenWorld> openWorldSpawners = chunkKeyToSpawners.get(chunkKey);

            for (MMSpawnerOpenWorld spawner : openWorldSpawners) {
                spawner.despawn();
            }
        }
    }

    public static MMSpawner getSpawnerOfEntity(LivingEntity livingEntity) {
        return spawnedToSpawners.get(livingEntity);
    }

    public static void addGlobalSpawner(MMSpawnerOpenWorld openWorldSpawner) {
        String chunkKey = LocationUtils.getChunkKey(openWorldSpawner.getLocation());
        if (chunkKeyToSpawners.containsKey(chunkKey)) {
            List<MMSpawnerOpenWorld> list = chunkKeyToSpawners.get(chunkKey);
            list.add(openWorldSpawner);
            chunkKeyToSpawners.put(chunkKey, list);
        } else {
            List<MMSpawnerOpenWorld> holograms = new ArrayList<>();
            holograms.add(openWorldSpawner);
            chunkKeyToSpawners.put(chunkKey, holograms);
        }
    }

    public static void clear() {
        chunkKeyToSpawners.clear();
        spawnedToSpawners.clear();
    }

    public static HashMap<String, List<MMSpawnerOpenWorld>> getChunkKeyToSpawners() {
        return chunkKeyToSpawners;
    }

    public static void onSpawn(MMSpawner spawner) {
        spawnedToSpawners.put(spawner.getSpawned(), spawner);
    }

    public static void onDespawn(LivingEntity livingEntity) {
        spawnedToSpawners.remove(livingEntity);
    }
}
