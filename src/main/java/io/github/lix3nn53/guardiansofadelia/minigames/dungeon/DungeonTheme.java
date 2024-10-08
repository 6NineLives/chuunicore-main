package io.github.lix3nn53.guardiansofadelia.minigames.dungeon;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.MMManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.element.ElementType;
import io.github.lix3nn53.guardiansofadelia.items.DungeonPrizeChest;
import io.github.lix3nn53.guardiansofadelia.items.GearLevel;
import io.github.lix3nn53.guardiansofadelia.items.PrizeChestType;
import io.github.lix3nn53.guardiansofadelia.items.RpgGears.ItemTier;
import io.github.lix3nn53.guardiansofadelia.menu.GuiDungeonJoin;
import io.github.lix3nn53.guardiansofadelia.minigames.MiniGameManager;
import io.github.lix3nn53.guardiansofadelia.minigames.dungeon.room.DungeonRoom;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.transportation.portals.PortalColor;
import io.github.lix3nn53.guardiansofadelia.utilities.ItemPoolGenerator;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import io.lumine.mythic.api.mobs.MobManager;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;

public class DungeonTheme {
    private final String code;
    private final String name;
    private final GearLevel gearLevel;
    private final PortalColor portalColor;

    private final int levelReq;
    private final int timeLimitInMinutes;

    private final List<String> monsterPool;
    private final String bossInternalName;

    // Rooms
    private final HashMap<Integer, DungeonRoom> dungeonRooms;
    private final List<Integer> startingRooms;

    private final List<Vector> checkpointOffsets;
    private Vector prizeChestCenterOffset;

    // Boss Room
    private BoundingBox bossRoomBox;

    public DungeonTheme(String code, String name, GearLevel gearLevel, PortalColor portalColor, int levelReq,
                        int timeLimitInMinutes, List<String> monsterPool, String bossInternalName, HashMap<Integer, DungeonRoom> dungeonRooms,
                        List<Integer> startingRooms, List<Vector> checkpoints, Vector prizeChestCenterOffset, BoundingBox bossRoomBox) {
        this.code = code;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.gearLevel = gearLevel;
        this.portalColor = portalColor;
        this.levelReq = levelReq;
        this.timeLimitInMinutes = timeLimitInMinutes;
        this.monsterPool = monsterPool;
        this.bossInternalName = bossInternalName;
        this.dungeonRooms = dungeonRooms;
        this.startingRooms = Collections.unmodifiableList(startingRooms);
        this.checkpointOffsets = checkpoints;
        this.prizeChestCenterOffset = prizeChestCenterOffset;
        this.bossRoomBox = bossRoomBox;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public GearLevel getGearLevel() {
        return gearLevel;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public int getTimeLimitInMinutes() {
        return timeLimitInMinutes;
    }

    public List<String> getMonsterPool() {
        return monsterPool;
    }

    public String getBossInternalName() {
        return bossInternalName;
    }

    public List<Vector> getCheckpointOffsets() {
        return checkpointOffsets;
    }

    public void addCheckpointOffset(Vector offset) {
        checkpointOffsets.add(offset);
    }

    public String getBossName() {
        MobManager mobManager = MythicBukkit.inst().getMobManager();
        Optional<MythicMob> mythicMob = mobManager.getMythicMob(bossInternalName);

        if (mythicMob.isPresent()) {
            return mythicMob.get().getDisplayName().get();
        }

        throw new IllegalArgumentException("DungeonTheme Boss: MythicMob not found: " + bossInternalName);
    }

    public DungeonPrizeChest getPrizeChest() {
        PrizeChestType[] values = PrizeChestType.values();
        int random = GuardiansOfAdelia.RANDOM.nextInt(values.length);

        PrizeChestType chestType = values[random];

        return new DungeonPrizeChest(this, chestType);
    }

    /**
     * @param type 0 for Weapon, 1 for Jewelry, 2 for Armor
     */
    public List<ItemStack> generateChestItems(PrizeChestType type) {
        ArrayList<ItemStack> chestItems = new ArrayList<>();

        List<ItemStack> lowTierItems = new ArrayList<>();
        List<ItemStack> highTierItems = new ArrayList<>();

        switch (type) {
            case WEAPON_MELEE -> {
                lowTierItems = ItemPoolGenerator.generateWeapons(ItemTier.MYSTIC, gearLevel, true, false, true);
                highTierItems = ItemPoolGenerator.generateWeapons(ItemTier.LEGENDARY, gearLevel, true, false, true);
            }
            case WEAPON_RANGED -> {
                lowTierItems = ItemPoolGenerator.generateWeapons(ItemTier.MYSTIC, gearLevel, false, false, true);
                highTierItems = ItemPoolGenerator.generateWeapons(ItemTier.LEGENDARY, gearLevel, false, false, true);
            }
            case ARMOR_HEAVY -> {
                lowTierItems = ItemPoolGenerator.generateArmors(ItemTier.MYSTIC, gearLevel, true, false, true);
                highTierItems = ItemPoolGenerator.generateArmors(ItemTier.LEGENDARY, gearLevel, true, false, true);
            }
            case ARMOR_LIGHT -> {
                lowTierItems = ItemPoolGenerator.generateArmors(ItemTier.MYSTIC, gearLevel, false, false, true);
                highTierItems = ItemPoolGenerator.generateArmors(ItemTier.LEGENDARY, gearLevel, false, false, true);
            }
            case JEWELRY -> {
                lowTierItems = ItemPoolGenerator.generatePassives(ItemTier.MYSTIC, gearLevel, false);
                highTierItems = ItemPoolGenerator.generatePassives(ItemTier.LEGENDARY, gearLevel, false);
            }
            case PET -> {
                lowTierItems = ItemPoolGenerator.generateEggs(gearLevel, 1);
                highTierItems = ItemPoolGenerator.generateEggs(gearLevel, 1);
            }
        }

        chestItems.addAll(lowTierItems);
        chestItems.addAll(lowTierItems);
        chestItems.addAll(highTierItems);

        return chestItems;
    }

    public GuiGeneric getJoinQueueGui(Player player) {
        GuardianData guardianData = GuardianDataManager.getGuardianData(player);
        String language = guardianData.getLanguage();
        List<ItemStack> instanceItems = new ArrayList<>();

        for (int i = 1; i < 100; i++) {
            DungeonInstance dungeonInstance = MiniGameManager.getDungeonInstance(code, i);
            if (dungeonInstance == null) {
                break;
            }
            ItemStack itemStack = generateInstanceItem(dungeonInstance, language);
            instanceItems.add(itemStack);
        }

        return new GuiDungeonJoin(name, code, instanceItems);
    }


    private ItemStack generateInstanceItem(DungeonInstance dungeonInstance, String language) {
        ItemStack room = new ItemStack(Material.LIME_WOOL);
        ItemMeta itemMeta = room.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.BLUE_LIGHT + getName() + " #" + dungeonInstance.getInstanceNo() + " (" + dungeonInstance.getPlayersInGameSize() + "/" + dungeonInstance.getMaxPlayerSize() + ")");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.YELLOW + "Level req: " + ChatPalette.WHITE + dungeonInstance.getLevelReq());
        lore.add(ChatPalette.PURPLE_LIGHT + "Time limit: " + ChatPalette.WHITE + dungeonInstance.getTimeLimitInMinutes() + " minute(s)");
        lore.add("");
        lore.add(ChatPalette.RED + "BOSS: " + ChatPalette.WHITE + getBossName());

        // Resistance
        List<String> weakness = new ArrayList<>();
        for (ElementType type : ElementType.values()) {
            if (MMManager.hasElementResistance(bossInternalName, type)) {
                float resistance = MMManager.getElementResistance(bossInternalName, type);

                if (resistance < 1) {
                    int percent = (int) (100 * (-(1d - resistance)));

                    lore.add(ChatPalette.BLUE_LIGHT + "Resistance: " + type.getFullName(language) + " - " + -percent + "%");
                } else {
                    int percent = (int) (100 * (1d - resistance));

                    weakness.add(ChatPalette.RED + "Weakness: " + type.getFullName(language) + " - " + -percent + "%");
                }
            }
        }
        lore.addAll(weakness);

        if (dungeonInstance.isInGame()) {
            lore.add("");
            lore.add(ChatPalette.GOLD + "Players in dungeon: ");
            for (Player player : dungeonInstance.getPlayersInGame()) {
                lore.add(player.getDisplayName());
            }
        } else {
            lore.add("");
            lore.add(ChatPalette.GRAY + "Click to join this dungeon room!");
        }
        itemMeta.setLore(lore);
        room.setItemMeta(itemMeta);

        if (dungeonInstance.isInGame()) {
            room.setType(Material.RED_WOOL);
        }
        room.setItemMeta(itemMeta);

        return room;
    }

