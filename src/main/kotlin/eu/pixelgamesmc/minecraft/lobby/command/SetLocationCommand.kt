package eu.pixelgamesmc.minecraft.lobby.command

import eu.pixelgamesmc.minecraft.lobby.configuration.LocationsConfiguration
import eu.pixelgamesmc.minecraft.servercore.command.PixelCommand
import eu.pixelgamesmc.minecraft.servercore.utility.CommandSenderUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class SetLocationCommand(private val plugin: Plugin): PixelCommand("setlocation", true) {

    override fun performCommand(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        val player = sender as Player
        val location = player.location

        if (args.size != 1) {
            CommandSenderUtil.sendMessage(player, CommandSenderUtil.getComponent(sender, "lobby", "prefix"), "lobby", "wrong_arguments")
            return false
        }
        val locationType = try {
            LocationType.valueOf(args[0].uppercase())
        } catch (e: IllegalArgumentException) {
            CommandSenderUtil.sendMessage(player, CommandSenderUtil.getComponent(sender, "lobby", "prefix"), "lobby", "wrong_location_type")
            return false
        }

        val locationsConfiguration = LocationsConfiguration.getConfiguration(plugin)
        when (locationType) {
            LocationType.MARKET_PLACE -> locationsConfiguration.marketPlaceLocation = location
            LocationType.SPAWN -> locationsConfiguration.spawnLocation = location
            LocationType.DAILY_REWARDS -> locationsConfiguration.dailyBonusLocation = location
            LocationType.CITY_BUILD -> locationsConfiguration.cityBuildLocation = location
            LocationType.LUCKY_WALLS -> locationsConfiguration.luckyWallsLocation = location
        }
        locationsConfiguration.saveConfiguration(plugin)
        CommandSenderUtil.sendMessage(player, CommandSenderUtil.getComponent(player, "lobby", "prefix"), "lobby", "set_location", "{location}" to locationType)
        return true
    }

    override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): List<String> {
        if (args.size == 1) {
            return LocationType.values().map { it.name }.filter { it.startsWith(args[0].uppercase()) }
        }

        return listOf()
    }

    enum class LocationType {
        MARKET_PLACE, SPAWN, DAILY_REWARDS, CITY_BUILD, LUCKY_WALLS
    }
}