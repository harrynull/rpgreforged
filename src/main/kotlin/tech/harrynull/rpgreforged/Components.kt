package tech.harrynull.rpgreforged

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.util.Identifier

fun ItemStack.getWeaponComponent(): WeaponRPGAttributeComponent? {
    return MyComponents.WEAPON_ATTRIBUTES.maybeGet(this)
        .takeIf { it.isPresent }?.get()
}

fun ItemStack.getArmorComponent(): ArmorRPGAttributeComponent? {
    return MyComponents.ARMOR_ATTRIBUTES.maybeGet(this)
        .takeIf { it.isPresent }?.get()
}

fun ItemStack.getRpgComponent(): IAttributes? {
    return getWeaponComponent() ?: getArmorComponent()
}

class MyComponents : EntityComponentInitializer, ItemComponentInitializer {
    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
    }

    @Suppress("UnstableApiUsage")
    override fun registerItemComponentFactories(registry: ItemComponentFactoryRegistry) {
        registry.register(
            { item -> item is SwordItem },
            WEAPON_ATTRIBUTES
        ) { item: ItemStack ->
            WeaponRPGAttributeComponent(item)
        }
        registry.register(
            { item -> item is ArmorItem },
            ARMOR_ATTRIBUTES
        ) { item: ItemStack ->
            ArmorRPGAttributeComponent(item)
        }
    }

    companion object {
        val WEAPON_ATTRIBUTES: ComponentKey<WeaponRPGAttributeComponent> =
            ComponentRegistryV3.INSTANCE.getOrCreate(
                Identifier("rpgreforged:weapon_attributes"),
                WeaponRPGAttributeComponent::class.java
            )
        val ARMOR_ATTRIBUTES: ComponentKey<ArmorRPGAttributeComponent> =
            ComponentRegistryV3.INSTANCE.getOrCreate(
                Identifier("rpgreforged:armor_attributes"),
                ArmorRPGAttributeComponent::class.java
            )
    }
}
