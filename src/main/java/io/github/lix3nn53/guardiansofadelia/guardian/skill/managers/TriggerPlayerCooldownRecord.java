package io.github.lix3nn53.guardiansofadelia.guardian.skill.managers;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.TriggerComponent;
import org.bukkit.entity.Player;

public record TriggerPlayerCooldownRecord(Player player, int skillId,
                                          TriggerComponent trigger,
                                          int skillLevel) {


}
