package tech.harrynull.rpgreforged

import net.minecraft.item.ItemStack
import tech.harrynull.rpgreforged.MyComponents.Companion.WEAPON_ATTRIBUTES


fun processSocket(itemStack: ItemStack, itemStack2: ItemStack): ItemStack? {
    val attr = itemStack.getWeaponComponent() ?: return null
    val socket = (itemStack2.item as? SocketItem)?.socket ?: return null
    if (!attr.canAddSocket(socket)) return null

    val itemStackResult = itemStack.copy()
    val attrResult = WEAPON_ATTRIBUTES.get(itemStackResult)
    attrResult.addSocket(socket)
    return itemStackResult
}

fun processCombine(itemStack: ItemStack, itemStack2: ItemStack, itemStack3: ItemStack) {
    val attr1 = itemStack.getWeaponComponent() ?: return
    val attr2 = itemStack2.getWeaponComponent() ?: return

    val attr3 = itemStack3.getWeaponComponent()!!
    attr3.quality =
        if (attr1.quality != attr2.quality) {
            maxOf(attr1.quality + attr2.quality)
        } else minOf(attr1.quality + 1, 5)
    attr3.sockets = attr1.sockets
    attr3.reforge = attr1.reforge
    attr3.saveToNbt()
}
