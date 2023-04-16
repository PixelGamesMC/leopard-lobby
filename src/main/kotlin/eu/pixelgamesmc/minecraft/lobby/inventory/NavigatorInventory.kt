package eu.pixelgamesmc.minecraft.lobby.inventory

import eu.pixelgamesmc.minecraft.lobby.entitiy.LocationsConfiguration
import eu.pixelgamesmc.minecraft.servercore.inventory.ClickablePlayerInventory
import eu.pixelgamesmc.minecraft.servercore.utility.CommandSenderUtil
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

class NavigatorInventory(private val plugin: Plugin, player: Player): ClickablePlayerInventory(player, 9*5, CommandSenderUtil.getComponent(player, "lobby", "navigator_title")) {

    private val marketPlaceSlot = 11
    private val spawnSlot = 13
    private val dailyBonusSlot = 15
    private val cityBuildSlot = 30
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

        val cityBuild = ItemStack(Material.PLAYER_HEAD)
        cityBuild.editMeta(SkullMeta::class.java) { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "city_build_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "city_build_lore"))
            meta.playerProfile = Bukkit.createProfile(UUID.randomUUID()).apply {
                val cache = textures
                cache.skin = URL("https://textures.minecraft.net/texture/219e36a87baf0ac76314352f59a7f63bdb3f4c86bd9bba6927772c01d4d1")
                setTextures(cache)
            }
        }
        setItem(cityBuildSlot, cityBuild)

        val luckyWalls = ItemStack(Material.PLAYER_HEAD)
        luckyWalls.editMeta(SkullMeta::class.java) { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "lucky_walls_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "lucky_walls_lore"))
            meta.playerProfile = Bukkit.createProfile(UUID.randomUUID()).apply {
                val cache = textures
                cache.skin = URL("https://textures.minecraft.net/texture/ac4970ea91ab06ece59d45fce7604d255431f2e03a737b226082c4cce1aca1c4")
                setTextures(cache)
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

        when (slot) {
            marketPlaceSlot -> player.teleport(locationsConfiguration.marketPlaceLocation)
            spawnSlot -> player.teleport(locationsConfiguration.spawnLocation)
            dailyBonusSlot -> player.teleport(locationsConfiguration.dailyBonusLocation)
            cityBuildSlot -> player.teleport(locationsConfiguration.cityBuildLocation)
            luckyWallsSlot -> player.teleport(locationsConfiguration.luckyWallsLocation)
        }
        return true
    }
}