    public PortalColor getPortalColor() {
        return portalColor;
    }

    public Set<Integer> getDungeonRoomKeys() {
        return dungeonRooms.keySet();
    }

    public DungeonRoom getDungeonRoom(int key) {
        return dungeonRooms.get(key);
    }

    public void addDungeonRoom(int key, DungeonRoom room) {
        dungeonRooms.put(key, room);
    }

    public List<Integer> getStartingRooms() {
        return startingRooms;
    }

    public Vector getPrizeChestCenterOffset() {
        return prizeChestCenterOffset;
    }

    public void setPrizeChestCenterOffset(Vector prizeChestCenterOffset) {
        this.prizeChestCenterOffset = prizeChestCenterOffset;
    }

    public String getRandomMonsterToSpawn(int darkness) {
        if (darkness < 0) {
            darkness = 1;
        }

        int size = monsterPool.size();
        float darknessPercent = darkness / 100f;

        int totalWeight = 0;
        List<Integer> weights = new ArrayList<>();
        for (int i = 1; i <= size; i++) { // start from 1 so first index has a percent and last one is 100
            float indexPercent = i / (float) size;
            float diff = Math.abs(indexPercent - darknessPercent);
            float diffReverse = 1 - diff;

            int weight = (int) (10 * diffReverse * diffReverse * diffReverse + 0.5); // to make differences between indexes larger
            totalWeight += weight;
            weights.add(weight);
        }

        float randomWeight = (float) (Math.random()) * totalWeight;

        int weightCounter = 0;
        for (int i = 0; i <= size; i++) {
            weightCounter += weights.get(i);
            if (weightCounter >= randomWeight) {
                return monsterPool.get(i);
            }
        }

        return monsterPool.get(0);
    }

    public int getMonsterLevel(int darkness) {
        if (darkness < 0) {
            darkness = 1;
        } else if (darkness > 99) {
            darkness = 99; // not 100 because of the rounding
        }

        int maxLevel = 10;

        return (int) ((darkness / 100d) * maxLevel) + 1;
    }

    public boolean isPlayerInBossRoom(Location dungeonStart, Player player) {
        return isLocationInBossRoom(dungeonStart, player.getLocation());
    }

    public boolean isLocationInBossRoom(Location dungeonStart, Location location) {
        BoundingBox shift = this.bossRoomBox.clone().shift(dungeonStart);

        Vector vector = location.toVector();
        return shift.contains(vector);
    }

    public BoundingBox getBossRoomBox() {
        return bossRoomBox;
    }

    public void setBossRoomBox(BoundingBox bossRoomBox) {
        this.bossRoomBox = bossRoomBox;
    }
}
