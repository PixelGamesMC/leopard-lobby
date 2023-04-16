package eu.pixelgamesmc.minecraft.lobby.listener

import eu.pixelgamesmc.minecraft.lobby.inventory.NavigatorInventory
import eu.pixelgamesmc.minecraft.lobby.player.ViewerRule
import eu.pixelgamesmc.minecraft.lobby.player.getViewerRule
import eu.pixelgamesmc.minecraft.lobby.player.toggleViewerRule
import eu.pixelgamesmc.minecraft.servercore.component.ComponentProvider
import eu.pixelgamesmc.minecraft.servercore.utility.CommandSenderUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCreativeEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.Plugin

class PlayerInventoryListener(private val plugin: Plugin): Listener {

    private val navigatorSlot = 0
    private val playerHiderSlot = 1
    private val gadgetsSlot = 4
    private val profileSlot = 7
    private val lobbySwitcherSlot = 8

    @EventHandler
    fun playerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val inventory = player.inventory

        val navigator = ItemStack(Material.COMPASS)
        navigator.editMeta { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "navigator_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "navigator_lore"))
        }
        inventory.setItem(navigatorSlot, navigator)

        inventory.setItem(playerHiderSlot, player.getViewerRule().toItemStack(player))

        val gadgets = ItemStack(Material.CHEST)
        gadgets.editMeta { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "gadgets_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "gadgets_lore"))
        }
        inventory.setItem(gadgetsSlot, gadgets)

        val profile = ItemStack(Material.PLAYER_HEAD)
        profile.editMeta(SkullMeta::class.java) { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "profile_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "profile_lore"))

            meta.playerProfile = player.playerProfile
        }
        inventory.setItem(profileSlot, profile)

        val lobbySwitcher = ItemStack(Material.NETHER_STAR)
        lobbySwitcher.editMeta { meta ->
            meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "lobby_switcher_display"))
            meta.lore(CommandSenderUtil.getComponents(player, "lobby", "lobby_switcher_lore"))
        }
        inventory.setItem(lobbySwitcherSlot, lobbySwitcher)
    }

    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        if (event.hasItem() && (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) {
            val item = event.item
            val player = event.player
            val inventory = player.inventory

            when (inventory.indexOf(item)) {
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
                    CommandSenderUtil.sendMessage(player, ComponentProvider.getCoreComponent("lobby", "prefix"), "lobby", "not_implemented")
                }
            }
            player.playSound(player, Sound.BLOCK_WOOD_BREAK, .25f, 1.0f)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun inventoryClick(event: InventoryClickEvent) {
        if (event.inventory.holder == event.whoClicked.inventory.holder) {
            val slot = event.slot
            if (slot == navigatorSlot || slot == playerHiderSlot || slot == gadgetsSlot || slot == profileSlot || slot == lobbySwitcherSlot) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun inventoryCreative(event: InventoryCreativeEvent) {
        if (event.clickedInventory?.holder == event.whoClicked.inventory.holder) {
            val slot = event.slot
            if (slot == navigatorSlot || slot == playerHiderSlot || slot == gadgetsSlot || slot == profileSlot || slot == lobbySwitcherSlot) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun playerDrop(event: PlayerDropItemEvent) {
        val item = event.itemDrop.itemStack
        val player = event.player
        val inventory = player.inventory
        val index = inventory.indexOf(item)

        if (index == navigatorSlot || index == playerHiderSlot || index == gadgetsSlot || index == profileSlot || index == lobbySwitcherSlot) {
            event.isCancelled = true
        }
    }
}