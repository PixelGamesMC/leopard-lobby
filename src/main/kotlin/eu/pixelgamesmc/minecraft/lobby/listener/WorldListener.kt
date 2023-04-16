package eu.pixelgamesmc.minecraft.lobby.listener

import eu.pixelgamesmc.minecraft.lobby.configuration.LocationsConfiguration
import eu.pixelgamesmc.minecraft.lobby.player.isBuilding
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

class WorldListener(private val plugin: Plugin): Listener {

    @EventHandler
    fun playerJoin(event: PlayerJoinEvent) {
        val locationsConfiguration = LocationsConfiguration.getConfiguration(plugin)

        event.player.teleport(locationsConfiguration.spawnLocation)
    }

    @EventHandler
    fun blockPlace(event: BlockPlaceEvent) {
        if (!event.player.isBuilding()) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun blockBreak(event: BlockBreakEvent) {
        if (!event.player.isBuilding()) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun entitySpawn(event: EntitySpawnEvent) {
        event.isCancelled = true
    }
}