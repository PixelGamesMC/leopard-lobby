package eu.pixelgamesmc.minecraft.lobby.listener

import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.player.PlayerInteractEvent

class WorldListener: Listener {

    @EventHandler
    fun blockPlace(event: BlockPlaceEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun blockBreak(event: BlockBreakEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun entitySpawn(event: EntitySpawnEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        event.setUseInteractedBlock(Event.Result.DENY)
    }
}