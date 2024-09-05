package io.github.lix3nn53.guardiansofadelia.items.RpgGears;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.items.stats.StatUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class manages the skills that can be used by the weapon left click.
 */
public class WeaponGearTypeSkillManager {

    private static final HashMap<WeaponGearType, Skill> skillMap = new HashMap<>();
    private static final List<Player> playersOnCooldown = new ArrayList<>();

    public static void register(WeaponGearType weaponGearType, Skill skill) {
        skillMap.put(weaponGearType, skill);
    }

    public static void cast(Player player, WeaponGearType weaponGearType, ItemStack itemStack) {
        Skill skill = skillMap.get(weaponGearType);
        if (skill == null) {
            return;
        }

        if (playersOnCooldown.contains(player)) {
            return; // player is on cooldown
        }

        String rpgClassStr = GuardianDataManager.getGuardianData(player).getActiveCharacter().getRpgClassStr();
        if (!StatUtils.doesCharacterMeetRequirements(itemStack, player, rpgClassStr)) {
            return;
        }

        boolean cast = skill.cast(player, 1, new ArrayList<>(), -1, -1);
        if (cast) {
            playersOnCooldown.add(player);

            int cooldown = skill.getCooldown(1);

            player.setCooldown(weaponGearType.getMaterial(), cooldown);

            new BukkitRunnable() {

                @Override
                public void run() {
                    playersOnCooldown.remove(player);
                }
            }.runTaskLater(GuardiansOfAdelia.getInstance(), cooldown);
        }
    }
}
