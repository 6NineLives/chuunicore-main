package io.github.lix3nn53.guardiansofadelia.bungeelistener;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.bungeelistener.products.BoostPremium;
import io.github.lix3nn53.guardiansofadelia.bungeelistener.web.WebProduct;
import io.github.lix3nn53.guardiansofadelia.bungeelistener.web.WebProductType;
import io.github.lix3nn53.guardiansofadelia.bungeelistener.web.WebPurchase;
import io.github.lix3nn53.guardiansofadelia.bungeelistener.web.WebResponse;
import io.github.lix3nn53.guardiansofadelia.chat.PremiumRank;
import io.github.lix3nn53.guardiansofadelia.database.DatabaseQueries;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.text.ChatPalette;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class RequestHandler {

    private final static HashMap<Integer, WebProduct> productIdToWebProduct = new HashMap<>();

    static {
        //list of ranks
        productIdToWebProduct.put(1, new WebProduct(ChatPalette.GREEN_DARK + "Hero Rank", WebProductType.RANK, 200, null, PremiumRank.HERO));
        productIdToWebProduct.put(2, new WebProduct(ChatPalette.GOLD + "Legend Rank", WebProductType.RANK, 400, null, PremiumRank.LEGEND));
        productIdToWebProduct.put(3, new WebProduct(ChatPalette.PURPLE_LIGHT + "Titan Rank", WebProductType.RANK, 600, null, PremiumRank.TITAN));

        ItemStack boostExp1 = BoostPremium.EXPERIENCE.getItemStack(1);
        ItemStack boostExp2 = BoostPremium.EXPERIENCE.getItemStack(2);
        ItemStack boostExp5 = BoostPremium.EXPERIENCE.getItemStack(5);
        ItemStack boostLoot1 = BoostPremium.LOOT.getItemStack(1);
        ItemStack boostLoot2 = BoostPremium.LOOT.getItemStack(2);
        ItemStack boostLoot5 = BoostPremium.LOOT.getItemStack(5);
        ItemStack boostEnchant1 = BoostPremium.ENCHANT.getItemStack(1);
        ItemStack boostEnchant2 = BoostPremium.ENCHANT.getItemStack(2);
        ItemStack boostEnchant5 = BoostPremium.ENCHANT.getItemStack(5);
        ItemStack boostGather1 = BoostPremium.GATHER.getItemStack(1);
        ItemStack boostGather2 = BoostPremium.GATHER.getItemStack(2);
        ItemStack boostGather5 = BoostPremium.GATHER.getItemStack(5);
        productIdToWebProduct.put(21, new WebProduct(ChatPalette.PURPLE_LIGHT + "Experience Boost x1", WebProductType.ITEM, 50, boostExp1));
        productIdToWebProduct.put(22, new WebProduct(ChatPalette.PURPLE_LIGHT + "Experience Boost x2", WebProductType.ITEM, 100, boostExp2));
        productIdToWebProduct.put(23, new WebProduct(ChatPalette.PURPLE_LIGHT + "Experience Boost x5", WebProductType.ITEM, 200, boostExp5));
        productIdToWebProduct.put(24, new WebProduct(ChatPalette.YELLOW + "Loot Boost x1", WebProductType.ITEM, 50, boostLoot1));
        productIdToWebProduct.put(25, new WebProduct(ChatPalette.YELLOW + "Loot Boost x2", WebProductType.ITEM, 100, boostLoot2));
        productIdToWebProduct.put(26, new WebProduct(ChatPalette.YELLOW + "Loot Boost x5", WebProductType.ITEM, 200, boostLoot5));
        productIdToWebProduct.put(27, new WebProduct(ChatPalette.BLUE_LIGHT + "Enchant Boost x1", WebProductType.ITEM, 50, boostEnchant1));
        productIdToWebProduct.put(28, new WebProduct(ChatPalette.BLUE_LIGHT + "Enchant Boost x2", WebProductType.ITEM, 100, boostEnchant2));
        productIdToWebProduct.put(29, new WebProduct(ChatPalette.BLUE_LIGHT + "Enchant Boost x5", WebProductType.ITEM, 200, boostEnchant5));
        productIdToWebProduct.put(30, new WebProduct(ChatPalette.GREEN_DARK + "Gather Boost x1", WebProductType.ITEM, 50, boostGather1));
        productIdToWebProduct.put(31, new WebProduct(ChatPalette.GREEN_DARK + "Gather Boost x2", WebProductType.ITEM, 100, boostGather2));
        productIdToWebProduct.put(32, new WebProduct(ChatPalette.GREEN_DARK + "Gather Boost x5", WebProductType.ITEM, 200, boostGather5));
    }

    static WebResponse onPurchase(WebPurchase webPurchase) {
        String minecraftUuidString = webPurchase.getMinecraftUuid();
        UUID minecraftUuid = UUID.fromString(minecraftUuidString);
        int productId = webPurchase.getProductId();
        int payment = webPurchase.getPayment();

        String minecraftUsername = "NULL";

        if (!productIdToWebProduct.containsKey(productId)) {
            return new WebResponse(false, "No such product1", minecraftUuidString, minecraftUsername, productId);
        }

        WebProduct webProduct = productIdToWebProduct.get(productId);
        int cost = webProduct.getCost();

        if (cost != payment) {
            return new WebResponse(false, "No such product2", minecraftUuidString, minecraftUsername, productId);
        }

        WebProductType type = webProduct.getType();
        if (type.equals(WebProductType.ITEM)) {
            ItemStack itemStack = webProduct.getItemStack();

            Player player = Bukkit.getPlayer(minecraftUuid);
            if (player != null) {
                minecraftUsername = player.getName();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatPalette.RED + "Closing your inventory for security to complete your web purchase.");
                        player.closeInventory();
                    }
                }.runTask(GuardiansOfAdelia.getInstance());

                if (GuardianDataManager.hasGuardianData(player)) {
                    GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                    boolean success = guardianData.addToPremiumStorage(itemStack);
                    if (!success) {
                        return new WebResponse(false, "Your premium-storage is full!", minecraftUuidString, minecraftUsername, productId);
                    }
                }
            } else { //player is offline
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(minecraftUuid);
                UUID uuid = offlinePlayer.getUniqueId();

                if (!uuidExists(uuid)) {
                    return new WebResponse(false, "You must be logged in to game server at least once!", minecraftUuidString, minecraftUsername, productId);
                }

                minecraftUsername = offlinePlayer.getName();

                try {
                    ItemStack[] premiumStorage = DatabaseQueries.getPremiumStorage(uuid);

                    List<ItemStack> list = new ArrayList<>();

                    if (premiumStorage != null) list = new ArrayList<>(Arrays.asList(premiumStorage));

                    if (list.size() >= 54) {
                        return new WebResponse(false, "Your premium-storage is full!", minecraftUuidString, minecraftUsername, productId);
                    }

                    list.add(itemStack);
                    ItemStack[] newPremiumStorage = list.toArray(new ItemStack[0]);
                    DatabaseQueries.setPremiumStorage(uuid, newPremiumStorage);
                } catch (Exception e) {
                    e.printStackTrace();

                    return new WebResponse(false, "A database error occurred.", minecraftUuidString, minecraftUsername, productId);
                }
            }
        } else if (type.equals(WebProductType.RANK)) {
            PremiumRank premiumRank = webProduct.getPremiumRank();

            Player player = Bukkit.getPlayer(minecraftUuid);
            if (player != null) {
                minecraftUsername = player.getName();
                PremiumRank currentRank = null;
                GuardianData guardianData;
                if (GuardianDataManager.hasGuardianData(player)) {
                    guardianData = GuardianDataManager.getGuardianData(player);
                    currentRank = guardianData.getPremiumRank();
                    if (currentRank != null) {
                        if (currentRank.ordinal() >= premiumRank.ordinal()) {
                            return new WebResponse(false, "You already have a rank that is higher or equal to this rank.", minecraftUuidString, minecraftUsername, productId);
                        }
                    }
                    guardianData.setPremiumRank(premiumRank);
                }

                try {
                    UUID uuid = player.getUniqueId();
                    DatabaseQueries.setPremiumRankWithDate(uuid, premiumRank);
                } catch (Exception e) {
                    e.printStackTrace();
                    //Revert online player rank if we get a database error
                    if (currentRank != null) {
                        guardianData = GuardianDataManager.getGuardianData(player);
                        guardianData.setPremiumRank(currentRank);
                    }
                    return new WebResponse(false, "A database error occurred.", minecraftUuidString, minecraftUsername, productId);
                }
            } else { //player is offline
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(minecraftUuid);
                UUID uuid = offlinePlayer.getUniqueId();

                if (!uuidExists(uuid)) {
                    return new WebResponse(false, "You must be logged in to game server at least once!", minecraftUuidString, minecraftUsername, productId);
                }

                try {
                    PremiumRank currentRank = DatabaseQueries.getPremiumRank(uuid);
                    if (currentRank != null) {
                        if (currentRank.ordinal() >= premiumRank.ordinal()) {
                            return new WebResponse(false, "You already have a rank that is higher or equal to this rank.", minecraftUuidString, minecraftUsername, productId);
                        }
                    }

                    DatabaseQueries.setPremiumRankWithDate(uuid, premiumRank);

                    minecraftUsername = offlinePlayer.getName();
                } catch (Exception e) {
                    e.printStackTrace();

                    return new WebResponse(false, "A database error occurred.", minecraftUuidString, minecraftUsername, productId);
                }
            }
        }

        GuardiansOfAdelia.getInstance().getLogger().info("Web purchase: " + minecraftUsername + " bought " + webProduct.getProductName() + " for " + payment + " credits!");
        return new WebResponse(true, "Item purchased successfully!", minecraftUuidString, minecraftUsername, productId);
    }

    public static void test(int productId, Player player) {
        WebProduct webProduct = productIdToWebProduct.get(productId);

        WebProductType type = webProduct.getType();
        if (type.equals(WebProductType.ITEM)) {
            ItemStack itemStack = webProduct.getItemStack();

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(ChatPalette.RED + "Closing your inventory for security to complete your web purchase.");
                    player.closeInventory();
                }
            }.runTask(GuardiansOfAdelia.getInstance());

            if (GuardianDataManager.hasGuardianData(player)) {
                GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                boolean success = guardianData.addToPremiumStorage(itemStack);
            }

        } else if (type.equals(WebProductType.RANK)) {
            PremiumRank premiumRank = webProduct.getPremiumRank();

            if (GuardianDataManager.hasGuardianData(player)) {
                GuardianData guardianData = GuardianDataManager.getGuardianData(player);
                guardianData.setPremiumRank(premiumRank);
            }
        }


        //Bukkit.broadcastMessage(ChatPalette.GOLD + "Thanks for your support! " + ChatPalette.GRAY + player.getName() + " bought " + webProduct.getProductName() + ChatPalette.GRAY + " from web-store!");
    }

    private static boolean uuidExists(UUID uuid) {
        return DatabaseQueries.uuidExists(uuid);
    }

    public static List<ItemStack> getSkinChestItemPool() {
        List<ItemStack> skinPool = new ArrayList<>();

        for (int id : productIdToWebProduct.keySet()) {
            WebProduct webProduct = productIdToWebProduct.get(id);

            WebProductType type = webProduct.getType();

            if (type.equals(WebProductType.ITEM)) {
                if (id < 18) {
                    skinPool.add(webProduct.getItemStack());
                }
            }
        }

        return skinPool;
    }
}
