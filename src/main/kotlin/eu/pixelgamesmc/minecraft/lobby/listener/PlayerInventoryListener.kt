package eu.pixelgamesmc.minecraft.lobby.listener

import eu.pixelgamesmc.minecraft.lobby.inventory.LobbySwitcherInventory
import eu.pixelgamesmc.minecraft.lobby.inventory.NavigatorInventory
import eu.pixelgamesmc.minecraft.lobby.player.ViewerRule
import eu.pixelgamesmc.minecraft.lobby.player.getViewerRule
import eu.pixelgamesmc.minecraft.lobby.player.toggleViewerRule
import eu.pixelgamesmc.minecraft.servercore.component.ComponentProvider
import eu.pixelgamesmc.minecraft.servercore.utility.CommandSenderUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.Plugin
import java.util.UUID
import java.util.concurrent.TimeUnit

class PlayerInventoryListener(private val plugin: Plugin): Listener {

    private val navigatorSlot = 0
    private val playerHiderSlot = 1
    private val gadgetsSlot = 4
    private val profileSlot = 7
    private val lobbySwitcherSlot = 8

    @EventHandler(priority = EventPriority.HIGHEST)
    fun playerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val inventory = player.inventory

        val navigator = ItemStack(Material.COMPASS)
        navigator.editMeta { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "navigator_display"))
        }
        inventory.setItem(navigatorSlot, navigator)

        inventory.setItem(playerHiderSlot, player.getViewerRule().toItemStack(player))

        val gadgets = ItemStack(Material.CHEST)
        gadgets.editMeta { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "gadgets_display"))
        }
        inventory.setItem(gadgetsSlot, gadgets)

        val profile = ItemStack(Material.PLAYER_HEAD)
        profile.editMeta(SkullMeta::class.java) { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "profile_display"))

            meta.playerProfile = player.playerProfile
        }
        inventory.setItem(profileSlot, profile)

        val lobbySwitcher = ItemStack(Material.NETHER_STAR)
        lobbySwitcher.editMeta { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "lobby_switcher_display"))
        }
        inventory.setItem(lobbySwitcherSlot, lobbySwitcher)
    }

    private val cooldown: MutableMap<UUID, Long> = mutableMapOf()

    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        if (event.hasItem() && (event.action.isRightClick)) {
            val item = event.item
            val player = event.player

            val inventory = player.inventory

            val slot = inventory.indexOf(item)
            if (!(slot == navigatorSlot || slot == playerHiderSlot || slot == gadgetsSlot || slot == profileSlot || slot == lobbySwitcherSlot)) {
                return
            }

            val cooldown = cooldown[player.uniqueId]
            val currentTimeMillis = System.currentTimeMillis()
            if (cooldown != null) {
                if (cooldown > currentTimeMillis) {
                    return
                }
            }
            this.cooldown[player.uniqueId] = currentTimeMillis + 10

            when (slot) {
                navigatorSlot -> {
                    val navigatorInventory = NavigatorInventory(plugin, player)
                    navigatorInventory.openInventory(player)
                }

                playerHiderSlot -> {
                    val viewerRule = player.toggleViewerRule()

                    when (viewerRule) {
                        ViewerRule.ALL -> {
                            Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
                                if (player.uniqueId != onlinePlayer.uniqueId) {
                                    player.showPlayer(plugin, onlinePlayer)
                                }
                            }
                            CommandSenderUtil.sendMessage(player, ComponentProvider.getCoreComponent("lobby", "prefix"), "lobby", "player_hider_all_message")
                        }
                        ViewerRule.VIP -> {
                            Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
                                if (player.uniqueId != onlinePlayer.uniqueId) {
                                    if (!onlinePlayer.hasPermission("pixelgamesmc.vip")) {
                                        player.hidePlayer(plugin, onlinePlayer)
                                    } else {
                                        player.showPlayer(plugin, onlinePlayer)
                                    }
                                }
                            }
                            CommandSenderUtil.sendMessage(player, ComponentProvider.getCoreComponent("lobby", "prefix"), "lobby", "player_hider_vip_message")
                        }
                        ViewerRule.NONE -> {
                            Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
                                if (player.uniqueId != onlinePlayer.uniqueId) {
                                    player.hidePlayer(plugin, onlinePlayer)
                                }
                            }
                            CommandSenderUtil.sendMessage(player, ComponentProvider.getCoreComponent("lobby", "prefix"), "lobby", "player_hider_none_message")
                        }
                    }

                    inventory.setItem(playerHiderSlot, viewerRule.toItemStack(player))
                }

                gadgetsSlot -> {
                    CommandSenderUtil.sendMessage(player, ComponentProvider.getCoreComponent("lobby", "prefix"), "lobby", "not_implemented")
                }

                profileSlot -> {
                    CommandSenderUtil.sendMessage(player, ComponentProvider.getCoreComponent("lobby", "prefix"), "lobby", "not_implemented")
                }

                lobbySwitcherSlot -> {
                    val lobbySwitcherInventory = LobbySwitcherInventory(player)
                    lobbySwitcherInventory.openInventory(player)
                }
            }
            player.playSound(player, Sound.BLOCK_WOOD_BREAK, .25f, 1.0f)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun inventoryClick(event: InventoryClickEvent) {
        val entity = event.whoClicked
        val currentItem = event.currentItem

        if (currentItem != null) {
            if (event.inventory.holder == entity.inventory.holder) {
                val slot = event.slot
                if (slot == navigatorSlot || slot == playerHiderSlot || slot == gadgetsSlot || slot == profileSlot || slot == lobbySwitcherSlot) {
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun playerDrop(event: PlayerDropItemEvent) {
        event.isCancelled = true
    }
}