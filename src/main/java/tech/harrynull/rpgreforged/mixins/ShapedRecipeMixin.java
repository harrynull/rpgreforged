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
import tech.harrynull.rpgreforged.MyComponents;
import tech.harrynull.rpgreforged.WeaponRPGAttributeComponent;

import java.util.Optional;

@Mixin(ShapedRecipe.class)
public abstract class ShapedRecipeMixin {
    @Shadow
    @Final
    ItemStack output;

    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    public void craft(CraftingInventory craftingInventory, CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
        ItemStack copy = output.copy();
        Optional<WeaponRPGAttributeComponent> attr = MyComponents.Companion.getWEAPON_ATTRIBUTES().maybeGet(copy);
        if (attr.isEmpty()) return;
        attr.get().forge();
        callbackInfoReturnable.setReturnValue(copy);
    }
}
