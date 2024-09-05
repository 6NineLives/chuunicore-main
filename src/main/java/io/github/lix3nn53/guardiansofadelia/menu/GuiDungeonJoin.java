package io.github.lix3nn53.guardiansofadelia.menu;

import io.github.lix3nn53.guardiansofadelia.minigames.MiniGameManager;
import io.github.lix3nn53.guardiansofadelia.minigames.dungeon.DungeonInstance;
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
import java.util.HashMap;
import java.util.List;

public class GuiDungeonJoin extends GuiGeneric {

    private final HashMap<Integer, Integer> slotToInstance = new HashMap<>();
    String code;

    public GuiDungeonJoin(String name, String code, List<ItemStack> instanceItems) {
        super(27, "Join dungeon: " + name + " #" + code, 0);
        this.code = code;

        int slotNo = 9;
        for (int i = 0; i < instanceItems.size(); i++) {
            int instanceNo = i + 1;
            DungeonInstance dungeonInstance = MiniGameManager.getDungeonInstance(code, instanceNo);
            if (dungeonInstance == null) {
                break;
            }
            ItemStack itemStack = instanceItems.get(i);
            this.setItem(slotNo, itemStack);
            slotToInstance.put(slotNo, instanceNo);

            slotNo = slotNo + 2;
        }

        ItemStack prize = new ItemStack(Material.YELLOW_WOOL);
        ItemMeta itemMeta = prize.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.GOLD + "Guide: Prize Chests");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "4 prize chests will spawn if you complete");
        lore.add(ChatPalette.GRAY + "the dungeon successfully.");
        lore.add("");
        lore.add(ChatPalette.GRAY + "You need keys to get prize chests.");
        lore.add(ChatPalette.GRAY + "+1 key for killing the boss");
        lore.add(ChatPalette.GRAY + "+1 key for clearing all rooms");
        lore.add(ChatPalette.GRAY + "+1 key for finishing with less than 50 darkness");
        lore.add("");
        lore.add(ChatPalette.GRAY + "You can get maximum 3 keys.");
        lore.add(ChatPalette.GRAY + "Punch to get a prize chest using your key.");
        itemMeta.setLore(lore);
        prize.setItemMeta(itemMeta);
        this.setItem(8, prize);

        ItemStack darkness = new ItemStack(Material.PURPLE_WOOL);
        itemMeta = darkness.getItemMeta();
        itemMeta.setDisplayName(ChatPalette.PURPLE + "Guide: Darkness");
        lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatPalette.GRAY + "Darkness starts from 0 and can go up to 100.");
        lore.add(ChatPalette.GRAY + "Darkness increases with time and when you die.");
        lore.add("");
        lore.add(ChatPalette.GRAY + "Darkness makes the mobs stronger.");
        lore.add(ChatPalette.GRAY + "Darkness applies damage and defense...");
        lore.add(ChatPalette.GRAY + "...debuff to all players.");
        lore.add("");
        lore.add(ChatPalette.GRAY + "You will get +1 key for completing the...");
        lore.add(ChatPalette.GRAY + "...dungeon with less than 50 darkness.");
        itemMeta.setLore(lore);
        darkness.setItemMeta(itemMeta);
        this.setItem(7, darkness);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory != null && clickedInventory.getType().equals(InventoryType.CHEST)) {
            ItemStack current = event.getCurrentItem();
            Material currentType = current.getType();

            if (currentType.equals(Material.LIME_WOOL)) {
                int instanceNo = slotToInstance.get(event.getSlot());

                boolean joined = MiniGameManager.getDungeonInstance(code, instanceNo).joinQueue(player);
                if (joined) {
                    player.closeInventory();
                }
            }
        }
    }
}
