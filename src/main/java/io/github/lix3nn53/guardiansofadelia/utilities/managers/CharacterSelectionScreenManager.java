package io.github.lix3nn53.guardiansofadelia.utilities.managers;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.database.DatabaseManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.npc.QuestNPCManager;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.utilities.hologram.Hologram;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class CharacterSelectionScreenManager {

    private static final HashMap<Player, HashMap<Integer, Location>> charLocationsForSelection = new HashMap<>();
    private static final HashMap<Player, HashMap<Integer, Integer>> charLevelsForSelection = new HashMap<>();
    private static final List<Player> players = new ArrayList<>();
    private static List<Location> armorStandLocationBases;
    private static Location tutorialStart;
    public static Location characterSelectionCenter;
    private static final HashMap<Integer, List<ArmorStand>> characterNoToArmorStands = new HashMap<>();

    private static final List<Player> playersInLoading = new ArrayList<>(); // Player clicked something and waiting for result

    private static final HashMap<Player, Integer> playerToRemoveConfirmation = new HashMap<>();

    public static void setArmorStandLocationBases(List<Location> armorStandLocationBases) {
        CharacterSelectionScreenManager.armorStandLocationBases = armorStandLocationBases;
    }

    private static void createHolograms() {
        //remove old armorStands since chunk events doesn't work for them. Because spawn chunks are always loaded.
        BoundingBox boundingBox = new BoundingBox(-2750, 41, 6108, -2772, 53, 6130);
        Predicate<Entity> predicate = entity -> entity.getType().equals(EntityType.ARMOR_STAND);
        Collection<Entity> oldArmorStands = Bukkit.getWorld("world").getNearbyEntities(boundingBox, predicate);
        for (Entity entity : oldArmorStands) {
            entity.remove();
        }

        //create new armorStands
        int i = 1;
        for (Location location : armorStandLocationBases) {
            List<ArmorStand> holoList = new ArrayList<>();
            Location clone = location.clone();

            holoList.add(new Hologram(clone.add(0.0, 0.4, 0.0), ChatPalette.GREEN_DARK + "create new character").getArmorStand());
            holoList.add(new Hologram(clone.add(0.0, 0.3, 0.0), ChatPalette.GREEN_DARK + "Right click NPC to").getArmorStand());
            holoList.add(new Hologram(clone.add(0.0, 0.3, 0.0), ChatPalette.GOLD + "Character-slot " + ChatPalette.YELLOW + i).getArmorStand());

            characterNoToArmorStands.put(i, holoList);
            i++;
        }
    }

    public static void startCharacterSelection(Player player, boolean isJoin) {
        if (players.isEmpty()) {
            createHolograms();
        }
        players.add(player);
        DatabaseManager.loadPlayerDataAndCharacterSelection(player, isJoin);
        if (isJoin) {
            player.teleport(characterSelectionCenter);
        }
    }

    private static void removeDisguisesFromPlayer(Player player) {
        for (int charNo = 1; charNo <= 8; charNo++) {
            if (characterNoToArmorStands.containsKey(charNo)) {
                List<ArmorStand> armorStands = characterNoToArmorStands.get(charNo);
                for (ArmorStand armorStand : armorStands) {
                    Disguise disguise = DisguiseAPI.getDisguise(player, armorStand);
                    if (disguise != null) {
                        disguise.removeDisguise();
                    }
                }
            }
        }
    }

    private static void removeHolograms() {
        for (int charNo = 1; charNo <= 8; charNo++) {
            if (characterNoToArmorStands.containsKey(charNo)) {
                List<ArmorStand> armorStands = characterNoToArmorStands.get(charNo);
                for (ArmorStand armorStand : armorStands) {
                    armorStand.remove();
                }
                characterNoToArmorStands.remove(charNo);
            }
        }
    }

    public static HashMap<Integer, List<ArmorStand>> getCharacterNoToArmorStands() {
        return characterNoToArmorStands;
    }

    public static void selectCharacter(Player player, int charNo, Location location) {
        if (playersInLoading.contains(player)) return;
        playersInLoading.add(player);

        player.sendMessage(ChatPalette.YELLOW + "Loading character-" + charNo);
        DatabaseManager.loadCharacter(player, charNo, location);
        clear(player);
    }

    public static void clear(Player player) {
        removeDisguisesFromPlayer(player);
        players.remove(player);
        charLocationsForSelection.remove(player);
        charLevelsForSelection.remove(player);
        playerToRemoveConfirmation.remove(player);
        if (players.isEmpty()) {
            removeHolograms();
        }
    }

    public static Location getCharacterSelectionCenter() {
        return characterSelectionCenter;
    }

    public static void setCharacterSelectionCenter(Location characterSelectionCenter) {
        CharacterSelectionScreenManager.characterSelectionCenter = characterSelectionCenter;
    }

    public static Location getTutorialStart() {
        return tutorialStart;
    }

    public static void setTutorialStart(Location tutorialStart) {
        CharacterSelectionScreenManager.tutorialStart = tutorialStart;
    }

    public static void createCharacter(Player player, int charNo, String rpgClassStr) {
        if (playersInLoading.contains(player)) return;
        playersInLoading.add(player);

        player.sendMessage(ChatPalette.YELLOW + "Creating character-" + charNo);
        clear(player);
        //start tutorial
        TutorialManager.startTutorial(player, charNo, tutorialStart, rpgClassStr);
    }

    public static void createCharacterWithoutTutorial(Player player, int charNo, String rpgClassStr) {
        if (playersInLoading.contains(player)) return;
        playersInLoading.add(player);

        player.sendMessage(ChatPalette.YELLOW + "Creating character-" + charNo);
        clear(player);
        //start character at first world quest
        GuardianData guardianData = GuardianDataManager.getGuardianData(player);

        RPGCharacter rpgCharacter = new RPGCharacter(rpgClassStr, player);
        guardianData.setActiveCharacter(rpgCharacter, charNo);

        // last quest of tutorial
        Quest questCopyById = QuestNPCManager.getQuestCopyById(7);

        boolean accept = rpgCharacter.acceptQuest(questCopyById, player, -1);
        questCopyById.onComplete(player);

        RPGClassStats rpgClassStats = rpgCharacter.getRPGClassStats();
        rpgCharacter.getRpgCharacterStats().recalculateEquipment(rpgCharacter.getRpgClassStr(), rpgClassStats);
        rpgCharacter.getRpgCharacterStats().recalculateRPGInventory(rpgCharacter.getRpgInventory(), rpgClassStats);

        RPGCharacterStats rpgCharacterStats = rpgCharacter.getRpgCharacterStats();
        rpgCharacterStats.setCurrentHealth(rpgCharacterStats.getTotalMaxHealth(rpgClassStats));
        rpgCharacterStats.setCurrentMana(rpgCharacterStats.getTotalMaxMana(rpgClassStats), rpgClassStats);

        Bukkit.getScheduler().runTaskLater(GuardiansOfAdelia.getInstance(), () -> CharacterSelectionScreenManager.onLoadingDone(player), 200L);
    }


    public static void setCharLocation(Player player, int charNo, Location location) {
        HashMap<Integer, Location> integerLocationHashMap = new HashMap<>();
        if (charLocationsForSelection.containsKey(player)) {
            integerLocationHashMap = charLocationsForSelection.get(player);
        }
        integerLocationHashMap.put(charNo, location);
        charLocationsForSelection.put(player, integerLocationHashMap);
    }

    public static void setCharLevel(Player player, int charNo, int level) {
        HashMap<Integer, Integer> integerLevelHashMap = new HashMap<>();
        if (charLevelsForSelection.containsKey(player)) {
            integerLevelHashMap = charLevelsForSelection.get(player);
        }
        integerLevelHashMap.put(charNo, level);
        charLevelsForSelection.put(player, integerLevelHashMap);
    }

    /**
     * @return last leave location if valid else null
     */
    public static Location getCharLocation(Player player, int charNo) {
        if (charLocationsForSelection.containsKey(player)) {
            HashMap<Integer, Location> integerLocationHashMap = charLocationsForSelection.get(player);
            if (integerLocationHashMap.containsKey(charNo)) {
                Location location = integerLocationHashMap.get(charNo);
                if (location.getWorld().getName().equals("world")) {
                    return location;
                }
            }
        }
        return null;
    }

    public static int getCharLevel(Player player, int charNo) {
        if (charLevelsForSelection.containsKey(player)) {
            HashMap<Integer, Integer> integerLevelHashMap = charLevelsForSelection.get(player);
            if (integerLevelHashMap.containsKey(charNo)) {
                return integerLevelHashMap.get(charNo);
            }
        }
        return 1;
    }

    public static boolean isPlayerInCharSelection(Player player) {
        return players.contains(player);
    }

    public static void onLoadingDone(Player player) {
        playersInLoading.remove(player);
    }

    public static void setToBeRemoved(Player player, int charNo) {
        playerToRemoveConfirmation.put(player, charNo);
        player.sendMessage(ChatPalette.RED + "Are you sure you want to delete character-" + charNo + "?");
        player.sendMessage(ChatPalette.RED + "Type '/character remove " + charNo + "' to confirm.");
        player.sendMessage(ChatPalette.RED_DARK + "WARNING: This action cannot be undone.");
    }

    public static void confirmRemove(Player player, int charNo) {
        if (!players.contains(player)) {
            player.sendMessage(ChatPalette.RED + "You must be in the character selection.");
            return;
        }

        if (playerToRemoveConfirmation.containsKey(player)) {
            int charNoSaved = playerToRemoveConfirmation.get(player);
            if (charNo != charNoSaved) {
                player.sendMessage(ChatPalette.RED + "Character number mismatch.");
                return;
            }
            playersInLoading.add(player);
            playerToRemoveConfirmation.remove(player);

            new BukkitRunnable() {

                @Override
                public void run() {
                    DatabaseManager.clearCharacter(player, charNo);
                    charLocationsForSelection.get(player).remove(charNo);
                    charLevelsForSelection.get(player).remove(charNo);
                    playersInLoading.remove(player);
                }
            }.runTaskAsynchronously(GuardiansOfAdelia.getInstance());

            if (characterNoToArmorStands.containsKey(charNo)) {
                List<ArmorStand> armorStands = characterNoToArmorStands.get(charNo);
                for (ArmorStand armorStand : armorStands) {
                    Disguise disguise = DisguiseAPI.getDisguise(player, armorStand);
                    if (disguise != null) {
                        disguise.removeDisguise();
                    }
                }
            }

            player.sendMessage(ChatPalette.RED + "Character-" + charNo + " deleted.");
        }
    }
}