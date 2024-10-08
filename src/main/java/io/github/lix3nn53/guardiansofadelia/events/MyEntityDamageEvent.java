package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.bossbar.HealthBarManager;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetManager;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.immunity.ImmunityListener;
import io.github.lix3nn53.guardiansofadelia.party.Party;
import io.github.lix3nn53.guardiansofadelia.party.PartyManager;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class MyEntityDamageEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;

        EntityDamageEvent.DamageCause cause = event.getCause();
        if (entity instanceof Player) {
            Player player = (Player) entity;

            float absorptionHearts = (float) player.getAbsorptionAmount();

            if (absorptionHearts > 0) {
                event.setCancelled(true);
                absorptionHearts -= 2;
                if (absorptionHearts < 0) {
                    absorptionHearts = 0;
                }
                player.setAbsorptionAmount(absorptionHearts);
                return;
            }
        }

        if (cause.equals(EntityDamageEvent.DamageCause.VOID)) {
            event.setDamage(10000D);
        } else if (cause.equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
            entity.teleport(entity.getLocation().clone().add(0, 5, 0));
            event.setCancelled(true);
            return;
        } else if (cause.equals(EntityDamageEvent.DamageCause.STARVATION)) {
            event.setCancelled(true);
            return;
        } else if (cause.equals(EntityDamageEvent.DamageCause.FALL)) {
            if (entity.getType().equals(EntityType.HORSE)) {
                event.setCancelled(true);
                return;
            }
        }

        if (ImmunityListener.isImmune((LivingEntity) entity, cause)) {
            event.setCancelled(true);
            return;
        }

        float customNaturalDamage = getCustomNaturalDamage(cause, (LivingEntity) entity);
        if (customNaturalDamage > 0) {
            event.setDamage(customNaturalDamage);
        }

        EntityType entityType = event.getEntityType();

        if (entityType.equals(EntityType.PLAYER)) {
            Player player = (Player) entity;

            float finalDamage = (float) event.getFinalDamage();
            if (PartyManager.inParty(player)) {
                Party party = PartyManager.getParty(player);
                party.getBoard().updateHP(player.getName(), (int) (player.getHealth() - finalDamage + 0.5));
            }
        } else {
            LivingEntity livingEntity = (LivingEntity) entity;
            PetManager.onTakeDamage(livingEntity, (float) livingEntity.getHealth(), (float) event.getFinalDamage());
        }

        LivingEntity livingEntity = (LivingEntity) entity;
        // These damage causes are handled in their own event
        if (!(cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || cause.equals(EntityDamageEvent.DamageCause.PROJECTILE))) {
            HealthBarManager.onLivingTargetHealthChange(livingEntity, (int) (event.getFinalDamage() + 0.5),
                    ChatPalette.RED, "");
        }
    }

    private float getCustomNaturalDamage(EntityDamageEvent.DamageCause cause, LivingEntity entity) {
        float maxHealth = (float) entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (cause.equals(EntityDamageEvent.DamageCause.FALL) && entity instanceof Player) {
            float fallDistance = entity.getFallDistance();

            return (fallDistance - 3) * (maxHealth / 20); // Vanilla fall damage is 1 damage each block after the third
        } else if (cause.equals(EntityDamageEvent.DamageCause.POISON)) {
            return maxHealth / 40;
        } else if (cause.equals(EntityDamageEvent.DamageCause.WITHER)) {
            return maxHealth / 40;
        } else if (cause.equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
            return maxHealth / 20;
        } else if (cause.equals(EntityDamageEvent.DamageCause.FIRE)) {
            return maxHealth / 20;
        } else if (cause.equals(EntityDamageEvent.DamageCause.DROWNING)) {
            return maxHealth / 20;
        } else if (cause.equals(EntityDamageEvent.DamageCause.HOT_FLOOR)) {
            return maxHealth / 20;
        } else if (cause.equals(EntityDamageEvent.DamageCause.LAVA)) {
            return maxHealth / 20;
        } else if (cause.equals(EntityDamageEvent.DamageCause.CONTACT)) {
            return maxHealth / 20;
        }
        return 0;
    }

}
