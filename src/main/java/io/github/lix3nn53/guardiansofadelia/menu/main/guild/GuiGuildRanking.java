package io.github.lix3nn53.guardiansofadelia.menu.main.guild;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.database.DatabaseQueries;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guild.Guild;
import io.github.lix3nn53.guardiansofadelia.guild.GuildManager;
import io.github.lix3nn53.guardiansofadelia.items.list.OtherItems;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiGuild;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiGuildEmpty;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GuiGuildRanking extends GuiGeneric {

    private final int[] indexes = new int[]{13, 21, 23, 29, 33, 37, 43, 45, 49, 53};

    public GuiGuildRanking(GuardianData guardianData) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + Translation.t(guardianData, "menu.guild.name"), 0);

        ItemStack backButton = OtherItems.getBackButton("Guild");
        this.setItem(0, backButton);

        new BukkitRunnable() {
            @Override
            public void run() {
                List<Guild> top10Guilds = DatabaseQueries.getTop10Guilds();

                for (int i = 0; i < 10; i++) {
                    ItemStack itemStack = new ItemStack(Material.PAPER);
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    if (i < top10Guilds.size()) {
                        Guild guild = top10Guilds.get(i);

                        itemMeta.setDisplayName(ChatPalette.GRAY.toString() + (i + 1) + " - " + ChatPalette.WHITE + guild.getName());
                        List<String> lore = new ArrayList<>();
                        lore.add("");
                        lore.add(ChatPalette.GRAY + Translation.t(guardianData, "menu.guild.war_point") + ": " + ChatPalette.WHITE + guild.getWarPoints());
                        itemMeta.setLore(lore);
                    } else {
                        itemMeta.setDisplayName(ChatPalette.GRAY.toString() + (i + 1) + " - " + ChatPalette.WHITE + Translation.t(guardianData, "menu.guild.empty"));
                    }

                    itemStack.setItemMeta(itemMeta);
                    setItem(indexes[i], itemStack);
                }
            }
        }.runTaskAsynchronously(GuardiansOfAdelia.getInstance());
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

        int slot = event.getSlot();
        if (slot == 0) {
            GuiGeneric gui;
            if (GuildManager.inGuild(player)) {
                gui = new GuiGuild(player, guardianData);
            } else {
                gui = new GuiGuildEmpty(player, guardianData);
            }
            gui.openInventory(player);
        }
    }
}
