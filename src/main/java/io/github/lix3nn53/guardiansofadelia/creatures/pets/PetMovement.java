package io.github.lix3nn53.guardiansofadelia.creatures.pets;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.minigames.MiniGameManager;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class PetMovement {

    public static void onPetSpawn(Player player, String petCode, LivingEntity pet, int level) {
        PetData petData = PetDataManager.getPetData(petCode);
        int speed = petData.getSpeed();

        if (!MiniGameManager.isInMinigame(player)) {
            PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed, false, false);
            player.addPotionEffect(potionEffect);
        }

        new BukkitRunnable() {

            Vector previousDirection = new Vector();

            @Override
            public void run() {
                if (!pet.isValid()) {
                    cancel();
                    return;
                }

                Location target = player.getLocation(); // Target location to follow
                Location start = pet.getLocation(); // Current location

                // Skill section
                if (!PetManager.petSkillOnCooldown.contains(player)) {
                    Skill skill = petData.getSkill(level);
                    if (skill != null) {
                        ArrayList<LivingEntity> targets = new ArrayList<>();
                        targets.add(pet);
                        boolean cast = skill.cast(player, 1, targets, 1, 999);

                        if (cast) {
                            PetManager.petSkillOnCooldown.add(player);

                            int cooldown = skill.getCooldown(0);
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    PetManager.petSkillOnCooldown.remove(player);
                                }
                            }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), cooldown);

                            PetExperienceManager.giveExperienceToActivePet(player, 1);

                            return;
                        }
                    }
                }

                // We don't want pets get too close to players so lets add offset
                // Start - Calculate offset
                Vector dirOfTarget = target.getDirection();
                dirOfTarget.setY(0);
                Vector side = dirOfTarget.clone().crossProduct(new Vector(0, 1, 0));
                Vector upward = dirOfTarget.clone().crossProduct(side);
                target.subtract(upward.multiply(2.8));

                Vector vectorBetweenPoints = target.toVector().subtract(start.toVector());
                float distance = (float) vectorBetweenPoints.length();
                if (distance < 2.8f) return; // Pet is close enough to player head

                target.add(dirOfTarget.multiply(-0.8)).add(side.multiply(2.8));
                // End - Calculate offset

                vectorBetweenPoints = target.toVector().subtract(start.toVector());
                distance = (float) vectorBetweenPoints.length();

                if (distance < 0.8f) return; // Pet is close enough to target location
                if (distance > 42) {
                    pet.teleport(target);
                    return;
                }
                Vector direction = vectorBetweenPoints.normalize();

                Vector nextDirection = previousDirection.add(direction); // Add previousDirection to direction so pet makes a smooth turn
                nextDirection.normalize();

                float travel = distance / 20f; // Use distances to calculate how fast a pet will travel. Longer the distance, faster the pet.
                start.add(nextDirection.multiply(travel));

                start.setDirection(direction); // Make pet look at the same direction as player

                pet.teleport(start); // Finally, teleport the pet

                previousDirection = nextDirection; // Update previousDirection
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 0L, 1L);
    }
}
