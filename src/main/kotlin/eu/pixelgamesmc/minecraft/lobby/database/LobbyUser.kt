package eu.pixelgamesmc.minecraft.lobby.database

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class LobbyUser(
    @SerialName("_id") @Contextual val uuid: UUID,
    var coins: Int
)
