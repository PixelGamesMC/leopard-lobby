package eu.pixelgamesmc.minecraft.lobby.player

import eu.pixelgamesmc.minecraft.servercore.utility.CommandSenderUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.UUID

enum class ViewerRule {
    ALL, VIP, NONE;

    fun toItemStack(player: Player): ItemStack {
        if (this == ALL) {
            val playerHiderAll = ItemStack(Material.LIME_DYE)
            playerHiderAll.editMeta { meta ->
                meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "player_hider_all_display"))
            }
            return playerHiderAll
        } else if (this == VIP) {
            val playerHiderVIP = ItemStack(Material.PURPLE_DYE)
            playerHiderVIP.editMeta { meta ->
                meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "player_hider_vip_display"))
            }
            return playerHiderVIP
        } else {
            val playerHiderNone = ItemStack(Material.GRAY_DYE)
            playerHiderNone.editMeta { meta ->
                meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "player_hider_none_display"))
            }
            return playerHiderNone
        }
    }
}

private val players = mutableMapOf<UUID, ViewerRule>()

fun Player.toggleViewerRule(): ViewerRule {
    val newRule = when (getViewerRule()) {
        ViewerRule.ALL -> {
            ViewerRule.VIP
        }
        ViewerRule.VIP -> {
            ViewerRule.NONE
        }
        else -> {
            ViewerRule.ALL
        }
    }

    return newRule.also { rule ->
        players[uniqueId] = rule
    }
}

fun Player.getViewerRule(): ViewerRule {
    return players[uniqueId] ?: ViewerRule.ALL
}