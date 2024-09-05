package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.target;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillDataManager;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.utilities.particle.ParticleShapes;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Applies child components to the closest all nearby entities around
 * each of the current targets.
 */
public class LookAtNearestTarget extends AreaTarget {

    private final boolean castAtFail;
    private final boolean drawLine;
    private final float drawLineGap;

    private final boolean selectTarget;

    public LookAtNearestTarget(ConfigurationSection configurationSection) {
        super(configurationSection);

        this.castAtFail = configurationSection.contains("castAtFail") && configurationSection.getBoolean("castAtFail");
        this.drawLine = configurationSection.contains("drawLine") && configurationSection.getBoolean("drawLine");

        if (drawLine) {
            this.drawLineGap = (float) configurationSection.getDouble("drawLineGap");
        } else {
            this.drawLineGap = 0;
        }

        this.selectTarget = configurationSection.contains("selectTarget") && configurationSection.getBoolean("selectTarget");
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter, int skillIndex) {
        if (targets.isEmpty()) return false;

        Vector offset = new Vector(0, 0.5, 0); // Default offset is 0.5 since it is most used value
        if (offset_xList != null) {
            float offset_x = offset_xList.get(skillLevel - 1);
            offset.setX(offset_x);
        }
        if (offset_xList != null) {
            float offset_y = offset_yList.get(skillLevel - 1);
            offset.setY(offset_y);
        }
        if (offset_xList != null) {
            float offset_z = offset_zList.get(skillLevel - 1);
            offset.setZ(offset_z);
        }

        float radius = radiusList.get(skillLevel - 1);
        if (multiplyWithValue != null) {
            int value = SkillDataManager.getValue(caster, multiplyWithValue);
            if (value > 0) {
                radius *= value;
            }
        }

        int amount = amountList.get(skillLevel - 1);

        boolean success = false; // If any cast was successful

        for (LivingEntity target : targets) {
            Location location = target.getLocation();
            location.add(offset);
            List<LivingEntity> nearbyTarget = TargetHelper.getNearbySphere(location, radius);

            if (arrangementSingle != null && showParticles) {
                ParticleShapes.drawCylinder(location, arrangementSingle, radius, amount, particleHeight, false, 0, 0, new Vector());
            }

            nearbyTarget = determineTargets(caster, nearbyTarget);

            if (nearbyTarget.isEmpty()) {
                if (castAtFail) {
                    boolean b = executeChildren(caster, skillLevel, targets, castCounter, skillIndex);
                    if (b) {
                        success = true;
                    }
                }
            } else {
                Location start = target.getLocation();
                List<LivingEntity> firstNElementsList = nearbyTarget;
                if (this.getMax() > nearbyTarget.size()) {
                    firstNElementsList = nearbyTarget.stream().limit(this.getMax()).collect(Collectors.toList());
                }
                // Draw line to each target
                for (LivingEntity singleTarget : firstNElementsList) {
                    float height = (float) (singleTarget.getHeight()) / 2f;
                    Location targetLocation = singleTarget.getLocation().add(0, height, 0);

                    if (drawLine) {
                        ParticleShapes.drawLineBetween(start.getWorld(), start.toVector(), arrangementSingle, targetLocation.toVector(), drawLineGap);
                    }
                }

                // Look at nearest target
                LivingEntity singleTarget = nearbyTarget.get(0);
                float height = (float) (singleTarget.getHeight()) / 2f;
                Location targetLocation = singleTarget.getLocation().add(0, height, 0);
                Vector vectorBetweenPoints = targetLocation.toVector().subtract(start.toVector());
                start.setDirection(vectorBetweenPoints);
                target.teleport(start);

                if (selectTarget) {
                    List<LivingEntity> targetsNew = new ArrayList<>();
                    if (super.isKeepCurrent()) {
                        if (super.isAddToBeginning()) {
                            firstNElementsList.addAll(targets);
                            targetsNew = firstNElementsList;
                        } else {
                            targetsNew.addAll(targets);
                            targetsNew.addAll(firstNElementsList);
                        }
                    } else {
                        targetsNew = firstNElementsList;
                    }

                    boolean b = executeChildren(caster, skillLevel, targetsNew, castCounter, skillIndex);
                    if (b) {
                        success = true;
                    }
                } else {
                    boolean b = executeChildren(caster, skillLevel, targets, castCounter, skillIndex);
                    if (b) {
                        success = true;
                    }
                }
            }
        }

        return success;
    }

    @Override
    public List<String> getSkillLoreAdditions(String lang, List<String> additions, int skillLevel) {
        if (!this.addLore) return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);

        if (skillLevel == 0) {
            additions.add(ChatPalette.YELLOW + "Look at nearest in radius: " + radiusList.get(skillLevel));
        } else if (skillLevel == radiusList.size()) {
            additions.add(ChatPalette.YELLOW + "Look at nearest in radius: " + radiusList.get(skillLevel - 1));
        } else {
            additions.add(ChatPalette.YELLOW + "Look at nearest in radius: " + radiusList.get(skillLevel - 1) + " -> " + radiusList.get(skillLevel));
        }

        return getSkillLoreAdditionsOfChildren(lang, additions, skillLevel);
    }
}
