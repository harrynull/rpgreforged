package com.mod_author.mod_id

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import kotlin.math.roundToInt

interface PlayerAttributes {
    val exp: Int
    val abilityPoints: Int
    val level: Int

    fun gainExp(expGained: Int)
}

fun onExperienceGain(
    playerEntity: PlayerEntity,
    expGained: Int
) {
    MyComponents.PLAYER_ATTRIBUTES.get(playerEntity).gainExp(expGained)
}

class PlayerAttributeComponent(
    private val provider: PlayerEntity
) : PlayerAttributes, AutoSyncedComponent {

    override var abilityPoints: Int = 0
    override var exp: Int = 0
    override val level: Int
        get() = kotlin.math.log((exp + 1).toDouble(), 8.0).roundToInt()

    override fun gainExp(expGained: Int) {
        exp += expGained
        MyComponents.PLAYER_ATTRIBUTES.sync(provider)
    }

    override fun readFromNbt(tag: NbtCompound) {
        abilityPoints = tag.getInt("ability_points")
        exp = tag.getInt("exp")
    }

    override fun writeToNbt(tag: NbtCompound) {
        tag.putInt("ability_points", abilityPoints)
        tag.putInt("exp", exp)
    }
}

