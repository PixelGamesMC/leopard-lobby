package eu.pixelgamesmc.minecraft.lobby.database

import com.mongodb.client.MongoCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PixelCollection
import eu.pixelgamesmc.minecraft.servercore.database.collection.PlayerCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import redis.clients.jedis.JedisPool
import java.util.*

class LobbyUserCollection(
    collection: MongoCollection<LobbyUser>
): PixelCollection<LobbyUser>(
    collection
), PlayerCollection {

    fun updateUser(lobbyUser: LobbyUser) {
        collection.save(lobbyUser)
    }

    fun getUser(uuid: UUID): LobbyUser? {
        return collection.findOne(LobbyUser::uuid eq uuid)
    }

    override fun playerLogin(uuid: UUID, name: String, skin: String) {
        if (collection.findOne(LobbyUser::uuid eq uuid) == null) {
            collection.insertOne(LobbyUser(uuid, 0))
        }
    }
}