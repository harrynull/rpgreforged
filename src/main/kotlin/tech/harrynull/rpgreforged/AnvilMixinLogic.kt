package tech.harrynull.rpgreforged

import net.minecraft.item.ItemStack


fun processSocket(itemStack: ItemStack, itemStack2: ItemStack): ItemStack? {
    val socket = (itemStack2.item as? SocketItem)?.socket ?: return null
    val itemStackResult = itemStack.copy()
    itemStackResult?.getRpgComponent()?.takeIf { it.canAddSocket(socket) }
        ?.addSocket(socket)
    return itemStackResult
}

fun shouldOverrideCombine(in1: ItemStack, in2: ItemStack): Boolean {
    val attr1 = in1.getRpgComponent() ?: return false
    val attr2 = in2.getRpgComponent() ?: return false
    return attr1.quality == attr2.quality
}


fun processCombine(itemStack: ItemStack, itemStack2: ItemStack, itemStack3: ItemStack) {
    val attr1 = itemStack.getRpgComponent() ?: return
    val attr2 = itemStack2.getRpgComponent() ?: return
    if (attr1.itemType != attr2.itemType) return
    itemStack3.getRpgComponent()!!.anvilCombine(attr1, attr2)
}
