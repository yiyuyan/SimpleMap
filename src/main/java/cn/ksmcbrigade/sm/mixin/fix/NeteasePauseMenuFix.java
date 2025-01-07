package cn.ksmcbrigade.sm.mixin.fix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.ModListScreen;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PauseScreen.class)
public abstract class NeteasePauseMenuFix {

    @Unique
    private boolean simpleMap$net = false;

    @Inject(method = "<init>",at = @At("TAIL"))
    public void init(boolean p_96308_, CallbackInfo ci){
        for (IModInfo mod : ModList.get().getMods()) {
            if(mod.getModId().toLowerCase().contains("netease")){
                simpleMap$net = true;
                break;
            }
        }
    }

    @Inject(method = "createPauseMenu",at = @At("TAIL"),locals = LocalCapture.CAPTURE_FAILHARD)
    public void init(CallbackInfo ci, GridLayout gridlayout, GridLayout.RowHelper gridlayout$rowhelper, Component component){
        if(simpleMap$net){
            gridlayout$rowhelper.addChild(Button.builder(Component.translatable("fml.menu.mods"), (p_280815_) -> Minecraft.getInstance().setScreen(new ModListScreen((Screen) (Object)this))).width(204).build(), 2);
        }
    }
}
