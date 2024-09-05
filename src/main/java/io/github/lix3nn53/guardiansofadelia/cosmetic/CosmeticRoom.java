package io.github.lix3nn53.guardiansofadelia.cosmetic;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.Cosmetic;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticColor;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticSlot;
import io.github.lix3nn53.guardiansofadelia.cosmetic.inner.CosmeticType;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CosmeticRoom {

    private static final HashMap<Player, CosmeticRoomData> players = new HashMap<>();
    private static ArmorStand armorStand;
    private static ArmorStand armorStandTop;
    private static Location location;
    private static Location tpLocation;

    public static Location getTpLocation() {
        return tpLocation;
    }

    public static void setTpLocation(Location location) {
        CosmeticRoom.tpLocation = location;
    }

    public static void setLocation(Location location) {
        CosmeticRoom.location = location;
    }

    public static void setCosmetic(Player player, int cosmeticId, CosmeticColor color, int tintIndex) {
        Cosmetic cosmetic = CosmeticManager.get(cosmeticId);
        CosmeticType cosmeticType = cosmetic.getType();
        EquipmentSlot equipmentSlot = cosmeticType.getEquipmentSlot();
        boolean onPlayer = cosmeticType.isOnPlayer();
        ItemStack showcase = cosmetic.getShowcase(color, tintIndex);

        if (onPlayer) {
            PlayerDisguise disguise;
            if (DisguiseAPI.isDisguised(player, armorStand)) {
                disguise = (PlayerDisguise) DisguiseAPI.getDisguise(player, armorStand);
            } else {
                disguise = new PlayerDisguise(player);
            }

            PlayerWatcher watcher = disguise.getWatcher();
            watcher.setNoGravity(true);
            watcher.setItemStack(equipmentSlot, showcase);

            DisguiseAPI.disguiseToPlayers(armorStand, disguise, player);
        } else {
            MobDisguise disguise;
            if (DisguiseAPI.isDisguised(player, armorStandTop)) {
                disguise = (MobDisguise) DisguiseAPI.getDisguise(player, armorStandTop);
            } else {
                disguise = new MobDisguise(DisguiseType.ARMOR_STAND);
            }

            LivingWatcher watcher = disguise.getWatcher();
            watcher.setNoGravity(true);
            watcher.setItemStack(equipmentSlot, showcase);

            DisguiseAPI.disguiseToPlayers(armorStandTop, disguise, player);
        }

        CosmeticRoomData cosmeticRoomData = players.get(player);
        cosmeticRoomData.setCosmetic(cosmeticType.getCosmeticSlot(), cosmeticId, color, tintIndex);
    }

    public static void start(Player player, Location backLocation) {
        CosmeticRoomData cosmeticRoomData = new CosmeticRoomData(backLocation);

        if (armorStand == null || !armorStand.isValid()) {
            armorStand = location.getWorld().spawn(location, ArmorStand.class);
            armorStand.setVisible(true);
            armorStand.setGravity(false);
            armorStand.setInvulnerable(true);
            rotate();
        }

        if (armorStandTop == null || !armorStandTop.isValid()) {
            armorStandTop = location.getWorld().spawn(location, ArmorStand.class);
            armorStandTop.setVisible(false);
            armorStandTop.setGravity(false);
            armorStandTop.setInvulnerable(true);
        }

        armorStand.addPassenger(armorStandTop);

        PlayerDisguise disguise = new PlayerDisguise(player);

        PlayerWatcher watcher = disguise.getWatcher();
        watcher.setNoGravity(true);

        DisguiseAPI.disguiseToPlayers(armorStand, disguise, player);

        if (PetManager.hasPet(player)) {
            player.removePotionEffect(PotionEffectType.SPEED);
            PetManager.despawnPet(player);
        }

        GuardiansOfAdelia instance = GuardiansOfAdelia.getInstance();
        for (Player other : players.keySet()) {
            other.hidePlayer(instance, player);
            player.hidePlayer(instance, other);
        }

        players.put(player, cosmeticRoomData);
    }

    public static void leaveRoom(Player player) {
        if (!players.containsKey(player)) return;

        CosmeticRoomData cosmeticRoomData = players.remove(player);

        if (DisguiseAPI.isDisguised(player, armorStand)) {
            Disguise disguise = DisguiseAPI.getDisguise(player, armorStand);
            disguise.removeDisguise();
        }

        if (DisguiseAPI.isDisguised(player, armorStandTop)) {
            Disguise disguise = DisguiseAPI.getDisguise(player, armorStandTop);
            disguise.removeDisguise();
        }

        Location backLocation = cosmeticRoomData.getBackLocation();
        if (backLocation != null) {
            player.teleport(backLocation);
        }

        GuardiansOfAdelia instance = GuardiansOfAdelia.getInstance();
        for (Player other : players.keySet()) {
            other.showPlayer(instance, player);
            player.showPlayer(instance, other);
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                PetManager.respawnPet(player);
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), 40L);
    }

    public static boolean isPlayerInRoom(Player player) {
        return players.containsKey(player);
    }

    private static void rotate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (armorStand == null || !armorStand.isValid()) {
                    cancel();
                    return;
                }

                Location location = armorStand.getLocation();
                float yaw = location.getYaw();

                if (yaw >= 180) {
                    yaw = -180;
                }

                location.setYaw(yaw + 4);

                armorStand.eject();
                armorStand.teleport(location);
                armorStandTop.teleport(location);
                armorStand.addPassenger(armorStandTop);
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 40L, 2L);
    }

    public static Location getPlayerBackLocation(Player player) {
        if (players.containsKey(player)) {
            return players.get(player).getBackLocation();
        }

        return null;
    }

    public static void apply(Player player) {
        CosmeticRoomData cosmeticRoomData = players.get(player);

        if (cosmeticRoomData == null) {
            return;
        }

        HashMap<CosmeticSlot, CosmeticRecord> cosmetics = cosmeticRoomData.getCosmetics();

        GuardianData guardianData = GuardianDataManager.getGuardianData(player);
        if (guardianData == null) {
            return;
        }
        RPGCharacter activeCharacter = guardianData.getActiveCharacter();
        if (activeCharacter == null) {
            return;
        }
        RPGCharacterStats rpgCharacterStats = activeCharacter.getRpgCharacterStats();

        for (CosmeticSlot cosmeticSlot : CosmeticSlot.values()) {
            if (cosmeticSlot.isSkin()) {
                continue;
            }

            if (cosmetics.containsKey(cosmeticSlot)) {
                CosmeticRecord record = cosmetics.get(cosmeticSlot);

                rpgCharacterStats.setCosmetic(cosmeticSlot, record);
            } else {
                rpgCharacterStats.removeCosmetic(cosmeticSlot);
            }
        }

        for (CosmeticSlot cosmeticSlot : CosmeticSlot.values()) {
            if (!cosmeticSlot.isSkin()) {
                continue;
            }

            if (cosmetics.containsKey(cosmeticSlot)) {
                CosmeticRecord record = cosmetics.get(cosmeticSlot);

                applyToItem(player, record);
            }
        }
    }

    private static void applyToItem(Player player, CosmeticRecord record) {
        int cosmeticId = record.cosmeticId();
        Cosmetic cosmetic = CosmeticManager.get(cosmeticId);

        CosmeticType type = cosmetic.getType();

        EquipmentSlot equipmentSlot = type.getEquipmentSlot();

        ItemStack itemStack = player.getEquipment().getItem(equipmentSlot);

        if (!type.getMaterial().equals(itemStack.getType())) {
            return;
        }

        int customModelData = cosmetic.getCustomModelData();

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);
    }
}
