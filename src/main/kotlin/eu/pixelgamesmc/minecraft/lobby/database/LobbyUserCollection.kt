package eu.pixelgamesmc.minecraft.lobby.database

import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PixelCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PlayerCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import redis.clients.jedis.JedisPool
import java.util.*

class LobbyUserCollection(
    jedisPool: JedisPool,
    collection: MongoCollection<LobbyUser>
): PixelCollection<LobbyUser>(
    jedisPool, collection
), PlayerCollection {

    fun updateUser(lobbyUser: LobbyUser) {
        updateCache("lobby_user#${lobbyUser.uuid}", lobbyUser)
    }

    fun getUser(uuid: UUID): LobbyUser? {
        return getCache("lobby_user#$uuid", LobbyUser::uuid eq uuid, LobbyUser::class)
    }

    override fun playerLogin(uuid: UUID, name: String, skin: String) {
        if (collection.findOne(LobbyUser::uuid eq uuid) == null) {
            collection.insertOne(LobbyUser(uuid, 0))
        }
    }
}