package eu.pixelgamesmc.minecraft.lobby.player

import org.bukkit.entity.Player
import java.util.UUID

private val players: MutableList<UUID> = mutableListOf()

fun Player.setBuilding(value: Boolean) {
    players.remove(uniqueId)
    if (value) {
        players.add(uniqueId)
    }
}

fun Player.isBuilding(): Boolean {
    return players.contains(uniqueId)
}