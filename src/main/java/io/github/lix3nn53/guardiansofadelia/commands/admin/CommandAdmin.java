package io.github.lix3nn53.guardiansofadelia.commands.admin;

import io.github.lix3nn53.guardiansofadelia.chat.ChatManager;
import io.github.lix3nn53.guardiansofadelia.chat.StaffRank;
import io.github.lix3nn53.guardiansofadelia.creatures.mythicmobs.MMSpawnerManager;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetExperienceManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillBar;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillRPGClassData;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.towns.Town;
import io.github.lix3nn53.guardiansofadelia.towns.TownManager;
import io.github.lix3nn53.guardiansofadelia.utilities.config.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandAdmin implements CommandExecutor {

    public static boolean DEBUG_MODE = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equals("admin")) {
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage(ChatPalette.PURPLE + "---- ADMIN ----");
                player.sendMessage(ChatPalette.PURPLE + "/admin debug");
                player.sendMessage(ChatPalette.PURPLE + "/admin reload <skills|pets|interact|rp>");
                player.sendMessage(ChatPalette.PURPLE + "/admin setstaff <player> <tag>");
                player.sendMessage(ChatPalette.PURPLE + "/admin build");
                player.sendMessage(ChatPalette.PURPLE_LIGHT + "---- UTILS ----");
                player.sendMessage(ChatPalette.PURPLE_LIGHT + "/admin fly");
                player.sendMessage(ChatPalette.PURPLE_LIGHT + "/admin speed <num>");
                player.sendMessage(ChatPalette.PURPLE_LIGHT + "/admin tp town <num>");
                player.sendMessage(ChatPalette.BLUE_DARK + "---- RPG ----");
                player.sendMessage(ChatPalette.BLUE_DARK + "/admin exp <player> <amount>");
                player.sendMessage(ChatPalette.BLUE_DARK + "/admin pexp <player> <amount> - pet exp");
            } else if (args[0].equals("debug")) {
                DEBUG_MODE = !DEBUG_MODE;
                player.sendMessage("DEBUG MODE: " + DEBUG_MODE);
            } else if (args[0].equals("speed")) {
                int val = Integer.parseInt(args[1]);
                if (val > 10 || val < -1) {
                    player.sendMessage(ChatPalette.RED + "Speed must be between 1-10");
                    return false;
                }
                float valf = val / 10f;
                player.setFlySpeed(valf);
            } else if (args[0].equals("exp")) {
                if (args.length == 3) {
                    int expToGive = Integer.parseInt(args[2]);
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        if (GuardianDataManager.hasGuardianData(target)) {
                            GuardianData guardianData = GuardianDataManager.getGuardianData(target);
                            if (guardianData.hasActiveCharacter()) {
                                RPGCharacter activeCharacter = guardianData.getActiveCharacter();

                                RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();

                                activeCharacter.getRpgCharacterStats().giveExp(expToGive, rpgClassStats);
                            }
                        }
                    }
                }
            } else if (args[0].equals("pexp")) {
                if (args.length == 3) {
                    int expToGive = Integer.parseInt(args[2]);
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        PetExperienceManager.giveExperienceToActivePet(target, expToGive);
                    }
                }
            } else if (args[0].equals("setstaff")) {
                if (args.length == 3) {
                    try {
                        StaffRank staffRank = StaffRank.valueOf(args[2].toUpperCase());
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            if (GuardianDataManager.hasGuardianData(target)) {
                                GuardianData guardianData = GuardianDataManager.getGuardianData(target);
                                guardianData.setStaffRank(staffRank);
                                player.sendMessage(ChatPalette.GREEN + "Set staff rank of " + target.getName() + " to " + staffRank);
                                target.sendMessage(ChatPalette.GREEN + "Your staff rank has been set to " + staffRank);
                                ChatManager.updatePlayerName(target);
                            }
                        }
                    } catch (IllegalArgumentException illegalArgumentException) {
                        player.sendMessage(ChatPalette.RED + "Unknown staff-rank");
                    }
                }
            } else if (args[0].equals("tp")) {
                if (args.length == 3) {
                    if (args[1].equals("town")) {
                        int no = Integer.parseInt(args[2]);
                        if (no < 6 && no > 0) {
                            Town town = TownManager.getTown(no);
                            player.teleport(town.getLocation());
                        }
                    }
                }
            } else if (args[0].equals("fly")) {
                boolean allowFlight = player.getAllowFlight();
                player.setFlying(!allowFlight);
            } else if (args[0].equals("reload")) {
                if (args.length == 1) {
                    player.sendMessage("/admin reload skills");
                    player.sendMessage("/admin reload pets");
                    player.sendMessage("/admin reload interact");
                    player.sendMessage("/admin reload rp");
                } else if (args[1].equals("skills")) {
                    ClassConfigurations.loadConfigs();
                    player.sendMessage(ChatPalette.GREEN_DARK + "Reloaded class configs!");
                    Set<Player> onlinePlayers = GuardianDataManager.getOnlinePlayers();
                    for (Player onlinePlayer : onlinePlayers) {
                        GuardianData guardianData = GuardianDataManager.getGuardianData(onlinePlayer);
                        RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                        if (activeCharacter == null) continue;

                        String rpgClassStr = activeCharacter.getRpgClassStr();
                        RPGClass rpgClass = RPGClassManager.getClass(rpgClassStr);

                        SkillBar skillBar = activeCharacter.getSkillBar();
                        RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();
                        SkillRPGClassData skillRPGClassData = rpgClassStats.getSkillRPGClassData();
                        skillBar.reloadSkillSet(rpgClass.getSkillTree(), skillRPGClassData, guardianData.getLanguage());
                    }

                    ItemSkillConfigurations.createConfigs();
                    ItemSkillConfigurations.loadConfigs();

                    player.sendMessage(ChatPalette.GREEN_DARK + "Reloaded player skills!");
                } else if (args[1].equals("pets")) {
                    PetConfigurations.createConfigs();
                    PetConfigurations.loadConfigs();

                    player.sendMessage(ChatPalette.GREEN_DARK + "Reloaded pets!");
                } else if (args[1].equals("interact")) {
                    MMSpawnerManager.clear();
                    GenericInteractableConfiguration.createConfigs();
                    GenericInteractableConfiguration.loadConfigs();
                } else if (args[1].equals("rp")) {
                    ConfigManager.loadResourcePackConfig();
                } else {
                    player.sendMessage("/admin reload skills");
                    player.sendMessage("/admin reload pets");
                    player.sendMessage("/admin reload interact");
                    player.sendMessage("/admin reload rp");
                }
            }

            // If the player (or console) uses our command correct, we can return true
            return true;
        }
        return false;
    }
}
