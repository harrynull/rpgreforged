package tech.harrynull.rpgreforged

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import dev.onyxstudios.cca.api.v3.item.ItemComponent
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Rarity
import tech.harrynull.rpgreforged.mixins.SwordItemMixin
import java.util.*
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

val ATTACK_SPEED_MODIFIER_ID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3")

interface RPGItemAttributes {
    val rarity: Rarity
    val levelRequirement: Int
    val score: Int

    var reforge: Reforge // effect strength dependent on quality
    val quality: Int // stars from 1 to 5
    val sockets: MutableList<Socket> // dependent on quality

    fun numberOfSocketsByQuality(quality: Int): Int = quality.div(2.0).roundToInt()

    fun canAddSocket(socket: Socket): Boolean = sockets.any { it == Socket.EmptySocket }

    fun saveToNbt()

    fun loadFromNbt()

    fun addSocket(socket: Socket) {
        sockets[sockets.indexOfFirst { it == Socket.EmptySocket }] = socket
        saveToNbt()
    }

    fun randomQuality(): Int {
        val rand = kotlin.random.Random.nextDouble()
        if (rand < 0.5) return 1 // 50%
        if (rand < 0.7) return 2 // 20%
        if (rand < 0.85) return 3 // 15%
        if (rand < 0.95) return 4 // 10%
        return 5 // 5%
    }

    fun reforge()
}

interface WeaponAttributes : RPGItemAttributes {
    var baseDamage: Double
    var damageSpread: Double
    var attackSpeed: Double
    val minDamage: Double
    val maxDamage: Double
    val dps: Double

    fun getRandomDamageOffset(random: Random): Double
    fun toolTip(): List<Text>
}


class WeaponRPGAttributeComponent(private val itemStack: ItemStack) :
    WeaponAttributes, ItemComponent(itemStack, MyComponents.WEAPON_ATTRIBUTES) {

    override var baseDamage: Double = 0.0
    override var damageSpread: Double = 0.0
    override var attackSpeed: Double = 0.0

    override val levelRequirement: Int
        get() = maxOf(floor(sqrt(score.toDouble()) - 8).roundToInt() * 3, 0)

    override val score: Int
        get() {
            return (aggregatedAttributes.toList().sumOf { it.second * it.second } / 5 +
                dps * dps / 12).roundToInt()
        }

    override var reforge: Reforge = Reforge.Raw // effect strength dependent on quality
    override var quality: Int = 0 // stars from 1 to 5
    override var sockets: MutableList<Socket> = mutableListOf() // number dependent on quality

    override val minDamage get() = baseDamage * (1 - damageSpread)
    override val maxDamage get() = baseDamage * (1 + damageSpread)
    override val dps get() = baseDamage * (4 - attackSpeed)
    override fun getRandomDamageOffset(random: Random): Double =
        (random.nextDouble() * 2 - 1) * damageSpread * baseDamage

    override val rarity: Rarity
        get() {
            val modRarity = if (score > 240) Rarity.EPIC
            else if (score > 120) Rarity.RARE
            else if (score > 60) Rarity.UNCOMMON
            else Rarity.COMMON
            return maxOf(modRarity, itemStack.rarity)
        }

    val aggregatedAttributes: Map<AttributeType, Double>
        get() = mutableMapOf<AttributeType, Double>().apply {
            reforge.modifier(this, quality)
            sockets.forEach { it.modifier(this, quality) }
        }

    val attributeUUIDs: MutableMap<AttributeType, UUID> = mutableMapOf()

    fun getAttributeMultimap(): HashMultimap<EntityAttribute, EntityAttributeModifier> {
        val map = aggregatedAttributes.map {
            it.key.attribute.get() to additionMultiplier(
                attributeUUIDs.getOrDefault(it.key, UUID.randomUUID()),
                it.value,
                "RPG Item"
            )
        }.toMap()
        return HashMultimap.create<EntityAttribute, EntityAttributeModifier>().apply {
            map.forEach { (k, v) -> this.put(k, v) }
        }
    }

    fun forge() {
        quality = randomQuality()
        sockets = (0 until numberOfSocketsByQuality(quality)).map { Socket.EmptySocket }.toMutableList()
        reforge = Reforge.randomReforge()
        saveToNbt()
    }

    override fun reforge() {
        reforge = Reforge.randomReforge()
        saveToNbt()
    }

    override fun saveToNbt() {
        putInt("quality", quality)
        sockets.forEachIndexed { index, socket -> putString("socket_$index", socket.name) }
        putString("reforge", reforge.name)
    }

    override fun loadFromNbt() {
        if (hasTag("quality")) quality = getInt("quality")
        sockets = (0 until quality).map {
            if (hasTag("socket_$it"))
                Socket.valueOf(getString("socket_$it"))
            else Socket.EmptySocket
        }.toMutableList()
        if (hasTag("reforge")) reforge = Reforge.valueOf(getString("reforge"))
        if (hasTag("damage_spread")) damageSpread = getDouble("damage_spread")
        if (hasTag("base_damage")) baseDamage = getDouble("base_damage")
        if (hasTag("attack_speed")) attackSpeed = getDouble("attack_speed")
        //if (hasTag("level_requirement")) levelRequirement = getInt("level_requirement")
    }

    init {
        baseDamage = (itemStack.item as SwordItem).attackDamage.toDouble()
        attackSpeed = (itemStack.item as SwordItemMixin)
            .attributeModifiers!![EntityAttributes.GENERIC_ATTACK_SPEED]
            .find { it!!.id == ATTACK_SPEED_MODIFIER_ID }
            ?.value ?: 0.0
        damageSpread = 0.2
        loadFromNbt()
    }

    override fun toolTip(): List<Text> {
        val basicInfo = listOf(
            LiteralText(reforge.name + " " + itemStack.name.string + " ").formatted(rarity.formatting).append(
                LiteralText("★".repeat(quality)).formatted(Formatting.YELLOW)
            ),
            LiteralText("$rarity ITEM (RATING $score)").formatted(rarity.formatting),
            LiteralText(
                "Base Damage: ${minDamage.toString(1)} ~ " +
                    "${maxDamage.toString(1)} " +
                    "(DPS ${dps.toString(1)})"
            ),
            LiteralText("")
        )

        val attributes = aggregatedAttributes.map { (attribute, value) ->
            LiteralText(attribute.icon + " ")
                .append(TranslatableText(attribute.attribute.get().translationKey))
                .append(LiteralText(": $value"))
                .formatted(attribute.formatting)
        } + listOf(LiteralText(""))

        val sockets: List<Text> = if (sockets.isNotEmpty())
            sockets.map { LiteralText(" ").append(it.description) }.toMutableList().apply {
                add(0, LiteralText("Sockets:"))
                add(LiteralText(""))
            }
        else listOf(LiteralText("Use a reforge station to unlock sockets.").formatted(Formatting.GRAY))

        val otherInfo = listOf(
            LiteralText("Requires Lv $levelRequirement to use").formatted(Formatting.GRAY)
        )

        return basicInfo + attributes + sockets + otherInfo
    }
}

fun addItemAttributes(
    itemStack: ItemStack,
    slot: EquipmentSlot,
    modifiers: Multimap<EntityAttribute, EntityAttributeModifier>
) {
    val attributes = MyComponents.WEAPON_ATTRIBUTES.maybeGet(itemStack)
        .takeIf { it.isPresent }?.get() ?: return
    if (slot != EquipmentSlot.MAINHAND) return
    modifiers.putAll(attributes.getAttributeMultimap())
}
