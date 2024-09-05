package io.github.lix3nn53.guardiansofadelia.menu.main;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.cosmetic.CosmeticRoom;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.menu.GuiHelper;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.transportation.TeleportationUtils;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import io.github.lix3nn53.guardiansofadelia.utilities.hologram.Hologram;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class GuiPremium extends GuiGeneric {

    public GuiPremium(GuardianData guardianData) {
        super(54, CustomCharacterGui.MENU_54.toString() + ChatPalette.BLACK + Translation.t(guardianData, "menu.premium.name"), 0);

        ItemStack activeBoosts = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta itemMeta = activeBoosts.getItemMeta();
        itemMeta.setCustomModelData(28);
        itemMeta.setDisplayName(ChatPalette.GREEN + Translation.t(guardianData, "menu.boost.name"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "menu.boost.l1"));
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        activeBoosts.setItemMeta(itemMeta);

        ItemStack donation = new ItemStack(Material.WOODEN_PICKAXE);
        itemMeta.setCustomModelData(54);
        itemMeta.setDisplayName(ChatPalette.RED + Translation.t(guardianData, "menu.store.name"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "menu.store.l1"));
        lore.add(ChatPalette.GRAY + "");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "menu.store.l2"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "menu.store.l3"));
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "menu.store.l4"));
        lore.add(ChatPalette.GRAY + "");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "menu.store.l5"));
        itemMeta.setLore(lore);
        donation.setItemMeta(itemMeta);

        ItemStack rank = new ItemStack(Material.WOODEN_PICKAXE);
        itemMeta.setCustomModelData(51);
        itemMeta.setDisplayName(ChatPalette.GOLD + Translation.t(guardianData, "menu.rank.name"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "menu.rank.l1"));
        itemMeta.setLore(lore);
        rank.setItemMeta(itemMeta);

        ItemStack cosmeticColor = new ItemStack(Material.WOODEN_PICKAXE);
        itemMeta.setCustomModelData(52);
        itemMeta.setDisplayName(ChatPalette.PURPLE_LIGHT + Translation.t(guardianData, "menu.cosmetic.name"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "menu.cosmetic.l1"));
        itemMeta.setLore(lore);
        cosmeticColor.setItemMeta(itemMeta);

        GuiHelper.form54Big(this, new ItemStack[]{donation, rank, activeBoosts, cosmeticColor}, "Main Menu");
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GuardianData guardianData;
        RPGCharacter rpgCharacter;
        if (GuardianDataManager.hasGuardianData(player)) {
            guardianData = GuardianDataManager.getGuardianData(player);

            if (guardianData.hasActiveCharacter()) {
                rpgCharacter = guardianData.getActiveCharacter();
            } else {
                return;
            }
        } else {
            return;
        }


        int slot = event.getSlot();
        if (slot == 0) {
            GuiMain gui = new GuiMain(guardianData);
            gui.openInventory(player);
        } else if (GuiHelper.get54BigButtonIndexes(0).contains(slot)) {
            player.closeInventory();
            TextComponent message = new TextComponent("Open Webstore! ♥ (Click Me)");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.guardiansofadelia.com/store"));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatPalette.GOLD + "Click to open website")));
            message.setColor(ChatColor.GOLD);
            message.setBold(true);
            player.spigot().sendMessage(message);
        } else if (GuiHelper.get54BigButtonIndexes(1).contains(slot)) {
            // TODO RANK
        } else if (GuiHelper.get54BigButtonIndexes(2).contains(slot)) {
            GuiServerBoost gui = new GuiServerBoost();
            gui.openInventory(player);
        } else if (GuiHelper.get54BigButtonIndexes(3).contains(slot)) {
            if (guardianData.isFreeToAct()) {
                if (!player.getWorld().getName().equals("world")) {
                    player.sendMessage(ChatPalette.RED + "You can't use teleport gui from this location.");
                    return;
                }

                player.closeInventory();

                String destination = "Cosmetic Room";
                int stepCount = 5;
                guardianData.setTeleporting(true);
                final float startPosX = (float) player.getLocation().getX();
                final float startPosY = (float) player.getLocation().getY();
                final float startPosZ = (float) player.getLocation().getZ();

                ArmorStand hologramTop = new Hologram(player.getLocation().add(0.0, 2.6, 0.0),
                        ChatPalette.BLUE + "< " + ChatPalette.YELLOW + destination + ChatPalette.BLUE + " >").getArmorStand();
                ArmorStand hologramBottom = new Hologram(player.getLocation().add(0.0, 2.3, 0.0),
                        ChatPalette.BLUE_LIGHT + "Teleporting.. " + stepCount).getArmorStand();
                player.sendTitle(ChatPalette.BLUE + "Teleporting..", ChatPalette.BLUE_LIGHT.toString() + stepCount, 5, 20, 5);

                new BukkitRunnable() {

                    // We don't want the task to run indefinitely
                    int ticksRun;

                    @Override
                    public void run() {
                        ticksRun++;

                        boolean doesDivide = ticksRun % 4 == 0;
                        if (doesDivide) {
                            int currentStep = ticksRun / 4;

                            float differenceX = Math.abs(startPosX - (float) player.getLocation().getX());
                            float differenceY = Math.abs(startPosY - (float) player.getLocation().getY());
                            float differenceZ = Math.abs(startPosZ - (float) player.getLocation().getZ());

                            if (currentStep < stepCount) {
                                if (TeleportationUtils.isTeleportCanceled(differenceX, differenceY, differenceZ)) {
                                    TeleportationUtils.cancelTeleportation(this, guardianData, hologramTop, hologramBottom, player);
                                } else {
                                    TeleportationUtils.nextStep(player, hologramTop, hologramBottom, destination, stepCount - currentStep);
                                }
                            } else {
                                if (TeleportationUtils.isTeleportCanceled(differenceX, differenceY, differenceZ)) {
                                    TeleportationUtils.cancelTeleportation(this, guardianData, hologramTop, hologramBottom, player);
                                } else {
                                    Location backLocation = player.getLocation();
                                    TeleportationUtils.finishTeleportation(this, guardianData, hologramTop, hologramBottom,
                                            player, CosmeticRoom.getTpLocation(), destination, null, 0);
                                    CosmeticRoom.start(player, backLocation);
                                }
                            }
                        }
                    }
                }.runTaskTimer(GuardiansOfAdelia.getInstance(), 5L, 5L);
            }
        }
    }
}
