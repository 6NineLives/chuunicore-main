package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.bungeelistener.BoostPremiumManager;
import io.github.lix3nn53.guardiansofadelia.bungeelistener.products.BoostPremium;
import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.items.DungeonPrizeChest;
import io.github.lix3nn53.guardiansofadelia.items.PrizeChestType;
import io.github.lix3nn53.guardiansofadelia.items.list.armors.ArmorSlot;
import io.github.lix3nn53.guardiansofadelia.items.stats.StatUtils;
import io.github.lix3nn53.guardiansofadelia.jobs.crafting.CraftingType;
import io.github.lix3nn53.guardiansofadelia.menu.crafting.GuiCraftingLevelSelection;
import io.github.lix3nn53.guardiansofadelia.minigames.MiniGameManager;
import io.github.lix3nn53.guardiansofadelia.minigames.dungeon.DungeonTheme;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.transportation.TeleportScroll;
import io.github.lix3nn53.guardiansofadelia.transportation.TeleportationUtils;
import io.github.lix3nn53.guardiansofadelia.utilities.PersistentDataContainerUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class MyPlayerInteractEvent implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(PlayerInteractEvent event) {
        EquipmentSlot hand = event.getHand();
        if (hand != null && !hand.equals(EquipmentSlot.HAND)) {
            return;
        }

        Action action = event.getAction();
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();

            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            Material itemInMainHandType = itemInMainHand.getType();

            if (itemInMainHandType.equals(Material.GLASS_BOTTLE)) {
                event.setCancelled(true);
            }

            ArmorSlot armorSlot = ArmorSlot.getArmorSlot(itemInMainHand);

            if (armorSlot != null) {
                if (GuardianDataManager.hasGuardianData(player)) {
                    GuardianData guardianData = GuardianDataManager.getGuardianData(player);

                    if (guardianData.hasActiveCharacter()) {
                        RPGCharacter activeCharacter = guardianData.getActiveCharacter();

                        if (StatUtils.doesCharacterMeetRequirements(itemInMainHand, player, activeCharacter.getRpgClassStr())) {

                            RPGClassStats rpgClassStats = activeCharacter.getRPGClassStats();

                            activeCharacter.getRpgCharacterStats().onArmorEquip(itemInMainHand, rpgClassStats, true);

                            if (itemInMainHandType.equals(CosmeticManager.COSMETIC_MATERIAL)) {
                                EntityEquipment equipment = player.getEquipment();
                                equipment.setHelmet(itemInMainHand);
                                equipment.setItemInMainHand(null);
                            }
                        } else {
                            event.setCancelled(true);
                        }
                    }
                }
            } else {
                Block clickedBlock = event.getClickedBlock();

                Material clickedBlockType = clickedBlock != null ? clickedBlock.getType() : Material.AIR;

                if (clickedBlockType.isInteractable() && !MyBlockEvents.canBuild(player)) {
                    event.setCancelled(true);
                }

                if (itemInMainHandType.equals(Material.PAPER)) {
                    if (PersistentDataContainerUtil.hasString(itemInMainHand, "teleportScroll")) {
                        if (!player.getLocation().getWorld().getName().equals("world")) {
                            player.sendMessage(ChatPalette.RED + "You can't use teleport gui from this location.");
                            return;
                        }

                        String teleportScrollStr = PersistentDataContainerUtil.getString(itemInMainHand, "teleportScroll");

                        TeleportScroll teleportScroll = new TeleportScroll(teleportScrollStr);
                        Location location = teleportScroll.getLocation();
                        String name = teleportScroll.getName();
                        TeleportationUtils.teleport(player, location, name, 5, itemInMainHand, 0);
                    }
                } else if (itemInMainHandType.equals(Material.IRON_PICKAXE)) {
                    if (PersistentDataContainerUtil.hasString(itemInMainHand, "prizeDungeon")) { //dungeon chests
                        String dungeonThemeString = PersistentDataContainerUtil.getString(itemInMainHand, "prizeDungeon");
                        String typeStr = PersistentDataContainerUtil.getString(itemInMainHand, "prizeType");
                        PrizeChestType type = PrizeChestType.valueOf(typeStr);

                        HashMap<String, DungeonTheme> dungeonThemes = MiniGameManager.getDungeonThemes();
                        DungeonTheme dungeonTheme = dungeonThemes.get(dungeonThemeString);

                        List<ItemStack> itemStacks = dungeonTheme.generateChestItems(type);

                        DungeonPrizeChest prizeChest = new DungeonPrizeChest(dungeonTheme, type);
                        prizeChest.play(player, itemStacks);
                        player.getInventory().setItemInMainHand(null);
                    }
                } else if (itemInMainHandType.equals(CosmeticManager.PREMIUM_CONSUMABLE_MATERIAL)) {
                    if (PersistentDataContainerUtil.hasString(itemInMainHand, "boostCode")) {
                        String boostCode = PersistentDataContainerUtil.getString(itemInMainHand, "boostCode");

                        BoostPremium boostPremium = BoostPremium.valueOf(boostCode);

                        BoostPremiumManager.tellBungeeToActivateBoost(player, boostPremium);
                        int amount = itemInMainHand.getAmount();
                        itemInMainHand.setAmount(amount - 1);
                    }

                } else {
                    if (clickedBlockType.equals(Material.CAMPFIRE)) {
                        if (GuardianDataManager.hasGuardianData(player)) {
                            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                            if (guardianData.hasActiveCharacter()) {
                                GuiCraftingLevelSelection gui = new GuiCraftingLevelSelection(CraftingType.FOOD);
                                gui.openInventory(player);
                            }
                        }
                    } else if (clickedBlockType.equals(Material.BREWING_STAND)) {
                        if (GuardianDataManager.hasGuardianData(player)) {
                            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                            if (guardianData.hasActiveCharacter()) {
                                GuiCraftingLevelSelection gui = new GuiCraftingLevelSelection(CraftingType.POTION);
                                gui.openInventory(player);
                            }
                        }
                    } else if (clickedBlockType.equals(Material.GRINDSTONE)) {
                        if (GuardianDataManager.hasGuardianData(player)) {
                            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                            if (guardianData.hasActiveCharacter()) {
                                GuiCraftingLevelSelection gui = new GuiCraftingLevelSelection(CraftingType.WEAPON_MELEE);
                                gui.openInventory(player);
                            }
                        }
                    } else if (clickedBlockType.equals(Material.FLETCHING_TABLE)) {
                        if (GuardianDataManager.hasGuardianData(player)) {
                            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                            if (guardianData.hasActiveCharacter()) {
                                GuiCraftingLevelSelection gui = new GuiCraftingLevelSelection(CraftingType.WEAPON_RANGED);
                                gui.openInventory(player);
                            }
                        }
                    } else if (clickedBlockType.equals(Material.ANVIL)) {
                        if (GuardianDataManager.hasGuardianData(player)) {
                            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                            if (guardianData.hasActiveCharacter()) {
                                GuiCraftingLevelSelection gui = new GuiCraftingLevelSelection(CraftingType.ARMOR_HEAVY);
                                gui.openInventory(player);
                            }
                        }
                    } else if (clickedBlockType.equals(Material.LOOM)) {
                        if (GuardianDataManager.hasGuardianData(player)) {
                            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                            if (guardianData.hasActiveCharacter()) {
                                GuiCraftingLevelSelection gui = new GuiCraftingLevelSelection(CraftingType.ARMOR_LIGHT);
                                gui.openInventory(player);
                            }
                        }
                    } else if (clickedBlockType.equals(Material.SMITHING_TABLE)) {
                        if (GuardianDataManager.hasGuardianData(player)) {
                            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                            if (guardianData.hasActiveCharacter()) {
                                GuiCraftingLevelSelection gui = new GuiCraftingLevelSelection(CraftingType.JEWEL);
                                gui.openInventory(player);
                            }
                        }
                    } else if (clickedBlockType.equals(Material.ENCHANTING_TABLE)) {
                        if (GuardianDataManager.hasGuardianData(player)) {
                            GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                            if (guardianData.hasActiveCharacter()) {
                                GuiCraftingLevelSelection gui = new GuiCraftingLevelSelection(CraftingType.ENCHANT_STONE);
                                gui.openInventory(player);
                            }
                        }
                    }

                    /*else if (clickedBlockType.equals(Material.CHEST)) {
                        //LOOT CHEST
                        Location location = clickedBlock.getLocation();
                        LootChest lootChest = LootChestManager.isLootChest(location);
                        if (lootChest != null) {
                            lootChest.openLootInventory(player);
                        }
                    }*/
                }
            }
        }
    }
}
