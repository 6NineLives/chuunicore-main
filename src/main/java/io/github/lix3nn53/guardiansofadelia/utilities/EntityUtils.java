package io.github.lix3nn53.guardiansofadelia.utilities;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.creatures.custom.TemporaryEntity;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetManager;
import io.github.lix3nn53.guardiansofadelia.party.Party;
import io.github.lix3nn53.guardiansofadelia.party.PartyManager;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class EntityUtils {

    public static final String MM_ALLY_FACTION = "ally";

    /*
    public static LivingEntity create(Location loc, String name, float hp, EntityType type) {
        LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
        entity.setCustomName(name);
        entity.setCustomNameVisible(true);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
        entity.setHealth(hp);
        entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.7D);
        entity.setCanPickupItems(false);

        return entity;
    }

     */

    public static void delayedRemove(Entity entity, long delayTicks) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isValid()) {
                    entity.remove();
                }
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), delayTicks);
    }

    public static void delayedRemove(List<Entity> entities, long delayTicks) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : entities) {
                    if (entity.isValid()) {
                        entity.remove();
                    }
                }
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), delayTicks);
    }

    /**
     * Checks whether or not something can be attacked
     *
     * @param attacker the attacking entity
     * @param target   the target entity
     * @return true if can be attacked, false otherwise
     */

    public static boolean canAttack(LivingEntity attacker, LivingEntity target) {
        if (target instanceof TemporaryEntity) {
            return false;
        }

        ActiveMob mythicTarget = null;

        try {
            BukkitAPIHelper apiHelper = MythicBukkit.inst().getAPIHelper();
            mythicTarget = apiHelper.getMythicMobInstance(target);
        } catch (Exception e) {
            GuardiansOfAdelia.getInstance().getLogger().warning("canAttack Could not get mythic mob instance for target " + target.getName());
            // e.printStackTrace();
            return true;
        }


        if (attacker instanceof Player) { //attacker is a player
            boolean pvp = target.getWorld().getPVP();

            if (target instanceof Player) { //player attack player
                if (attacker == target) return false;
                if (pvp) {
                    Player attackerPlayer = (Player) attacker;

                    if (PartyManager.inParty(attackerPlayer)) {
                        Party party = PartyManager.getParty(attackerPlayer);
                        List<Player> members = party.getMembers();
                        return !members.contains(target);
                    }

                    return true;
                }

                return false; //pvp is off
            } else if (PetManager.isCompanion(target)) { //player attack pet
                if (pvp) {
                    Player owner = PetManager.getOwner(target);

                    if (attacker == owner) return false;

                    return canAttack(attacker, owner);
                }

                return false; //pvp is off
            }

            // player attack monster
            if (mythicTarget != null) {
                String faction = mythicTarget.getFaction();
                GuardiansOfAdelia.getInstance().getLogger().info("player attack monster faction: " + faction);
                // player attack ally monster
                return faction == null || !faction.equals(MM_ALLY_FACTION);
            }

            return true;
        } else if (PetManager.isCompanion(attacker)) {  //attacker is a pet
            Player attackerOwner = PetManager.getOwner(attacker);
            if (PetManager.isCompanion(target)) { //target is also a pet
                Player targetOwner = PetManager.getOwner(target);
                return canAttack(attackerOwner, targetOwner);
            } else if (target instanceof Player) { //pet attack player
                return canAttack(attackerOwner, target);
            }

            // pet attack monster
            if (mythicTarget != null) {
                String faction = mythicTarget.getFaction();
                GuardiansOfAdelia.getInstance().getLogger().info("pet attack monster faction: " + faction);
                // pet attack ally monster
                return faction == null || !faction.equals(MM_ALLY_FACTION);
            }

            return true;
        }

        //attacker is monster
        if (mythicTarget != null) {
            String faction = mythicTarget.getFaction();
            GuardiansOfAdelia.getInstance().getLogger().info("enemy attack monster faction: " + faction);
            if (faction != null && faction.equals(MM_ALLY_FACTION)) { // enemy attack ally monster
                return true;
            }
        }

        return target instanceof Player || PetManager.isCompanion(target); // monster attack player or pet
    }
}
