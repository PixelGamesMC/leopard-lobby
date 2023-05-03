package eu.pixelgamesmc.minecraft.lobby.inventory

import eu.pixelgamesmc.minecraft.lobby.configuration.LocationsConfiguration
import eu.pixelgamesmc.minecraft.servercore.inventory.ClickablePlayerInventory
import eu.pixelgamesmc.minecraft.servercore.utility.CommandSenderUtil
import eu.thesimplecloud.api.CloudAPI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.Plugin
import java.net.URL
import java.util.*

class NavigatorInventory(private val plugin: Plugin, player: Player): ClickablePlayerInventory(player, 9*5, CommandSenderUtil.getComponent(player, "lobby", "navigator_inventory_title")) {

    private val marketPlaceSlot = 11
    private val spawnSlot = 13
    private val dailyBonusSlot = 15
    private val cityBuildSlot = 30
    private val itemFiestaSlot = 31
    private val luckyWallsSlot = 32

    init {
        val marketPlace = ItemStack(Material.NETHER_STAR)
        marketPlace.editMeta { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "market_place_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "market_place_lore"))
        }
        setItem(marketPlaceSlot, marketPlace)

        val spawn = ItemStack(Material.SLIME_BALL)
        spawn.editMeta { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "spawn_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "spawn_lore"))
        }
        setItem(spawnSlot, spawn)

        val dailyBonus = ItemStack(Material.SUNFLOWER)
        dailyBonus.editMeta { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "daily_bonus_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "daily_bonus_lore"))
        }
        setItem(dailyBonusSlot, dailyBonus)

        val serviceManager = CloudAPI.instance.getCloudServiceManager()

        val cityBuildServices = serviceManager.getCloudServicesByGroupName("CityBuild")
        val playerCountCityBuild = cityBuildServices.sumOf { it.getOnlineCount() }

        val cityBuild = ItemStack(Material.PLAYER_HEAD)
        cityBuild.editMeta(SkullMeta::class.java) { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "city_build_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "city_build_lore",
                "{player_count}" to playerCountCityBuild,
                "{connection_status}" to if (cityBuildServices.isEmpty()) "&cOFFLINE" else "&aONLINE"))
            meta.playerProfile = Bukkit.createProfile(UUID.randomUUID()).apply {
                textures.apply {
                    skin = URL("https://textures.minecraft.net/texture/219e36a87baf0ac76314352f59a7f63bdb3f4c86bd9bba6927772c01d4d1")
                    setTextures(this)
                }
            }
        }
        setItem(cityBuildSlot, cityBuild)

        val itemFiestaService = serviceManager.getCloudServicesByGroupName("ItemFiesta")
        val playerCountItemFiesta = itemFiestaService.sumOf { it.getOnlineCount() }

        val itemFiesta = ItemStack(Material.PLAYER_HEAD)
        itemFiesta.editMeta(SkullMeta::class.java) { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "item_fiesta_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "item_fiesta_lore",
                "{player_count}" to playerCountItemFiesta,
                "{connection_status}" to if (itemFiestaService.isEmpty()) "&cOFFLINE" else "&aONLINE"))
            meta.playerProfile = Bukkit.createProfile(UUID.randomUUID()).apply {
                textures.apply {
                    skin = URL("https://textures.minecraft.net/texture/63f4d555fb3b9357af7582f273800f1da4ac69b04210c3aa34e63db2a4235bbf")
                    setTextures(this)
                }
            }
        }
        setItem(itemFiestaSlot, itemFiesta)

        val luckyWallsServices = serviceManager.getCloudServicesByGroupName("LuckyWalls")
        val playerCountLuckyWalls = serviceManager.getAllCachedObjects().filter { it.getGroupName().startsWith("LuckyWalls") }.sortedBy { it.getServiceNumber() }.sumOf { it.getOnlineCount() }

        val luckyWalls = ItemStack(Material.PLAYER_HEAD)
        luckyWalls.editMeta(SkullMeta::class.java) { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "lucky_walls_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "lucky_walls_lore",
                "{player_count}" to playerCountLuckyWalls,
                "{connection_status}" to if (luckyWallsServices.isEmpty()) "&cOFFLINE" else "&aONLINE"))
            meta.playerProfile = Bukkit.createProfile(UUID.randomUUID()).apply {
                textures.apply {
                    skin = URL("https://textures.minecraft.net/texture/ac4970ea91ab06ece59d45fce7604d255431f2e03a737b226082c4cce1aca1c4")
                    setTextures(this)
                }
            }
        }
        setItem(luckyWallsSlot, luckyWalls)
    }

    override fun playerClick(
        slot: Int,
        clickType: ClickType,
        action: InventoryAction,
        isShiftClick: Boolean,
        isRightClick: Boolean,
        isLeftClick: Boolean,
        currentItem: ItemStack?
    ): Boolean {
        val locationsConfiguration = LocationsConfiguration.getConfiguration(plugin)

        val serviceManager = CloudAPI.instance.getCloudServiceManager()
        val cloudPlayerManager = CloudAPI.instance.getCloudPlayerManager()

        when (slot) {
            marketPlaceSlot -> player.teleport(locationsConfiguration.marketPlaceLocation)
            spawnSlot -> player.teleport(locationsConfiguration.spawnLocation)
            dailyBonusSlot -> player.teleport(locationsConfiguration.dailyBonusLocation)
            cityBuildSlot, luckyWallsSlot, itemFiestaSlot -> {
                val cloudPlayerPromise = cloudPlayerManager.getCloudPlayer(player.uniqueId)

                val cloudService = when (slot) {
                    cityBuildSlot -> {
                        val cityBuildServices = serviceManager.getCloudServicesByGroupName("CityBuild")
                        cityBuildServices.randomOrNull()
                    }
                    luckyWallsSlot -> {
                        val luckyWallsServices = serviceManager.getCloudServicesByGroupName("LuckyWalls")
                        luckyWallsServices.randomOrNull()
                    }
                    itemFiestaSlot -> {
                        val itemFiestaService = serviceManager.getCloudServicesByGroupName("ItemFiesta")
                        itemFiestaService.randomOrNull()
                    }

                    else -> {
                        null
                    }
                }

                if (cloudService != null) {
                    val cloudPlayer = cloudPlayerPromise.sync().get()
                    cloudPlayer.connect(cloudService)
                }
            }
        }
        return true
    }
}