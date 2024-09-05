package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.ActionBarInfo;
import io.github.lix3nn53.guardiansofadelia.guardian.character.ActionBarInfoType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.MechanicComponent;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.managers.ActionBarInfoManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class RegisterActionBarInfoMechanic extends MechanicComponent {

    private final ActionBarInfo actionBarInfo;

    public RegisterActionBarInfoMechanic(ActionBarInfo actionBarInfo, int skillId) {
        super(true);

        this.actionBarInfo = actionBarInfo;
    }

    public RegisterActionBarInfoMechanic(ConfigurationSection configurationSection, int skillId) {
        super(!configurationSection.contains("addLore") || configurationSection.getBoolean("addLore"));

        if (!configurationSection.contains("actionBarInfoType")) {
            configLoadError("actionBarInfoType");
        }

        if (!configurationSection.contains("icon")) {
            configLoadError("icon");
        }

        ActionBarInfoType actionBarInfoType = ActionBarInfoType.valueOf(configurationSection.getString("actionBarInfoType"));
        String icon = configurationSection.getString("icon");
        String key = configurationSection.getString("key");
        this.actionBarInfo = new ActionBarInfo(actionBarInfoType, icon, key);
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillId) {
        if (targets.isEmpty()) return false;

        for (LivingEntity target : targets) {
            if (target instanceof Player player) {
                boolean b = GuardianDataManager.hasGuardianData(player);
                if (!b) continue;

                ActionBarInfoManager.setActionBarInfo(player, this.actionBarInfo, castCounter);
            }
        }

        return true;
    }

    @Override
    public List<String> getSkillLoreAdditions(String lang, List<String> additions, int skillLevel) {

        return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);
    }
}
