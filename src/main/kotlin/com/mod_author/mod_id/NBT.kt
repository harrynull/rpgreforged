package com.mod_author.mod_id

import net.minecraft.nbt.NbtCompound
import kotlin.reflect.KProperty


class NBT(private val compound: NbtCompound, private val nbtKeyName: String) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        return compound.getDouble(nbtKeyName)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        compound.putDouble(nbtKeyName, value)
    }
}