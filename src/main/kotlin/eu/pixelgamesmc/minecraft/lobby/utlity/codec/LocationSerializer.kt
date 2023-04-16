package eu.pixelgamesmc.minecraft.lobby.utlity.codec

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.Location

object LocationSerializer : KSerializer<Location> {

    override val descriptor: SerialDescriptor = LocationSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): Location {
        val surrogate = decoder.decodeSerializableValue(LocationSurrogate.serializer())
        return Location(Bukkit.getWorld(surrogate.world), surrogate.x, surrogate.y, surrogate.z, surrogate.yaw, surrogate.pitch)
    }

    override fun serialize(encoder: Encoder, value: Location) {
        val surrogate = LocationSurrogate(value.world.name, value.x, value.y, value.z, value.yaw, value.pitch)
        encoder.encodeSerializableValue(LocationSurrogate.serializer(), surrogate)
    }

    @Serializable
    @SerialName("Location")
    data class LocationSurrogate(
        val world: String,
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float,
        val pitch: Float
    )
}