package io.github.lix3nn53.guardiansofadelia.guardian.character;

import io.github.lix3nn53.guardiansofadelia.guardian.attribute.Attribute;
import io.github.lix3nn53.guardiansofadelia.guardian.attribute.AttributeType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillBar;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillRPGClassData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.tree.SkillTree;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RPGClassStats {
    private final SkillRPGClassData skillRPGClassData;
    private final HashMap<AttributeType, Integer> attributeInvested;

    public RPGClassStats(SkillRPGClassData skillRPGClassData, HashMap<AttributeType, Integer> attributeInvested) {
        this.skillRPGClassData = skillRPGClassData;
        this.attributeInvested = attributeInvested;
    }

    public RPGClassStats() {
        this.skillRPGClassData = new SkillRPGClassData();
        this.attributeInvested = new HashMap<>();
        for (AttributeType attributeType : AttributeType.values()) {
            attributeInvested.put(attributeType, 0);
        }
    }

    public SkillRPGClassData getSkillRPGClassData() {
        return skillRPGClassData;
    }

    public int getInvested(AttributeType attributeType) {
        if (attributeInvested.containsKey(attributeType)) {
            return attributeInvested.get(attributeType);
        }

        return 0;
    }

    public void setInvested(Attribute attribute, int invested, RPGCharacterStats rpgCharacterStats, boolean fixDisplay) {
        AttributeType attributeType = attribute.getAttributeType();
        attributeInvested.put(attributeType, invested);
        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            attribute.onValueChange(rpgCharacterStats, this);
        }
    }

    public void investPoint(Attribute attribute, int amount, RPGCharacterStats rpgCharacterStats, boolean fixDisplay) {
        int invested = 0;
        AttributeType attributeType = attribute.getAttributeType();
        if (attributeInvested.containsKey(attributeType)) {
            invested = attributeInvested.get(attributeType);
        }
        invested += amount;
        attributeInvested.put(attributeType, invested);
        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            attribute.onValueChange(rpgCharacterStats, this);
        }
    }

    public void downgradePoint(Attribute attribute, int amount, RPGCharacterStats rpgCharacterStats, boolean fixDisplay) {
        int invested = 0;
        AttributeType attributeType = attribute.getAttributeType();
        if (attributeInvested.containsKey(attributeType)) {
            invested = attributeInvested.get(attributeType);
        }
        invested -= amount;
        attributeInvested.put(attributeType, invested);
        if (fixDisplay && attributeType.getFixDisplayOnChange()) {
            attribute.onValueChange(rpgCharacterStats, this);
        }
    }

    public void resetAttributes(RPGCharacterStats rpgCharacterStats) {
        attributeInvested.clear();
        for (AttributeType attributeType : AttributeType.values()) {
            attributeInvested.put(attributeType, 0);
        }

        rpgCharacterStats.onMaxHealthChange(this);
        rpgCharacterStats.onCurrentManaChange(this);
    }

    public void resetAll(RPGCharacterStats rpgCharacterStats, Player player, SkillTree skillTree, SkillBar skillBar, String lang) {
        resetAttributes(rpgCharacterStats);

        skillRPGClassData.resetSkillPoints(player, skillTree, skillBar, lang);
    }

    public int getInvestedAttributePoints() {
        int total = 0;
        for (int invested : attributeInvested.values()) {
            total += invested;
        }

        return total;
    }

    public int getAttributePointsLeftToSpend(RPGCharacterStats rpgCharacterStats) {
        int totalExp = rpgCharacterStats.getTotalExp();
        int level = RPGCharacterExperienceManager.getLevel(totalExp);

        int inventedPointsOnAttributes = getInvestedAttributePoints();

        int pointsPerLevel = 1;

        return (level * pointsPerLevel) - inventedPointsOnAttributes;
    }
}
