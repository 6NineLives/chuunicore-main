package io.github.lix3nn53.guardiansofadelia.commands.admin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import io.github.lix3nn53.guardiansofadelia.chat.SpeechBubble;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.onground.SkillOnGround;
import io.github.lix3nn53.guardiansofadelia.sounds.CustomSound;
import io.github.lix3nn53.guardiansofadelia.sounds.GoaSound;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import io.github.lix3nn53.guardiansofadelia.text.font.CustomCharacter;
import io.github.lix3nn53.guardiansofadelia.text.font.NegativeSpace;
import io.github.lix3nn53.guardiansofadelia.utilities.gui.GuiGeneric;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.UUID;

public class CommandTest implements CommandExecutor {

    boolean test = false;
    SkillOnGround skillOnGround;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equals("test")) {
            return false;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage(ChatPalette.BLUE + "/test test");
                player.sendMessage(ChatPalette.BLUE + "/test color <code>");
                player.sendMessage(ChatPalette.BLUE + "/test hex <code>");
                player.sendMessage(ChatPalette.BLUE + "/test palette");
                player.sendMessage(ChatPalette.BLUE + "/test model <item> <model>");
                player.sendMessage(ChatPalette.BLUE + "/test sound <code> - play custom sounds");
                player.sendMessage(ChatPalette.BLUE + "/test damage <amount> - damage self");
                player.sendMessage(ChatPalette.BLUE + "/test custom <custom> [sBefore] [sAfter] <negative> <positive> - test custom char");
            } else if (args[0].equals("test")) {
                /*int entityId = GuardiansOfAdelia.getEntityId();
                Location location = player.getLocation();
                CosmeticHologram cosmeticHologram = new CosmeticHologram(entityId, location, "TEST");

                cosmeticHologram.show(player);
                cosmeticHologram.mount(player, player);
                Cosmetic cosmetic = CosmeticManager.get(CosmeticType.COSMETIC_BACK.getIdOffset() + 1);
                cosmeticHologram.helmet(player, cosmetic.getShowcase(CosmeticColor.RED, 1));

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        cosmeticHologram.look(player, player.getLocation());
                        cosmeticHologram.rotateHead(player, player.getLocation());
                    }
                }.runTaskTimer(GuardiansOfAdelia.getInstance(), 2L, 10L);*/

                /*Location location = player.getLocation();
                ArmorStand cosmeticStand = location.getWorld().spawn(location, ArmorStand.class);
                cosmeticStand.setMarker(true);
                player.addPassenger(cosmeticStand);
                Cosmetic cosmetic = CosmeticManager.get(CosmeticType.COSMETIC_BACK.getIdOffset() + 1);
                cosmeticStand.getEquipment().setHelmet(cosmetic.getShowcase(CosmeticColor.RED, 1));

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        Location newLoc = player.getLocation();
                        cosmeticStand.setRotation(newLoc.getYaw(), newLoc.getPitch());
                    }
                }.runTaskTimer(GuardiansOfAdelia.getInstance(), 2L, 2L);*/

                /*PlayerInfoPacket playerInfoPacket = new PlayerInfoPacket(player);

                player.getNearbyEntities(20, 20, 20).forEach(e -> {
                    if (e instanceof Player target) {
                        target.hidePlayer(GuardiansOfAdelia.getInstance(), player);
                        playerInfoPacket.send(target);
                    }
                });*/

                /*PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
                packet.getIntegers().write(0, player.getEntityId());
                List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairs = new ArrayList<>();
                ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
                pairs.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, helmet));
                packet.getSlotStackPairLists().writeSafely(0, pairs);
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);*/
//                EntityEquipmentPacket entityEquipmentPacket = new EntityEquipmentPacket(player.getEntityId(), new ItemStack(Material.CARVED_PUMPKIN));
//                entityEquipmentPacket.load().send(player);

                SpeechBubble.player(player, "holoMessage");

                int entityID = new Random().nextInt();
                final PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);

                final int entityType = Integer.parseInt(args[1]);
                final int extraData = 1;
                packetContainer.getIntegers().write(0, entityID);
                packetContainer.getIntegers().write(1, entityType);
                packetContainer.getIntegers().write(2, extraData);

                packetContainer.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);

                packetContainer.getUUIDs().write(0, UUID.randomUUID());

                Location location = player.getLocation();
                packetContainer.getDoubles().write(0, location.getX());
                packetContainer.getDoubles().write(1, location.getY());
                packetContainer.getDoubles().write(2, location.getZ());

                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
            } else if (args[0].equals("custom")) {
                CustomCharacter chara = CustomCharacter.valueOf(args[1]);
                chara.setNegativeSpacesBefore(Integer.parseInt(args[2]));
                chara.setNegativeSpacesAfter(Integer.parseInt(args[3]));
                chara.setNegativeSpace(NegativeSpace.valueOf(args[4]));
                chara.setPositiveSpace(NegativeSpace.valueOf(args[5]));

                player.sendMessage("BEFORE" + chara + "AFTER");
                //test custom LOGO 1 20 NEGATIVE_8 POSITIVE_8
                //test custom COMPASS_EMPTY 1 1 NEGATIVE_8 POSITIVE_8
            } else if (args[0].equals("customgui")) {
                CustomCharacter menuContent = CustomCharacter.valueOf(args[1]);
                CustomCharacter logo = CustomCharacter.valueOf(args[2]);
                menuContent.setNegativeSpacesBefore(Integer.parseInt(args[3]));
                menuContent.setNegativeSpacesAfter(Integer.parseInt(args[4]));
                menuContent.setNegativeSpace(NegativeSpace.valueOf(args[5]));
                menuContent.setPositiveSpace(NegativeSpace.valueOf(args[6]));

                logo.setNegativeSpacesBefore(Integer.parseInt(args[7]));
                logo.setNegativeSpacesAfter(Integer.parseInt(args[8]));
                logo.setNegativeSpace(NegativeSpace.valueOf(args[9]));
                logo.setPositiveSpace(NegativeSpace.valueOf(args[10]));

                String s1 = menuContent.toString();
                String s2 = logo.toString();

                GuiGeneric guiGeneric = new GuiGeneric(54, s1 + s2 + ChatColor.DARK_GRAY + "TEST TITLE", 0);
                guiGeneric.openInventory(player);
                //test customgui MENU_54 LOGO 1 41 NEGATIVE_4 POSITIVE_4 -5 37 NEGATIVE_4 POSITIVE_4
                //test customgui MENU_54_FLAT LOGO 5 41 NEGATIVE_4 POSITIVE_4 -5 37 NEGATIVE_4 POSITIVE_4
            } else if (args[0].equals("palette")) {
                StringBuilder msg = new StringBuilder();
                for (ChatPalette chatPalette : ChatPalette.values()) {
                    msg.append(chatPalette).append("⬛");
                }

                player.sendMessage(msg.toString());
            } else if (args[0].equals("color")) {
                ChatPalette chatPalette = ChatPalette.valueOf(args[1].toUpperCase());

                player.sendMessage(chatPalette + "TEST COLOR");
            } else if (args[0].equals("hex")) {
                ChatColor chatPalette = ChatColor.of(args[1].toUpperCase());

                player.sendMessage(chatPalette + "TEST COLOR");
            } else if (args[0].equals("sound")) {
                if (args.length == 2) {
                    GoaSound goaSound = GoaSound.valueOf(args[1]);
                    CustomSound customSound = goaSound.getCustomSound();
                    customSound.play(player);
                }
            } else if (args[0].equals("model")) {
                if (args.length == 3) {
                    Location location = player.getLocation();
                    ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
                    ItemStack itemStack = new ItemStack(Material.valueOf(args[1].toUpperCase()));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setCustomModelData(Integer.valueOf(args[2]));
                    itemMeta.setUnbreakable(true);
                    itemStack.setItemMeta(itemMeta);
                    EntityEquipment equipment = armorStand.getEquipment();
                    equipment.setHelmet(itemStack);
                    armorStand.setVisible(false);
                    armorStand.setInvulnerable(true);
                    armorStand.setGravity(false);
                }
            } else if (args[0].equals("damage")) {
                float damage = Float.parseFloat(args[1]);

                player.damage(damage);
            }

            // If the player (or console) uses our command correct, we can return true
            return true;
        }
        return false;
    }
}
