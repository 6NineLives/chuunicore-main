package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.statuseffect;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

class StatusEffectDuration {

    private final StatusEffectType effectType;
    private final int totalDuration;

    // State
    private int durationLeft;
    private BukkitTask bukkitTask;

    public StatusEffectDuration(StatusEffectType effectType, int startDuration) {
        this.totalDuration = startDuration;
        this.effectType = effectType;
    }

    public void startCooldown(LivingEntity target) {
        durationLeft = totalDuration;

        bukkitTask = new BukkitRunnable() {

            @Override
            public void run() {
                if (durationLeft > 1) {
                    durationLeft -= 1;
                    return;
                }
                cancel();
                StatusEffectManager.removeStatus(target, effectType);
            }
        }.runTaskTimerAsynchronously(GuardiansOfAdelia.getInstance(), 0L, 1L);
    }

    public int getDurationLeft() {
        return durationLeft;
    }

    public void cancel() {
        this.bukkitTask.cancel();
    }
}
