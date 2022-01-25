package com.mod_author.mod_id

import drawer.readFrom
import drawer.write
import io.netty.buffer.Unpooled
import kotlinx.serialization.Serializable
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

val ID_USE_ABILITY_POINTS = Identifier("modid", "use_ability_points")

@Serializable
data class C2SUseAbilityPoints(val attributeName: String)

fun c2sUseAbilityPoints(attribute: AttributeType) {
    val packetData = PacketByteBuf(Unpooled.buffer())
    C2SUseAbilityPoints.serializer()
        .write(C2SUseAbilityPoints(attribute.name), toBuf = packetData)
    ClientPlayNetworking.send(ID_USE_ABILITY_POINTS, packetData)
}

fun registerServerListeners() {
    ServerPlayNetworking.registerGlobalReceiver(ID_USE_ABILITY_POINTS) { server, player, handler, packet, packetSender ->
        val request = C2SUseAbilityPoints.serializer().readFrom(packet)
        val component = MyComponents.PLAYER_ATTRIBUTES.get(player)
        component.useAbilityPoints(1, AttributeType.valueOf(request.attributeName))
    }
}