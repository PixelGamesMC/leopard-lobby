package eu.pixelgamesmc.minecraft.lobby.sidebar

import eu.pixelgamesmc.minecraft.lobby.database.LobbyUserCollection
import eu.pixelgamesmc.minecraft.servercore.database.PixelDatabase
import eu.pixelgamesmc.minecraft.servercore.scoreboard.Sidebar
import eu.pixelgamesmc.minecraft.servercore.utility.CommandSenderUtil
import eu.thesimplecloud.api.CloudAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.luckperms.api.LuckPermsProvider
import org.bukkit.entity.Player

class LobbySidebar(player: Player): Sidebar("lobby_${player.name}", player, CommandSenderUtil.getComponent(player, "lobby", "scoreboard_title")) {

    init {
        val luckPerms = LuckPermsProvider.get()
        val user = luckPerms.userManager.getUser(player.uniqueId)
        val lobbyUserCollection = PixelDatabase.getCollection(LobbyUserCollection::class)
        val lobbyUser = lobbyUserCollection.getUser(player.uniqueId)
        if (user != null && lobbyUser != null) {
            val permissionGroup = luckPerms.groupManager.getGroup(user.primaryGroup)

            if (permissionGroup != null) {
                val legacySection = LegacyComponentSerializer.legacySection()
                updateScore(7, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_rank_title")))
                updateScore(6, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_rank_value",
                    "{rank}" to legacySection.serialize(LegacyComponentSerializer.legacyAmpersand().deserialize((permissionGroup.displayName ?: permissionGroup.name))))))
                updateScore(5, "§a")
                updateScore(4, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_currency_title")))
                updateScore(3, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_currency_value",
                    "{currency}" to lobbyUser.coins)))
                updateScore(2, "§c")
                updateScore(1, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_server_title")))
                updateScore(0, legacySection.serialize(CommandSenderUtil.getComponent(player, "lobby", "scoreboard_server_value",
                    "{server}" to CloudAPI.instance.getThisSidesName())))
            }
        }
    }
}