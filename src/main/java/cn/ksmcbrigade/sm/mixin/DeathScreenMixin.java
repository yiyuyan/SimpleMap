package cn.ksmcbrigade.sm.mixin;

import cn.ksmcbrigade.sm.SimpleMap;
import cn.ksmcbrigade.sm.config.Config;
import cn.ksmcbrigade.sm.config.WorldPointConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Inject(method = "<init>",at = @At("TAIL"))
    public void init(Component p_95911_, boolean p_95912_, CallbackInfo ci) throws IOException {
        if(Config.DEATH_POS_RECORD.get()){
            SimpleMap.configs.add(new WorldPointConfigs.PointConfig("DeathPosition-"+ (SimpleMap.configs.points()+1), Minecraft.getInstance().level.dimension().location(),Minecraft.getInstance().player.getPosition(0)));
        }
    }
}
