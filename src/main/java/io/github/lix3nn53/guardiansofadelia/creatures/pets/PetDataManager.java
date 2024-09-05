package io.github.lix3nn53.guardiansofadelia.creatures.pets;

import java.util.HashMap;
import java.util.Set;

public class PetDataManager {

    private static final HashMap<String, PetData> petToData = new HashMap<>();

    public static void put(String key, PetData petData) {
        petToData.put(key, petData);
    }

    public static PetData getPetData(String key) {
        return petToData.get(key);
    }

    public static Set<String> getKeys() {
        return petToData.keySet();
    }
}
