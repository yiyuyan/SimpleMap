package cn.ksmcbrigade.sm.mixin;

import cn.ksmcbrigade.sm.utils.MapDataMixinAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MapItemSavedData.class)
public class MapDataFix implements MapDataMixinAccessor {
    @Shadow public byte[] colors;
    @Unique
    private int size = 128;

    @Override
    public int get() {
        return this.size;
    }

    @Override
    public void set(int size) {
        this.size = size;
        this.setColorsSize();
    }

    @Override
    public void setColorsSize() {
        this.colors = new byte[this.size*size];
    }

    @ModifyConstant(method = "addDecoration",constant = @Constant(intValue = -128))
    private int decoration(int constant){
        return -this.size;
    }

    @ModifyConstant(method = {"updateColor","setColor"},constant = @Constant(intValue = 128))
    private int color(int constant){
        return this.size;
    }

    @Inject(method = {"getHoldingPlayer"},at = @At("RETURN"),cancellable = true)
    private void fresh(Player p_77917_, CallbackInfoReturnable<MapItemSavedData.HoldingPlayer> cir){
        MapItemSavedData.HoldingPlayer holdingPlayer = cir.getReturnValue();
        ((MapDataMixinAccessor)holdingPlayer).set(this.size);
        cir.setReturnValue(holdingPlayer);
    }
}
