package cn.ksmcbrigade.sm.mixin;

import cn.ksmcbrigade.sm.utils.MapDataMixinAccessor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MapItemSavedData.HoldingPlayer.class)
public class MapHoldingPlayerFix implements MapDataMixinAccessor {
    @Unique
    private int size = 128;

    @Override
    public int get() {
        return this.size;
    }

    @Override
    public void set(int size) {
        this.size = 128;
    }

    @Override
    public void setColorsSize() {}

    @ModifyConstant(method = {"createPatch"},constant = @Constant(intValue = 128))
    private int fresh(int constant){
        return this.size;
    }
}
