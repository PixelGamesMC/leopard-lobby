package eu.pixelgamesmc.minecraft.lobby.inventory

import eu.pixelgamesmc.minecraft.servercore.inventory.ClickablePlayerInventory
import eu.pixelgamesmc.minecraft.servercore.utility.CommandSenderUtil
import eu.thesimplecloud.api.CloudAPI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.inventory.ItemStack

class LobbySwitcherInventory(player: Player): ClickablePlayerInventory(player, 9*1, CommandSenderUtil.getComponent(player, "lobby", "lobby_switcher_inventory_title")) {

    init {
        val serviceManager = CloudAPI.instance.getCloudServiceManager()

        val lobbyServices = serviceManager.getCloudServicesByGroupName("Lobby")
        for (slot in 0 until 7) {
            if (slot > lobbyServices.size - 1) {
                break
            }
            val lobbyService = lobbyServices[slot]
            val lobby = ItemStack(Material.NETHER_STAR)
            lobby.editMeta { meta ->
                meta.displayName(CommandSenderUtil.getComponent(player, "lobby", "lobby_display", "{name}" to lobbyService.getName()))
                meta.lore(CommandSenderUtil.getComponents(player, "lobby", "lobby_lore",
                    "{name}" to lobbyService.getName(),
                    "{player_count}" to lobbyService.getOnlineCount(),
                    "{connection_status}" to if (!lobbyService.isOnline()) "&cOFFLINE" else "&aONLINE"))
            }
            setItem(slot+1, lobby)
        }
    }

    override fun playerClick(
        slot: Int,
        clickType: ClickType,
        action: InventoryAction,
        isShiftClick: Boolean,
        isRightClick: Boolean,
        isLeftClick: Boolean,
        currentItem: ItemStack?
    ): Boolean {
        val serviceManager = CloudAPI.instance.getCloudServiceManager()
        val cloudPlayerManager = CloudAPI.instance.getCloudPlayerManager()
        val lobbyServices = serviceManager.getCloudServicesByGroupName("Lobby")

        val index = slot - 1
        if (index < lobbyServices.size) {
            val lobbyService = lobbyServices[index]
            val cloudPlayerPromise = cloudPlayerManager.getCloudPlayer(player.uniqueId)
            val cloudPlayer = cloudPlayerPromise.sync().get()
            cloudPlayer.connect(lobbyService)
        }
        return true
    }
}