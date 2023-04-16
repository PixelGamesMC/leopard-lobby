package eu.pixelgamesmc.minecraft.lobby.configuration

import eu.pixelgamesmc.minecraft.lobby.utlity.codec.LocationSerializer
import eu.pixelgamesmc.minecraft.servercore.utility.PluginUtil
import kotlinx.serialization.Serializable
import org.bukkit.Location
import org.bukkit.plugin.Plugin

@Serializable
data class LocationsConfiguration(
    @Serializable(with = LocationSerializer::class) var marketPlaceLocation: Location,
    @Serializable(with = LocationSerializer::class) var spawnLocation: Location,
    @Serializable(with = LocationSerializer::class) var dailyBonusLocation: Location,
    @Serializable(with = LocationSerializer::class) var cityBuildLocation: Location,
    @Serializable(with = LocationSerializer::class) var luckyWallsLocation: Location,
) {
    companion object {

        private var instance: LocationsConfiguration? = null

        fun getConfiguration(plugin: Plugin): LocationsConfiguration {
            return instance ?: PluginUtil.loadConfiguration(plugin, "locations", LocationsConfiguration(
                Location(plugin.server.worlds.first(), 0.0, 0.0, 0.0, 0.0f, 0.0f),
                Location(plugin.server.worlds.first(), 0.0, 0.0, 0.0, 0.0f, 0.0f),
                Location(plugin.server.worlds.first(), 0.0, 0.0, 0.0, 0.0f, 0.0f),
                Location(plugin.server.worlds.first(), 0.0, 0.0, 0.0, 0.0f, 0.0f),
                Location(plugin.server.worlds.first(), 0.0, 0.0, 0.0, 0.0f, 0.0f)
            )).also {
                instance = it
            }
        }
    }

    fun saveConfiguration(plugin: Plugin) {
        PluginUtil.saveConfiguration(plugin, "locations", this)
    }
}
