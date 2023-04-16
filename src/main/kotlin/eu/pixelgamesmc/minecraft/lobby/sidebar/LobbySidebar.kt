package eu.pixelgamesmc.minecraft.lobby.sidebar

import de.dytanic.cloudnet.wrapper.Wrapper
import eu.pixelgamesmc.minecraft.servercore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.database.collection.currency.CurrencyCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.permission.group.PermissionGroupCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.permission.user.PermissionUserCollection
import eu.pixelgamesmc.minecraft.servercore.scoreboard.Sidebar
import eu.pixelgamesmc.minecraft.servercore.utility.CommandSenderUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player

class LobbySidebar(private val player: Player): Sidebar("lobby_${player.name}", player, CommandSenderUtil.getComponent(player, "lobby", "scoreboard_title")) {

    init {
        val permissionUserCollection = PixelDatabase.getCollection(PermissionUserCollection::class)
        val permissionUser = permissionUserCollection.getUser(player.uniqueId)
        val permissionGroupCollection = PixelDatabase.getCollection(PermissionGroupCollection::class)
        val currencyCollection = PixelDatabase.getCollection(CurrencyCollection::class)
        val currencyUser = currencyCollection.getUser(player.uniqueId)
        if (permissionUser != null && currencyUser != null) {
            val permissionGroup = permissionUser.permissionGroups.mapNotNull { permissionGroupCollection.getGroup(it) }.minByOrNull { it.weight }
                ?: permissionGroupCollection.getDefaultGroup()

            val legacySection = LegacyComponentSerializer.legacySection()
            updateScore(7, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_rank_title")))
            updateScore(6, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_rank_value",
                "{rank}" to LegacyComponentSerializer.legacyAmpersand().serialize(Component.text(permissionGroup.name, permissionGroup.color)))))
            updateScore(5, "§a")
            updateScore(4, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_currency_title")))
            updateScore(3, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_currency_value",
                "{currency}" to currencyUser.value)))
            updateScore(2, "§c")
            updateScore(1, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_server_title")))
            updateScore(0, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_server_value",
                "{server}" to Wrapper.getInstance().serviceId.name)))
        }
    }
}