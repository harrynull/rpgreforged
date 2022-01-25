package com.mod_author.mod_id

import com.google.common.math.IntMath.pow
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.registry.Registry
import kotlin.math.roundToInt
import kotlin.math.sqrt

enum class AttributeType(id: String, default: Double, translationKey: String) {
    CONSTITUTION("rpg.constitution", 10.0, "rpg.attribute.constitution"),
    STRENGTH("rpg.strength", 0.0, "rpg.attribute.strength"),
    DEFENSE("rpg.defense", 0.0, "rpg.attribute.defense"),
    DEXTERITY("rpg.dexterity", 0.0, "rpg.attribute.dexterity");

    val attribute: EntityAttribute

    init {
        attribute = Registry.register(
            Registry.ATTRIBUTE, id,
            ClampedEntityAttribute(translationKey, default, 0.0, 2000.0).setTracked(true)
        )
    }
    companion object{
        fun fill(container: AttributeContainer, attributes: PlayerAttributes) {
            values().forEach { attr ->
                container.getCustomInstance(attr.attribute)!!.baseValue =
                    (attributes.abilityBase[attr]
                        ?: PlayerAttributeComponent.DEFAULT_ATTRIBUTES[attr]!!).toDouble()
            }
        }
    }
}

interface PlayerAttributes {
    val exp: Int
    val abilityPoints: Int
    val expForNextLevel: Int
        get() = pow((level + 1) * 10, 2)
    val level: Int
        get() = sqrt(exp / 10.0).roundToInt()
    val abilityBase: Map<AttributeType, Int>

    fun gainExp(expGained: Int)
    fun useAbilityPoints(pts: Int, ability: AttributeType)
}

fun onExperienceGain(playerEntity: PlayerEntity, expGained: Int) {
    MyComponents.PLAYER_ATTRIBUTES.get(playerEntity).gainExp(expGained)
}

class PlayerAttributeComponent(
    private val provider: PlayerEntity
) : PlayerAttributes, AutoSyncedComponent {

    override var abilityPoints: Int = 0
        private set
    override var exp: Int = 0
        private set
    override var abilityBase: MutableMap<AttributeType, Int> = DEFAULT_ATTRIBUTES
        private set

    override fun gainExp(expGained: Int) {
        val oldLevel = level
        exp += expGained
        val newLevel = level
        if (newLevel != oldLevel) {
            abilityPoints += if (newLevel >= 60) 3
            else if (newLevel >= 20) 2
            else 1
        }
        MyComponents.PLAYER_ATTRIBUTES.sync(provider)
    }

    override fun useAbilityPoints(pts: Int, ability: AttributeType) {
        abilityPoints = maxOf(abilityPoints - pts, 0)
        abilityBase.compute(ability) { k, v -> v!! + 1 }
        MyComponents.PLAYER_ATTRIBUTES.sync(provider)
    }

    override fun readFromNbt(tag: NbtCompound) {
        abilityPoints = tag.getInt("ability_points")
        exp = tag.getInt("exp")
        val baseValues = tag.getIntArray("base_values")
        if (baseValues.size >= AttributeType.values().size) {
            abilityBase.keys.forEachIndexed { index, attributeType ->
                abilityBase[attributeType] = baseValues[index]
            }
        } else {
            DEFAULT_ATTRIBUTES.forEach { (k, v) -> abilityBase[k] = v }
        }
    }

    override fun writeToNbt(tag: NbtCompound) {
        tag.putInt("ability_points", abilityPoints)
        tag.putInt("exp", exp)
        tag.putIntArray("base_values", abilityBase.values.toList())
    }

    companion object {
        val DEFAULT_ATTRIBUTES = mutableMapOf(
            AttributeType.CONSTITUTION to 10,
            AttributeType.DEFENSE to 0,
            AttributeType.DEXTERITY to 0,
            AttributeType.STRENGTH to 0,
        )
    }
}

