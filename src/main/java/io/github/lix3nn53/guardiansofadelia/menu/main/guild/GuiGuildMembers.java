package io.github.lix3nn53.guardiansofadelia.menu.main.guild;

import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guild.Guild;
import io.github.lix3nn53.guardiansofadelia.guild.GuildManager;
import io.github.lix3nn53.guardiansofadelia.guild.PlayerRankInGuild;
import io.github.lix3nn53.guardiansofadelia.menu.GuiHelper;
import io.github.lix3nn53.guardiansofadelia.menu.main.GuiGuild;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacterGui;
import io.github.lix3nn53.guardiansofadelia.text.locale.Translation;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiGuildMembers extends GuiGeneric {

    public GuiGuildMembers(Player player, GuardianData guardianData) {
        super(54, CustomCharacterGui.MENU_54_FLAT.toString() + ChatPalette.BLACK + Translation.t(guardianData, "menu.language.name"), 0);

        Guild guild = GuildManager.getGuild(player);

        List<ItemStack> itemStacks = new ArrayList<>();

        for (UUID uuid : guild.getMembers()) {
            OfflinePlayer member = Bukkit.getOfflinePlayer(uuid);
            PlayerProfile playerProfile = member.getPlayerProfile();

            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setOwnerProfile(playerProfile);
            skullMeta.setDisplayName(ChatPalette.BLUE_LIGHT + member.getName());
            ArrayList<String> lore = new ArrayList<>();
            lore.add("");
            PlayerRankInGuild rankInGuild = guild.getRankInGuild(uuid);
            lore.add(ChatPalette.GRAY + Translation.t(guardianData, "menu.guild.rank") + ": " + rankInGuild.getName());
            skullMeta.setLore(lore);
            itemStack.setItemMeta(skullMeta);

            itemStacks.add(itemStack);
        }

        GuiHelper.form54Small(this, itemStacks.toArray(new ItemStack[0]), "Settings");
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
            new GuiGuild(player, guardianData).openInventory(player);
        }
    }
}
