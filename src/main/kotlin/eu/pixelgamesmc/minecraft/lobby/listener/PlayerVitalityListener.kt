package eu.pixelgamesmc.minecraft.lobby.listener

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerJoinEvent

class PlayerVitalityListener: Listener {

    @EventHandler
    fun playerJoin(event: PlayerJoinEvent) {
        val player = event.player

        player.gameMode = GameMode.SURVIVAL

        player.foodLevel = 20
        player.health = 20.0
    }

    @EventHandler
    fun foodLevel(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun entityDamage(event: EntityDamageEvent) {
        event.isCancelled = true
    }
}