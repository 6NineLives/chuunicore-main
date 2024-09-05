package io.github.lix3nn53.guardiansofadelia.database;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.chat.*;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.attribute.AttributeType;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClassStats;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillBarData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillRPGClassData;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.player.SkillTreeData;
import io.github.lix3nn53.guardiansofadelia.guild.Guild;
import io.github.lix3nn53.guardiansofadelia.guild.PlayerRankInGuild;
import io.github.lix3nn53.guardiansofadelia.jobs.RPGCharacterCraftingStats;
import io.github.lix3nn53.guardiansofadelia.jobs.gathering.GatheringToolType;
import io.github.lix3nn53.guardiansofadelia.npc.QuestNPCManager;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.quests.task.Task;
import io.github.lix3nn53.guardiansofadelia.rpginventory.RPGInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DatabaseQueries {

    public static void onDisable() {
        ConnectionPool.closePool();
    }

    public static void createTables() {
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_player` (\n" +
                    "     `uuid` varchar(40) NOT NULL,\n" +
                    "     `daily_last_date` date NULL,\n" +
                    "     `staff_rank` varchar(20) NULL,\n" +
                    "     `premium_rank` varchar(20) NULL,\n" +
                    "     `premium_rank_date` date NULL,\n" +
                    "     `storage_personal` mediumtext NULL,\n" +
                    "     `storage_bazaar` mediumtext NULL,\n" +
                    "     `storage_premium` mediumtext NULL,\n" +
                    "     `lang` varchar(20) NOT NULL,\n" +
                    "     `friend_uuids` text,\n" +
                    "     `chat_channels` text,\n" +
                    "     `cosmetics` text,\n" +
                    "     PRIMARY KEY (`uuid`)\n" +
                    ");");
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_player_character` (\n" +
                    "     `character_no` smallint NOT NULL,\n" +
                    "     `uuid` varchar(40) NOT NULL,\n" +
                    "     `off_hand` text NULL,\n" +
                    "     `slot_parrot` text NULL,\n" +
                    "     `slot_necklace` text NULL,\n" +
                    "     `slot_ring` text NULL,\n" +
                    "     `slot_earring` text NULL,\n" +
                    "     `slot_glove` text NULL,\n" +
                    "     `slot_pet` text NULL,\n" +
                    "     `chat_tag` varchar(45) NULL,\n" +
                    "     `crafting_experiences` text NOT NULL,\n" +
                    "     `inventory` mediumtext NOT NULL,\n" +
                    "     `turnedinquests` text NULL,\n" +
                    "     `activequests` text NULL,\n" +
                    "     `location` text NOT NULL,\n" +
                    "     `armor_content` text NOT NULL,\n" +
                    "     `rpg_class` varchar(45) NOT NULL,\n" +
                    "     `totalexp` int NOT NULL,\n" +
                    "     `slot_tool_axe` text,\n" +
                    "     `slot_tool_bottle` text,\n" +
                    "     `slot_tool_hoe` text,\n" +
                    "     `slot_tool_pickaxe` text,\n" +
                    "     UNIQUE KEY `Ind_88` (`uuid`, `character_no`),\n" +
                    "     KEY `fkIdx_55` (`uuid`),\n" +
                    "     CONSTRAINT `FK_55` FOREIGN KEY `fkIdx_55` (`uuid`) REFERENCES `goa_player` (`uuid`)\n" +
                    ");");
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_player_character_class` (\n" +
                    "     `class_name` varchar(40) NOT NULL,\n" +
                    "     `uuid` varchar(40) NOT NULL,\n" +
                    "     `character_no` smallint NOT NULL,\n" +
                    "     `skill_points` mediumtext,\n" +
                    "     `attribute_points` mediumtext,\n" +
                    "     `skill_bar` mediumtext,\n" +
                    "     UNIQUE KEY `Index_228` (`class_name`, `uuid`, `character_no`),\n" +
                    "     KEY `FK_215` (`uuid`),\n" +
                    "     CONSTRAINT `FK_213` FOREIGN KEY `FK_215` (`uuid`) REFERENCES `goa_player` (`uuid`)\n" +
                    ");");
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_player_web` (\n" +
                    "     `uuid` varchar(40) NULL,\n" +
                    "     `email` varchar(45) NULL,\n" +
                    "     `credits` smallint NOT NULL,\n" +
                    "     `sessions` text NULL,\n" +
                    "     `twitch_id` varchar(40) NOT NULL,\n" +
                    "     `discord_id` varchar(40) NOT NULL,\n" +
                    "     `google_id` varchar(40) NOT NULL,\n" +
                    "     KEY `fkIdx_188` (`uuid`),\n" +
                    "     CONSTRAINT `FK_187` FOREIGN KEY `fkIdx_188` (`uuid`) REFERENCES `goa_player` (`uuid`)\n" +
                    ");");
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_guild` (\n" +
                    "     `name` varchar(20) NOT NULL,\n" +
                    "     `tag` varchar(5) NOT NULL,\n" +
                    "     `war_point` smallint NULL,\n" +
                    "     `announcement` tinytext NULL,\n" +
                    "     `hall_level` smallint NOT NULL,\n" +
                    "     `bank_level` smallint NOT NULL,\n" +
                    "     `lab_level` smallint NOT NULL,\n" +
                    "     `storage` mediumtext NULL,\n" +
                    "     PRIMARY KEY (`name`)\n" +
                    ");");
            statement.addBatch("CREATE TABLE IF NOT EXISTS `goa_player_guild` (\n" +
                    "     `uuid` varchar(40) NOT NULL,\n" +
                    "     `name` varchar(20) NOT NULL,\n" +
                    "     `rank` varchar(20) NOT NULL,\n" +
                    "     PRIMARY KEY (`uuid`),\n" +
                    "     KEY `fkIdx_38` (`uuid`),\n" +
                    "     CONSTRAINT `FK_38` FOREIGN KEY `fkIdx_38` (`uuid`) REFERENCES `goa_player` (`uuid`),\n" +
                    "     KEY `fkIdx_41` (`name`),\n" +
                    "     CONSTRAINT `FK_41` FOREIGN KEY `fkIdx_41` (`name`) REFERENCES `goa_guild` (`name`)\n" +
                    ");");

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //GETTERS

    public static HashMap<UUID, PlayerRankInGuild> getGuildMembers(String guild) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_player_guild WHERE name = ?";
        HashMap<UUID, PlayerRankInGuild> members = new HashMap<>();
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, guild);

            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                String uuidString = resultSet.getString("uuid");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    String rankString = resultSet.getString("rank");
                    if (!resultSet.wasNull()) {
                        //if NOT NULL
                        PlayerRankInGuild rank = PlayerRankInGuild.valueOf(rankString);
                        members.put(UUID.fromString(uuidString), rank);
                    }

                }
            }
            resultSet.close();
            pst.close();
        }
        return members;
    }

    public static String getGuildNameOfPlayer(UUID uuid) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_player_guild WHERE uuid = ?";
        String guildName = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                guildName = resultSet.getString("name");
            }
            resultSet.close();
            pst.close();
        }
        return guildName;
    }

    public static GuardianData getGuardianData(UUID uuid) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_player WHERE uuid = ?";
        GuardianData guardianData = new GuardianData();
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String staffRankString = resultSet.getString("staff_rank");
                if (!resultSet.wasNull()) {
                    StaffRank staffRank = StaffRank.valueOf(staffRankString);
                    guardianData.setStaffRank(staffRank);
                }

                String premiumRankString = resultSet.getString("premium_rank");
                if (!resultSet.wasNull()) {
                    PremiumRank premiumRank = PremiumRank.valueOf(premiumRankString);
                    guardianData.setPremiumRank(premiumRank);
                }

                String storagePersonalString = resultSet.getString("storage_personal");
                if (!resultSet.wasNull()) {
                    ItemStack[] itemStacks = ItemSerializer.itemStackArrayFromBase64(storagePersonalString);
                    guardianData.setPersonalStorage(itemStacks);
                }

                String storageBazaarString = resultSet.getString("storage_bazaar");
                if (!resultSet.wasNull()) {
                    ItemStack[] itemStacks = ItemSerializer.itemStackArrayFromBase64(storageBazaarString);
                    guardianData.setBazaarStorage(itemStacks);
                }

                String storagePremiumString = resultSet.getString("storage_premium");
                if (!resultSet.wasNull()) {
                    ItemStack[] itemStacks = ItemSerializer.itemStackArrayFromBase64(storagePremiumString);
                    guardianData.setPremiumStorage(itemStacks);
                }

                LocalDate dailyLastDate = resultSet.getObject("daily_last_date", LocalDate.class);
                if (!resultSet.wasNull()) {
                    guardianData.getDailyRewardInfo().setLastObtainDate(dailyLastDate);
                }

                String lang = resultSet.getString("lang");
                if (!resultSet.wasNull()) {
                    guardianData.setLanguage(lang);
                }

                String friendUUIDs = resultSet.getString("friend_uuids");
                if (!resultSet.wasNull()) {
                    List<OfflinePlayer> friends = new ArrayList<>();

                    String[] split = friendUUIDs.split(";");
                    for (String s : split) {
                        UUID friendUUID = UUID.fromString(s);
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(friendUUID);
                        friends.add(offlinePlayer);
                    }

                    guardianData.setFriends(friends);
                }

                String chatChannels = resultSet.getString("chat_channels");
                if (!resultSet.wasNull()) {
                    ChatChannelData chatChannelData = guardianData.getChatChannelData();

                    String[] split = chatChannels.split(";");
                    for (String s : split) {
                        ChatChannel chatChannel = ChatChannel.valueOf(s);
                        chatChannelData.addListening(chatChannel);
                    }
                }

                String cosmeticsStr = resultSet.getString("cosmetics");
                if (!resultSet.wasNull()) {
                    List<Integer> cosmetics = new ArrayList<>();

                    String[] split = cosmeticsStr.split(";");
                    for (String s : split) {
                        if (s.equals("")) {
                            continue;
                        }

                        int cosmeticId = Integer.parseInt(s);
                        cosmetics.add(cosmeticId);
                    }

                    guardianData.setUnlockedCosmetics(cosmetics);
                }
            }
            resultSet.close();
            pst.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return guardianData;
    }

    public static ItemStack[] getPremiumStorage(UUID uuid) throws SQLException {
        String SQL_QUERY = "SELECT storage_premium FROM goa_player WHERE uuid = ?";
        ItemStack[] itemStacks = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String storagePremiumString = resultSet.getString("storage_premium");
                if (!resultSet.wasNull()) {
                    itemStacks = ItemSerializer.itemStackArrayFromBase64(storagePremiumString);
                }
            }
            resultSet.close();
            pst.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemStacks;
    }

    public static RPGCharacter getCharacterAndSetPlayerInventory(Player player, int characterNo) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        RPGCharacter rpgCharacter = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            UUID uuid = player.getUniqueId();
            pst.setString(1, uuid.toString());
            pst.setInt(2, characterNo);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String rpgClassStr = resultSet.getString("rpg_class").toUpperCase();

                HashMap<String, RPGClassStats> rpgClassStatsMap = loadRPGClassStats(uuid, characterNo);

                rpgCharacter = new RPGCharacter(rpgClassStr, player, rpgClassStatsMap.get(rpgClassStr));
                RPGInventory rpgInventory = rpgCharacter.getRpgInventory();

                RPGCharacterStats rpgCharacterStats = rpgCharacter.getRpgCharacterStats();

                int totalexp = resultSet.getInt("totalexp");
                rpgCharacterStats.setTotalExp(totalexp);

                String offHand = resultSet.getString("off_hand");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    player.getInventory().setItemInOffHand(ItemSerializer.itemStackFromBase64(offHand));
                }

                String parrot = resultSet.getString("slot_parrot");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                        try {
                            rpgInventory.setParrot(ItemSerializer.itemStackFromBase64(parrot), player);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                String necklace = resultSet.getString("slot_necklace");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                        try {
                            rpgInventory.setNecklace(ItemSerializer.itemStackFromBase64(necklace), player);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                String ring = resultSet.getString("slot_ring");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                        try {
                            rpgInventory.setRing(ItemSerializer.itemStackFromBase64(ring), player);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                String earring = resultSet.getString("slot_earring");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                        try {
                            rpgInventory.setEarring(ItemSerializer.itemStackFromBase64(earring), player);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                String glove = resultSet.getString("slot_glove");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                        try {
                            rpgInventory.setGlove(ItemSerializer.itemStackFromBase64(glove), player);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                String pet = resultSet.getString("slot_pet");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                        try {
                            rpgInventory.setEgg(ItemSerializer.itemStackFromBase64(pet), player);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    Bukkit.getScheduler().runTaskLater(GuardiansOfAdelia.getInstance(), () -> PetManager.onEggEquip(player), 40L);
                }

                String toolAxe = resultSet.getString("slot_tool_axe");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                        try {
                            rpgInventory.setTool(GatheringToolType.AXE, ItemSerializer.itemStackFromBase64(toolAxe));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                String toolBottle = resultSet.getString("slot_tool_bottle");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                        try {
                            rpgInventory.setTool(GatheringToolType.BOTTLE, ItemSerializer.itemStackFromBase64(toolBottle));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                String toolHoe = resultSet.getString("slot_tool_hoe");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                        try {
                            rpgInventory.setTool(GatheringToolType.HOE, ItemSerializer.itemStackFromBase64(toolHoe));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                String toolPickaxe = resultSet.getString("slot_tool_pickaxe");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    Bukkit.getScheduler().runTask(GuardiansOfAdelia.getInstance(), () -> {
                        try {
                            rpgInventory.setTool(GatheringToolType.PICKAXE, ItemSerializer.itemStackFromBase64(toolPickaxe));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                String craftingExperiencesString = resultSet.getString("crafting_experiences");
                if (!resultSet.wasNull()) {
                    rpgCharacter.getCraftingStats().loadDatabaseString(craftingExperiencesString);
                }

                String chatTagString = resultSet.getString("chat_tag");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    rpgCharacter.setChatTag(player, ChatTag.valueOf(chatTagString));
                }

                String activeQuestsString = resultSet.getString("activequests");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    List<Quest> questList = new ArrayList<>();

                    String[] allQuestsWithTheirTasks = activeQuestsString.split(";");
                    for (String aQuestAndItsTasksString : allQuestsWithTheirTasks) {
                        if (!aQuestAndItsTasksString.equals("")) {
                            String[] aQuestAndItsTasksArray = aQuestAndItsTasksString.split("-");
                            Quest quest = QuestNPCManager.getQuestCopyById(Integer.parseInt(aQuestAndItsTasksArray[0]));
                            List<Task> tasks = quest.getTasks();
                            for (int i = 1; i < aQuestAndItsTasksArray.length; i++) {
                                int taskIndex = i - 1;
                                if (taskIndex < tasks.size()) {
                                    tasks.get(taskIndex).setProgress(Integer.parseInt(aQuestAndItsTasksArray[i]));
                                }
                            }
                            questList.add(quest);
                        }
                    }
                    rpgCharacter.setQuestList(questList);
                }

                String turnedInQuestNumbersArray = resultSet.getString("turnedinquests");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    List<Integer> turnedInQuestNumberList = new ArrayList<>();

                    String[] turnedInQuestNumbersString = turnedInQuestNumbersArray.split(";");
                    for (String aTurnedInQuestNumberString : turnedInQuestNumbersString) {
                        if (!aTurnedInQuestNumberString.equals("")) {
                            turnedInQuestNumberList.add(Integer.parseInt(aTurnedInQuestNumberString));
                        }
                    }

                    rpgCharacter.setTurnedInQuests(turnedInQuestNumberList);
                }

                String inventoryString = resultSet.getString("inventory");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    ItemStack[] itemStacks = ItemSerializer.itemStackArrayFromBase64(inventoryString);
                    player.getInventory().setContents(itemStacks);
                }

                String armorContentString = resultSet.getString("armor_content");
                if (!resultSet.wasNull()) {
                    //if NOT NULL
                    ItemStack[] itemStacks = ItemSerializer.itemStackArrayFromBase64(armorContentString);
                    player.getInventory().setArmorContents(itemStacks);
                }
            }
            resultSet.close();
            pst.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rpgCharacter;
    }

    public static HashMap<String, RPGClassStats> loadRPGClassStats(UUID uuid, int characterNo) {
        String SQL_QUERY = "SELECT * FROM goa_player_character_class WHERE uuid = ? AND character_no = ?";

        HashMap<String, RPGClassStats> result = new HashMap<>();
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, characterNo);

            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                String className = resultSet.getString("class_name");
                String skillPointsTotal = resultSet.getString("skill_points");
                String attributePoints = resultSet.getString("attribute_points");
                String skillBar = resultSet.getString("skill_bar");

                HashMap<Integer, Integer> investedSkillPoints = new HashMap<>();
                if (skillPointsTotal != null) {
                    String[] skillPoints = skillPointsTotal.split(";");
                    for (int i = 0; i < skillPoints.length; i++) {
                        String skillPoint = skillPoints[i];
                        String[] skillPointSplit = skillPoint.split(",");

                        int skillId = Integer.parseInt(skillPointSplit[0]);
                        int investedPoints = Integer.parseInt(skillPointSplit[1]);

                        investedSkillPoints.put(skillId, investedPoints);
                    }
                }
                SkillTreeData skillTreeData = new SkillTreeData(investedSkillPoints);

                HashMap<AttributeType, Integer> investedAttributePoints = new HashMap<>();
                if (attributePoints != null) {
                    String[] attrSplit = attributePoints.split(";");
                    for (String attrWithPointStr : attrSplit) {
                        String[] attrWithPoint = attrWithPointStr.split(",");
                        AttributeType attributeType = AttributeType.valueOf(attrWithPoint[0]);
                        int investedPoints = Integer.parseInt(attrWithPoint[1]);
                        investedAttributePoints.put(attributeType, investedPoints);
                    }
                }

                int barOne = -1;
                int barTwo = -1;
                int barThree = -1;
                int barFour = -1;
                if (skillBar != null) {
                    String[] skillBarSplit = skillBar.split(";");
                    for (int i = 0; i < 4; i++) {
                        String skillBarString = skillBarSplit[i];
                        if (i == 0) {
                            barOne = Integer.parseInt(skillBarString);
                        } else if (i == 1) {
                            barTwo = Integer.parseInt(skillBarString);
                        } else if (i == 2) {
                            barThree = Integer.parseInt(skillBarString);
                        } else {
                            barFour = Integer.parseInt(skillBarString);
                        }
                    }
                }
                SkillBarData skillBarData = new SkillBarData(barOne, barTwo, barThree, barFour);
                SkillRPGClassData skillRPGClassData = new SkillRPGClassData(skillTreeData, skillBarData);

                result.put(className, new RPGClassStats(skillRPGClassData, investedAttributePoints));
            }
            resultSet.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static Guild getGuild(String name) throws SQLException {
        String SQL_QUERY = "SELECT * FROM goa_guild WHERE name = ?";
        Guild guild = null;

        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, name);
            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                guild = guildFromResultSet(resultSet);
            }
            resultSet.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return guild;
    }

    public static Location getLastLocationOfCharacter(UUID uuid, int charNo) throws SQLException {
        String SQL_QUERY = "SELECT location FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        Location location = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String locationString = resultSet.getString("location");
                String[] locationArray = locationString.split(";");
                World world = Bukkit.getWorld(locationArray[0]);
                int x = Integer.parseInt(locationArray[1]);
                int y = Integer.parseInt(locationArray[2]);
                int z = Integer.parseInt(locationArray[3]);
                location = new Location(world, x, y, z);
            }
            resultSet.close();
            pst.close();
        }
        return location;
    }

    public static String getRPGClassCharacter(UUID uuid, int charNo) throws SQLException {
        String SQL_QUERY = "SELECT rpg_class FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        String rpgClass = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                rpgClass = resultSet.getString("rpg_class");
            }
            resultSet.close();
            pst.close();
        }
        return rpgClass;
    }

    public static int getTotalExp(UUID uuid, int charNo) throws SQLException {
        String SQL_QUERY = "SELECT totalexp FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        int totalexp = -1;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                totalexp = resultSet.getInt("totalexp");
            }
            resultSet.close();
            pst.close();
        }
        return totalexp;
    }

    //SETTERS

    public static int setGuildOfPlayer(UUID uuid, String guild, PlayerRankInGuild rank) throws SQLException {
        String SQL_QUERY = "INSERT INTO goa_player_guild \n" +
                "\t(uuid, name, rank) \n" +
                "VALUES \n" +
                "\t(?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tname = VALUES(name),\n" +
                "\trank = VALUES(rank);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setString(2, guild);
            pst.setString(3, rank.name());

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static int setGuardianData(UUID uuid, LocalDate lastPrizeDate, StaffRank staffRank, PremiumRank premiumRank,
                                      ItemStack[] personalStorage, ItemStack[] bazaarStorage,
                                      ItemStack[] premiumStorage, String language, String friendUUIDS,
                                      String chatChannels, String cosmetics) throws SQLException {
        if (friendUUIDS.equals("")) {
            friendUUIDS = null;
        }

        String SQL_QUERY = "INSERT INTO goa_player \n" +
                "\t(uuid, daily_last_date, staff_rank, premium_rank, storage_personal, storage_bazaar, storage_premium, " +
                "lang, friend_uuids, chat_channels, cosmetics) \n" +
                "VALUES \n" +
                "\t(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tdaily_last_date = VALUES(daily_last_date),\n" +
                "\tstaff_rank = VALUES(staff_rank),\n" +
                "\tpremium_rank = VALUES(premium_rank),\n" +
                "\tstorage_personal = VALUES(storage_personal),\n" +
                "\tstorage_bazaar = VALUES(storage_bazaar),\n" +
                "\tstorage_premium = VALUES(storage_premium),\n" +
                "\tlang = VALUES(lang),\n" +
                "\tfriend_uuids = VALUES(friend_uuids),\n" +
                "\tchat_channels = VALUES(chat_channels),\n" +
                "\tcosmetics = VALUES(cosmetics);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setObject(2, lastPrizeDate);
            pst.setString(3, staffRank != null ? staffRank.name() : null);
            pst.setString(4, premiumRank != null ? premiumRank.name() : null);
            String personalStorageString = ItemSerializer.itemStackArrayToBase64(personalStorage);
            pst.setString(5, personalStorageString);
            String bazaarStorageString = ItemSerializer.itemStackArrayToBase64(bazaarStorage);
            pst.setString(6, bazaarStorageString);
            String premiumStorageString = ItemSerializer.itemStackArrayToBase64(premiumStorage);
            pst.setString(7, premiumStorageString);
            pst.setString(8, language);
            pst.setString(9, friendUUIDS);
            pst.setString(10, chatChannels);
            pst.setString(11, cosmetics);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static int setPremiumStorage(UUID uuid, ItemStack[] premiumStorage) throws SQLException {
        String SQL_QUERY = "INSERT INTO goa_player \n" +
                "\t(uuid, storage_premium) \n" +
                "VALUES \n" +
                "\t(?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tstorage_premium = VALUES(storage_premium);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            String premiumStorageString = ItemSerializer.itemStackArrayToBase64(premiumStorage);
            pst.setString(2, premiumStorageString);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static int setPremiumRankWithDate(UUID uuid, PremiumRank premiumRank) throws SQLException {
        LocalDate now = LocalDate.now();
        String currentDateString = now.toString();

        String SQL_QUERY = "INSERT INTO goa_player \n" +
                "\t(uuid, premium_rank, premium_rank_date) \n" +
                "VALUES \n" +
                "\t(?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tpremium_rank = VALUES(premium_rank),\n" +
                "\tpremium_rank_date = VALUES(premium_rank_date);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setString(2, premiumRank.name());
            pst.setString(3, currentDateString);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static PremiumRank getPremiumRank(UUID uuid) throws SQLException {
        String SQL_QUERY = "SELECT premium_rank FROM goa_player WHERE uuid = ?";
        PremiumRank premiumRank = null;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                String premiumRankString = resultSet.getString("premium_rank");
                if (!resultSet.wasNull()) {
                    premiumRank = PremiumRank.valueOf(premiumRankString);
                }
            }
            resultSet.close();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return premiumRank;
    }

    public static int clearExpiredPremiumRanks() throws SQLException {
        String SQL_QUERY = ("UPDATE goa_player SET premium_rank=NULL,premium_rank_date=NULL WHERE premium_rank_date < DATE_SUB(NOW(), INTERVAL 1 MONTH) OR premium_rank_date IS NULL");

        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static boolean setMembersOfGuild(String guild, HashMap<UUID, PlayerRankInGuild> playerPlayerRankInGuildHashMap) {
        try (Connection connection = ConnectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            statement.addBatch("DELETE FROM goa_player_guild WHERE name = '" + guild + "'");

            for (UUID player : playerPlayerRankInGuildHashMap.keySet()) {
                statement.addBatch("INSERT INTO goa_player_guild \n" +
                        "\t(uuid, name, rank) \n" +
                        "VALUES \n" +
                        "\t('" + player.toString() + "', '" + guild + "', '" + playerPlayerRankInGuildHashMap.get(player).name() + "')");
            }

            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int setGuild(Guild guild) throws SQLException {
        String SQL_QUERY = "INSERT INTO goa_guild \n" +
                "\t(name, tag, war_point, announcement, hall_level, bank_level, lab_level, storage) \n" +
                "VALUES \n" +
                "\t(?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tname = VALUES(name),\n" +
                "\ttag = VALUES(tag),\n" +
                "\twar_point = VALUES(war_point),\n" +
                "\tannouncement = VALUES(announcement),\n" +
                "\thall_level = VALUES(hall_level),\n" +
                "\tbank_level = VALUES(bank_level),\n" +
                "\tlab_level = VALUES(lab_level),\n" +
                "\tstorage = VALUES(storage);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, guild.getName());
            pst.setString(2, guild.getTag());
            pst.setInt(3, guild.getWarPoints());
            pst.setString(4, guild.getAnnouncement());
            pst.setInt(5, guild.getHallLevel());
            pst.setInt(6, guild.getBankLevel());
            pst.setInt(7, guild.getLabLevel());
            String moddedStacksData = ItemSerializer.itemStackArrayToBase64(guild.getGuildStorage());
            pst.setString(8, moddedStacksData);

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static int setCharacter(UUID uuid, int charNo, RPGCharacter rpgCharacter, ItemStack[] inventory, Location location, ItemStack[] armorContent, ItemStack offHand) throws SQLException {
        String SQL_QUERY = "INSERT INTO goa_player_character \n" +
                "\t(uuid, character_no, off_hand, slot_parrot, slot_necklace, slot_ring, slot_earring, slot_glove, " +
                "slot_pet, chat_tag, crafting_experiences, inventory, activequests, turnedinquests, location, armor_content, " +
                "rpg_class, totalexp, slot_tool_axe, slot_tool_bottle, slot_tool_hoe, slot_tool_pickaxe) \n" +
                "VALUES \n" +
                "\t(?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tcharacter_no = VALUES(character_no),\n" +
                "\toff_hand = VALUES(off_hand),\n" +
                "\tslot_parrot = VALUES(slot_parrot),\n" +
                "\tslot_necklace = VALUES(slot_necklace),\n" +
                "\tslot_ring = VALUES(slot_ring),\n" +
                "\tslot_earring = VALUES(slot_earring),\n" +
                "\tslot_glove = VALUES(slot_glove),\n" +
                "\tslot_pet = VALUES(slot_pet),\n" +
                "\tchat_tag = VALUES(chat_tag),\n" +
                "\tcrafting_experiences = VALUES(crafting_experiences),\n" +
                "\tinventory = VALUES(inventory),\n" +
                "\tactivequests = VALUES(activequests),\n" +
                "\tturnedinquests = VALUES(turnedinquests),\n" +
                "\tlocation = VALUES(location),\n" +
                "\tarmor_content = VALUES(armor_content),\n" +
                "\trpg_class = VALUES(rpg_class),\n" +
                "\ttotalexp = VALUES(totalexp),\n" +
                "\tslot_tool_axe = VALUES(slot_tool_axe),\n" +
                "\tslot_tool_bottle = VALUES(slot_tool_bottle),\n" +
                "\tslot_tool_hoe = VALUES(slot_tool_hoe),\n" +
                "\tslot_tool_pickaxe = VALUES(slot_tool_pickaxe);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);
            if (offHand != null) {
                String itemString = ItemSerializer.itemStackToBase64(offHand);
                pst.setString(3, itemString);
            } else {
                pst.setNull(3, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getParrotSlot().isEmpty()) {
                String itemString = ItemSerializer.itemStackToBase64(rpgCharacter.getRpgInventory().getParrotSlot().getItemOnSlot());
                pst.setString(4, itemString);
            } else {
                pst.setNull(4, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getNecklaceSlot().isEmpty()) {
                String itemString = ItemSerializer.itemStackToBase64(rpgCharacter.getRpgInventory().getNecklaceSlot().getItemOnSlot());
                pst.setString(5, itemString);
            } else {
                pst.setNull(5, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getRingSlot().isEmpty()) {
                String itemString = ItemSerializer.itemStackToBase64(rpgCharacter.getRpgInventory().getRingSlot().getItemOnSlot());
                pst.setString(6, itemString);
            } else {
                pst.setNull(6, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getEarringSlot().isEmpty()) {
                String itemString = ItemSerializer.itemStackToBase64(rpgCharacter.getRpgInventory().getEarringSlot().getItemOnSlot());
                pst.setString(7, itemString);
            } else {
                pst.setNull(7, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getGloveSlot().isEmpty()) {
                String itemString = ItemSerializer.itemStackToBase64(rpgCharacter.getRpgInventory().getGloveSlot().getItemOnSlot());
                pst.setString(8, itemString);
            } else {
                pst.setNull(8, Types.BLOB);
            }
            if (!rpgCharacter.getRpgInventory().getEggSlot().isEmpty()) {
                String itemString = ItemSerializer.itemStackToBase64(rpgCharacter.getRpgInventory().getEggSlot().getItemOnSlot());
                pst.setString(9, itemString);
            } else {
                pst.setNull(9, Types.BLOB);
            }
            pst.setString(10, rpgCharacter.getChatTag().name());

            RPGCharacterCraftingStats craftingStats = rpgCharacter.getCraftingStats();
            pst.setString(11, craftingStats.getDatabaseString());

            if (inventory.length > 0) {
                String moddedStacksData = ItemSerializer.itemStackArrayToBase64(inventory);
                pst.setString(12, moddedStacksData);
            } else {
                pst.setNull(12, Types.BLOB);
            }
            if (!rpgCharacter.getQuestList().isEmpty()) {
                List<Quest> questList = rpgCharacter.getQuestList();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < questList.size(); i++) {
                    if (i > 0) {
                        stringBuilder.append(";");
                    }
                    Quest quest = questList.get(i);
                    stringBuilder.append(quest.getQuestID());
                    stringBuilder.append("-");
                    List<Task> tasks = quest.getTasks();
                    for (int y = 0; y < tasks.size(); y++) {
                        if (y > 0) {
                            stringBuilder.append("-");
                        }
                        stringBuilder.append(tasks.get(y).getProgress());
                    }
                }
                String string = stringBuilder.toString();
                pst.setString(13, string);
            } else {
                pst.setNull(13, Types.BLOB);
            }
            if (!rpgCharacter.getTurnedInQuests().isEmpty()) {
                List<Integer> turnedInQuestList = rpgCharacter.getTurnedInQuests();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < turnedInQuestList.size(); i++) {
                    if (i > 0) {
                        stringBuilder.append(";");
                    }
                    int questNo = turnedInQuestList.get(i);
                    stringBuilder.append(questNo);
                }
                String string = stringBuilder.toString();
                pst.setString(14, string);
            } else {
                pst.setNull(14, Types.BLOB);
            }
            if (location != null) {
                String locationString = location.getWorld().getName() +
                        ";" +
                        (int) (location.getX() + 0.5) +
                        ";" +
                        (int) (location.getY() + 0.5) +
                        ";" +
                        (int) (location.getZ() + 0.5);
                pst.setString(15, locationString);
            } else {
                pst.setNull(15, Types.BLOB);
            }
            if (armorContent.length > 0) {
                String moddedStacksData = ItemSerializer.itemStackArrayToBase64(armorContent);
                pst.setString(16, moddedStacksData);
            } else {
                pst.setNull(16, Types.BLOB);
            }

            String rpgClassStr = rpgCharacter.getRpgClassStr();
            pst.setString(17, rpgClassStr);

            RPGCharacterStats rpgCharacterStats = rpgCharacter.getRpgCharacterStats();

            int totalExp = rpgCharacterStats.getTotalExp();
            pst.setInt(18, totalExp);

            if (!rpgCharacter.getRpgInventory().getToolSlot(GatheringToolType.AXE).isEmpty()) {
                String itemString = ItemSerializer.itemStackToBase64(rpgCharacter.getRpgInventory().getToolSlot(GatheringToolType.AXE).getItemOnSlot());
                pst.setString(19, itemString);
            } else {
                pst.setNull(19, Types.BLOB);
            }

            if (!rpgCharacter.getRpgInventory().getToolSlot(GatheringToolType.BOTTLE).isEmpty()) {
                String itemString = ItemSerializer.itemStackToBase64(rpgCharacter.getRpgInventory().getToolSlot(GatheringToolType.BOTTLE).getItemOnSlot());
                pst.setString(20, itemString);
            } else {
                pst.setNull(20, Types.BLOB);
            }

            if (!rpgCharacter.getRpgInventory().getToolSlot(GatheringToolType.HOE).isEmpty()) {
                String itemString = ItemSerializer.itemStackToBase64(rpgCharacter.getRpgInventory().getToolSlot(GatheringToolType.HOE).getItemOnSlot());
                pst.setString(21, itemString);
            } else {
                pst.setNull(21, Types.BLOB);
            }

            if (!rpgCharacter.getRpgInventory().getToolSlot(GatheringToolType.PICKAXE).isEmpty()) {
                String itemString = ItemSerializer.itemStackToBase64(rpgCharacter.getRpgInventory().getToolSlot(GatheringToolType.PICKAXE).getItemOnSlot());
                pst.setString(22, itemString);
            } else {
                pst.setNull(22, Types.BLOB);
            }

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    public static int setCharacterClassStats(UUID uuid, int charNo, String className, RPGClassStats rpgClassStats) throws SQLException {
        SkillRPGClassData skillRPGClassData = rpgClassStats.getSkillRPGClassData();
        SkillTreeData skillTreeData = skillRPGClassData.getSkillTreeData();
        SkillBarData skillBarData = skillRPGClassData.getSkillBarData();

        String SQL_QUERY = "INSERT INTO goa_player_character_class \n" +
                "\t(class_name, uuid, character_no, skill_points, attribute_points, skill_bar) \n" +
                "VALUES \n" +
                "\t(?, ?, ?, ?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "\tclass_name = VALUES(class_name),\n" +
                "\tuuid = VALUES(uuid),\n" +
                "\tcharacter_no = VALUES(character_no),\n" +
                "\tskill_points = VALUES(skill_points),\n" +
                "\tattribute_points = VALUES(attribute_points),\n" +
                "\tskill_bar = VALUES(skill_bar);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, className);
            pst.setString(2, uuid.toString());
            pst.setInt(3, charNo);

            Set<Integer> skillIds = skillTreeData.getSkillIds();
            if (skillIds.isEmpty()) {
                pst.setString(4, null);
            } else {
                StringBuilder skill_points = new StringBuilder();
                for (int skillId : skillIds) {
                    int investedSkillPoints = skillTreeData.getInvestedSkillPoints(skillId);
                    skill_points.append(skillId).append(",").append(investedSkillPoints).append(";");
                }
                pst.setString(4, skill_points.toString());
            }

            StringBuilder attribute_points = new StringBuilder();
            for (AttributeType attributeType : AttributeType.values()) {
                int invested = rpgClassStats.getInvested(attributeType);
                attribute_points.append(attributeType.name()).append(",").append(invested).append(";");
            }
            pst.setString(5, attribute_points.toString());

            StringBuilder skill_bar = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                int skillId = skillBarData.getSkillId(i);
                skill_bar.append(skillId).append(";");
            }
            pst.setString(6, skill_bar.toString());

            //2 = replaced, 1 = new row added
            int returnValue = pst.executeUpdate();

            pst.close();
            return returnValue;
        }
    }

    //CLEANERS

    public static void clearGuild(String guild) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_guild WHERE name = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, guild);
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void clearMembersOfGuild(String guild) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_player_guild WHERE name = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, guild);
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void clearGuildOfPlayer(UUID uuid) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_player_guild WHERE uuid = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, uuid.toString());
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void clearPlayerRanksAndStorages(UUID uuid) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_player WHERE uuid = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, uuid.toString());
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void clearFriendsOfPlayer(UUID uuid) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_player_friend WHERE uuid = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, uuid.toString());
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void clearCharacter(UUID uuid, int charNo) throws SQLException {
        String SQL_QUERY = "DELETE FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);
            pst.executeUpdate();
            pst.close();
        }
    }

    //BOOLEAN

    public static boolean characterExists(UUID uuid, int charNo) {
        boolean charExists = false;
        String SQL_QUERY = "SELECT inventory FROM goa_player_character WHERE uuid = ? AND character_no = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());
            pst.setInt(2, charNo);

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                //row exists
                charExists = true;
            }
            resultSet.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return charExists;
    }

    public static boolean uuidExists(UUID uuid) {
        boolean exists = false;
        String SQL_QUERY = "SELECT uuid FROM goa_player WHERE uuid = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);

            pst.setString(1, uuid.toString());

            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                //row exists
                exists = true;
            }
            resultSet.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    private static Guild guildFromResultSet(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        if (resultSet.wasNull()) {
            //if NOT NULL
            return null;
        }

        String tag = resultSet.getString("tag");
        if (resultSet.wasNull()) {
            //if NOT NULL
            return null;
        }

        Guild guild = new Guild(name, tag);

        String announcement = resultSet.getString("announcement");
        if (!resultSet.wasNull()) {
            //if NOT NULL
            guild.setAnnouncement(announcement);
        }

        int currentValue = resultSet.getInt("war_point");
        if (!resultSet.wasNull()) {
            //if NOT NULL
            guild.setWarPoints(currentValue);
        }

        currentValue = resultSet.getInt("hall_level");
        if (!resultSet.wasNull()) {
            //if NOT NULL
            guild.setHallLevel(currentValue);
        }

        currentValue = resultSet.getInt("bank_level");
        if (!resultSet.wasNull()) {
            //if NOT NULL
            guild.setBankLevel(currentValue);
        }

        currentValue = resultSet.getInt("lab_level");
        if (!resultSet.wasNull()) {
            //if NOT NULL
            guild.setLabLevel(currentValue);
        }

        String storageString = resultSet.getString("storage");
        if (!resultSet.wasNull()) {
            //if NOT NULL
            ItemStack[] itemStacks = new ItemStack[0];
            try {
                itemStacks = ItemSerializer.itemStackArrayFromBase64(storageString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            guild.setGuildStorage(itemStacks);
        }

        return guild;
    }

    public static List<Guild> getTop10Guilds() {
        String SQL_QUERY = "SELECT * FROM goa_guild ORDER BY war_point LIMIT 10";
        List<Guild> guilds = new ArrayList<>();

        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                Guild guild = guildFromResultSet(resultSet);
                if (guild != null) {
                    guilds.add(guild);
                }
            }
            resultSet.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return guilds;
    }
}
