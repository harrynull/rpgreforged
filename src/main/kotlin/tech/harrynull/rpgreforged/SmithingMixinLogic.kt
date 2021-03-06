package tech.harrynull.rpgreforged

import net.minecraft.inventory.CraftingResultInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

fun canReforge(input: Inventory): Boolean {
    val itemStack1 = input.getStack(0)
    val itemStack2 = input.getStack(1)
    itemStack1.getRpgComponent() ?: return false
    if (!itemStack1.item.canRepair(itemStack1, itemStack2)) return false
    return true
}

fun reforgePreview(input: Inventory, output: CraftingResultInventory): Boolean {
    if (!canReforge(input)) return false
    output.setStack(0, input.getStack(0).copy())
    return true
}

fun reforge(stack: ItemStack): Boolean {
    return stack.getRpgComponent()?.reforge() != null
}