package eu.pixelgamesmc.minecraft.lobby.listener

import eu.pixelgamesmc.minecraft.lobby.player.ViewerRule
import eu.pixelgamesmc.minecraft.lobby.player.getViewerRule
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

class PlayerVisibilityListener(private val plugin: Plugin): Listener {

    @EventHandler
    fun playerJoin(event: PlayerJoinEvent) {
        val player = event.player
        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            val viewerRule = onlinePlayer.getViewerRule()

            if ((viewerRule == ViewerRule.VIP && !player.hasPermission("pixelgamesmc.vip")) || viewerRule == ViewerRule.NONE) {
                onlinePlayer.hidePlayer(plugin, player)
            }
        }
    }
}