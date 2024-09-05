package io.github.lix3nn53.guardiansofadelia.menu.main.guild;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guild.GuildManager;
import io.github.lix3nn53.guardiansofadelia.menu.GuiHelper;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiGuild;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiGuildEmpty;
import io.github.lix3nn53.guardiansofadelia.minigames.MiniGameManager;
import io.github.lix3nn53.guardiansofadelia.minigames.guildwar.GuildWar;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiGuildWar extends GuiGeneric {

    public GuiGuildWar() {
        super(27, ChatPalette.GRAY_DARK + "Join Guild War", 0);

        List<ItemStack> itemStacks = new ArrayList<>();
        for (GuildWar guildWar : MiniGameManager.guildWarList) {
            ItemStack room = new ItemStack(Material.LIME_WOOL);
            ItemMeta itemMeta = room.getItemMeta();
            itemMeta.setDisplayName(ChatPalette.GREEN_DARK + guildWar.getMinigameName() + " (" + guildWar.getPlayersInGameSize() + "/" + guildWar.getMaxPlayerSize() + ")");

            ArrayList<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatPalette.GREEN_DARK + "Map: " + ChatPalette.WHITE + guildWar.getMapName());
            lore.add(ChatPalette.YELLOW + "Level req: " + ChatPalette.WHITE + guildWar.getLevelReq());
            lore.add(ChatPalette.GOLD + "Team size: " + ChatPalette.WHITE + guildWar.getTeamSize());
            lore.add(ChatPalette.PURPLE_LIGHT + "Game time: " + ChatPalette.WHITE + guildWar.getTimeLimitInMinutes() + " minute(s)");
            lore.add("");
            lore.add(ChatPalette.GRAY + "Click to join this room!");
            itemMeta.setLore(lore);

            room.setItemMeta(itemMeta);
            if (guildWar.isInGame()) {
                room.setType(Material.RED_WOOL);
            }
            room.setItemMeta(itemMeta);
            itemStacks.add(room);
        }

        GuiHelper.form27Small(this, itemStacks.toArray(new ItemStack[0]), "Guild");
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GuardianData guardianData;
        if (GuardianDataManager.hasGuardianData(player)) {
            guardianData = GuardianDataManager.getGuardianData(player);
        } else {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory.getType().equals(InventoryType.CHEST)) {
            int slot = event.getSlot();

            if (slot == 0) {
                GuiGeneric gui;
                if (GuildManager.inGuild(player)) {
                    gui = new GuiGuild(player, guardianData);
                } else {
                    gui = new GuiGuildEmpty(player, guardianData);
                }
                gui.openInventory(player);
            } else {
                int instanceNo = -1;
                if (GuiHelper.get54SmallButtonIndex(0) == slot) {
                    instanceNo = 0;
                } else if (GuiHelper.get54SmallButtonIndex(1) == slot) {
                    instanceNo = 1;
                } else if (GuiHelper.get54SmallButtonIndex(2) == slot) {
                    instanceNo = 2;
                } else if (GuiHelper.get54SmallButtonIndex(3) == slot) {
                    instanceNo = 3;
                } else if (GuiHelper.get54SmallButtonIndex(4) == slot) {
                    instanceNo = 4;
                } else if (GuiHelper.get54SmallButtonIndex(5) == slot) {
                    instanceNo = 5;
                } else if (GuiHelper.get54SmallButtonIndex(6) == slot) {
                    instanceNo = 6;
                }

                boolean joined = MiniGameManager.getGuildWar(instanceNo).joinQueue(player);
                if (joined) {
                    player.closeInventory();
                }
            }
        }
    }
}
