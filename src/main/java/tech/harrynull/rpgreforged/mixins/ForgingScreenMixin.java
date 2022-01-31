package tech.harrynull.rpgreforged.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tech.harrynull.rpgreforged.SmithingMixinLogicKt;

// for fixing quick move
@Mixin(ForgingScreenHandler.class)
public abstract class ForgingScreenMixin {
    @Shadow
    @Final
    protected CraftingResultInventory output;

    @Shadow
    protected abstract void onTakeOutput(PlayerEntity var1, ItemStack var2);

    @Shadow
    @Final
    protected PlayerEntity player;

    @Inject(
            method = "transferSlot",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/screen/slot/Slot.getStack ()Lnet/minecraft/item/ItemStack;",
                    shift = At.Shift.BY,
                    by = 2
            ), locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void mixin(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> cir,
                       ItemStack itemStack, Slot slot, ItemStack itemStack2) {
        if (index != 2) return;
        ForgingScreenHandler handler = ((ForgingScreenHandler) (Object) this);
        if (handler instanceof SmithingScreenHandler) {
            ForgingScreenHandlerAccessor accessor = (ForgingScreenHandlerAccessor) handler;
            if (SmithingMixinLogicKt.canReforge(accessor.getInput())) {
                SmithingMixinLogicKt.reforge(itemStack2);
            }
        }
    }
}
