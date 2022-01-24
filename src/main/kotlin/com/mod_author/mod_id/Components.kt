package com.mod_author.mod_id

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.util.Identifier

class MyComponents : EntityComponentInitializer, ItemComponentInitializer {
    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        //registry.registerForPlayers(
        //    PLAYER_ATTRIBUTES,
        //    ComponentFactory { player: PlayerEntity? -> },
        //    RespawnCopyStrategy.ALWAYS_COPY
        //)
    }

    override fun registerItemComponentFactories(registry: ItemComponentFactoryRegistry) {
        registry.register(
            { item -> item is SwordItem },
            WEAPON_ATTRIBUTES
        ) { item: ItemStack ->
            WeaponRPGAttributeComponent(item)
        }
    }

    companion object {
        //val PLAYER_ATTRIBUTES: ComponentKey<IntComponent> = ComponentRegistryV3.INSTANCE.getOrCreate(
        //    Identifier("mymod:attributes"),
        //    IntComponent::class.java
        //)
        val WEAPON_ATTRIBUTES: ComponentKey<WeaponRPGAttributeComponent> =
            ComponentRegistryV3.INSTANCE.getOrCreate(
                Identifier("mymod:weapon_attributes"),
                WeaponRPGAttributeComponent::class.java
            )
    }

}