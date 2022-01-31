package tech.harrynull.rpgreforged.mixins;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Accessor
    Inventory getInput();
}

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilMixin {
    @Shadow
    @Final
    private Property levelCost;

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

    // process the cases where the original output would be empty e.g. two items are at full durability.
    @Inject(
            method = "updateResult",
            at = @At(value = "RETURN"))
    public void handleCombineEmpty(CallbackInfo ci) {
        AnvilScreenHandler handler = (AnvilScreenHandler) (Object) this;
        ForgingScreenHandlerAccessor accessor = (ForgingScreenHandlerAccessor) handler;

        if (!accessor.getOutput().getStack(0).isEmpty()) return;

        ItemStack input1 = accessor.getInput().getStack(0);
        ItemStack input2 = accessor.getInput().getStack(1);
        if (input1.getItem().equals(input2.getItem())) {
            ItemStack output = input1.copy();
            if (AnvilMixinLogicKt.shouldOverrideCombine(input1, input2)) {
                AnvilMixinLogicKt.processCombine(input1, input2, output);
                levelCost.set(5);
                accessor.getOutput().setStack(0, output);
                handler.sendContentUpdates();
            }
        }
    }
}