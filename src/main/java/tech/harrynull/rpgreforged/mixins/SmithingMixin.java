package tech.harrynull.rpgreforged.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SmithingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tech.harrynull.rpgreforged.SmithingMixinLogicKt;

@Mixin(SmithingScreenHandler.class)
public class SmithingMixin {
    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    public void reforge(CallbackInfo ci) {
        ForgingScreenHandlerAccessor accessor = (ForgingScreenHandlerAccessor) this;
        if (SmithingMixinLogicKt.reforgePreview(accessor.getInput(), accessor.getOutput())) {
            ci.cancel();
        }
    }

    @Inject(method = "onTakeOutput", at = @At("HEAD"))
    public void takeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        SmithingMixinLogicKt.reforge(stack);
    }

    @Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
    public void canReforge(PlayerEntity player, boolean present,
                           CallbackInfoReturnable<Boolean> cir) {
        ForgingScreenHandlerAccessor accessor = (ForgingScreenHandlerAccessor) this;
        if (SmithingMixinLogicKt.canReforge(accessor.getInput()))
            cir.setReturnValue(true);
    }
}
