package eu.pixelgamesmc.minecraft.lobby

import eu.pixelgamesmc.minecraft.lobby.entitiy.LocationsConfiguration
import eu.pixelgamesmc.minecraft.lobby.listener.PlayerInventoryListener
import eu.pixelgamesmc.minecraft.lobby.listener.PlayerVisibilityListener
import eu.pixelgamesmc.minecraft.lobby.listener.PlayerVitalityListener
import eu.pixelgamesmc.minecraft.lobby.listener.WorldListener
import eu.pixelgamesmc.minecraft.servercore.component.ComponentProvider
import eu.pixelgamesmc.minecraft.servercore.utility.PluginUtil
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class Lobby: JavaPlugin() {

    override fun onEnable() {
        ComponentProvider.loadComponents(this)

        val locationsConfiguration = PluginUtil.loadConfiguration(
            this,
            LocationsConfiguration(
                Location(server.worlds.first(), 0.0, 0.0, 0.0, 0.0f, 0.0f),
                Location(server.worlds.first(), 0.0, 0.0, 0.0, 0.0f, 0.0f),
                Location(server.worlds.first(), 0.0, 0.0, 0.0, 0.0f, 0.0f),
                Location(server.worlds.first(), 0.0, 0.0, 0.0, 0.0f, 0.0f),
                Location(server.worlds.first(), 0.0, 0.0, 0.0, 0.0f, 0.0f)
            )
        )

        PluginUtil.registerEvents(this, PlayerInventoryListener(this), PlayerVitalityListener(), PlayerVisibilityListener(this), WorldListener())
    }
}