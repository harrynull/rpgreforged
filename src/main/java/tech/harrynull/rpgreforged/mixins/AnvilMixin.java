package tech.harrynull.rpgreforged.mixins;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tech.harrynull.rpgreforged.AnvilMixinLogicKt;

import java.util.Map;

@Mixin(ForgingScreenHandler.class)
interface ForgingScreenHandlerAccessor {
    @Accessor
    CraftingResultInventory getOutput();
}

@Mixin(AnvilScreenHandler.class)
public class AnvilMixin {
    @Inject(
            method = "updateResult",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamageable()Z", ordinal = 0),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    public void handleSocket(CallbackInfo ci, ItemStack itemStack, int i, int j, int k, ItemStack itemStack2, ItemStack itemStack3, Map<Enchantment, Integer> map, boolean bl) {
        AnvilScreenHandler handler = (AnvilScreenHandler) (Object) this;
        ItemStack result = AnvilMixinLogicKt.processSocket(itemStack, itemStack3);
        if (result != null) {
            ((ForgingScreenHandlerAccessor) handler).getOutput().setStack(0, result);
            handler.sendContentUpdates();
            ci.cancel();
        }
    }

    @Inject(
            method = "updateResult",
            at = @At(value = "INVOKE", target = "net/minecraft/enchantment/EnchantmentHelper.set (Ljava/util/Map;Lnet/minecraft/item/ItemStack;)V", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void handleCombine(CallbackInfo ci, ItemStack itemStack, int i, int j, int k, ItemStack itemStack2, ItemStack itemStack3, Map map, int bl) {
        AnvilMixinLogicKt.processCombine(itemStack, itemStack3, itemStack2);
    }
}