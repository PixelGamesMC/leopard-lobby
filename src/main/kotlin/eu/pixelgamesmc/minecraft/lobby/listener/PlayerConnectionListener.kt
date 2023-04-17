package eu.pixelgamesmc.minecraft.lobby.listener

import eu.pixelgamesmc.minecraft.lobby.configuration.LocationsConfiguration
import eu.pixelgamesmc.minecraft.lobby.sidebar.LobbySidebar
import eu.pixelgamesmc.minecraft.servercore.utility.PlayerUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

class PlayerConnectionListener(private val plugin: Plugin): Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun playerJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.inventory.clear()

        val locationsConfiguration = LocationsConfiguration.getConfiguration(plugin)

        player.teleport(locationsConfiguration.spawnLocation)
        val lobbySidebar = LobbySidebar(player)
        lobbySidebar.updateDisplay()
        PlayerUtil.setSidebar(player, lobbySidebar)

        event.joinMessage(null)
    }

    @EventHandler
    fun playerQuit(event: PlayerQuitEvent) {
        event.quitMessage(null)
    }
}