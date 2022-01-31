package tech.harrynull.rpgreforged.mixins;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.harrynull.rpgreforged.RPGAttributesKt;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeMixin {
    @Shadow
    @Final
    ItemStack output;

    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    public void craft(CraftingInventory craftingInventory, CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
        ItemStack transformed = RPGAttributesKt.onCraftCallback(output.copy());
        if (transformed != null) callbackInfoReturnable.setReturnValue(transformed);
    }
}
