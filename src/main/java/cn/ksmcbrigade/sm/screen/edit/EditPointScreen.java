package cn.ksmcbrigade.sm.screen.edit;

import cn.ksmcbrigade.sm.SimpleMap;
import cn.ksmcbrigade.sm.config.WorldPointConfigs;
import cn.ksmcbrigade.sm.config.mode.PointAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class EditPointScreen extends Screen {

    public final Screen lastScreen;
    public final WorldPointConfigs.PointConfig alt;
    public final PointAction action;


    private Button selectButton;
    private EditBox Edit;
    private EditBox pos;
    private final boolean renderBackground;

    public EditPointScreen(Screen screen, @Nullable WorldPointConfigs.PointConfig alt, PointAction action, boolean background) throws IOException {
        super(Component.literal("Edit Point"));
        this.lastScreen = screen;
        this.alt = alt;
        this.action = action;
        this.renderBackground = background;
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    public boolean keyPressed(int p_95964_, int p_95965_, int p_95966_) {
        if (!this.selectButton.active || this.getFocused() != this.Edit || p_95964_ != 257 && p_95964_ != 335) {
            return super.keyPressed(p_95964_, p_95965_, p_95966_);
        } else {
            try {
                this.onSelect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    @Override
    protected void init() {
        if(this.minecraft==null){
            this.minecraft = Minecraft.getInstance();
        }
        this.Edit = new EditBox(this.font, this.width / 2 - 100, 100, 200, 20, Component.literal("Point Name"));
        this.pos = new EditBox(this.font, this.width / 2 - 100, 100+this.Edit.getHeight()+2, 200, 20, Component.literal("Point Name"));
        this.Edit.setMaxLength(128);
        this.Edit.setValue(this.alt!=null?this.alt.name():"Point");
        this.pos.setValue(this.alt!=null?pos():"0 0 0");
        this.addWidget(this.Edit);
        this.addWidget(this.pos);
        this.selectButton = this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_DONE, p_95981_ -> {
                            try {
                                this.onSelect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        })
                        .bounds(this.width / 2 - 100, this.height / 4 + 96 + 12, 200, 20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_CANCEL, p_95977_ -> this.onClose())
                        .bounds(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20)
                        .build()
        );
    }

    @Override
    public void resize(@NotNull Minecraft p_95973_, int p_95974_, int p_95975_) {
        String s = this.Edit.getValue();
        this.init(p_95973_, p_95974_, p_95975_);
        this.Edit.setValue(s);
    }

    private void onSelect() throws IOException {
        String[] posString = this.pos.getValue().split(" ");
        Vec3 vec3;
        try {
            vec3 = new Vec3(Double.parseDouble(posString[0]),Double.parseDouble(posString[1]),Double.parseDouble(posString[2]));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        if(this.action.equals(PointAction.ADD)){
            SimpleMap.configs.add(new WorldPointConfigs.PointConfig(this.Edit.getValue(),alt.levelResourceLocation(),vec3));
        }
        else{
            SimpleMap.configs.replace(this.alt,new WorldPointConfigs.PointConfig(this.Edit.getValue(),alt.levelResourceLocation(),vec3));
        }
        this.onClose();
    }

    @Override
    public void onClose() {
        if(this.minecraft==null){
            this.minecraft = Minecraft.getInstance();
        }
        this.minecraft.setScreen(this.lastScreen);
    }


    @Override
    public void render(@NotNull GuiGraphics p_282464_, int p_95969_, int p_95970_, float p_95971_) {
        super.render(p_282464_, p_95969_, p_95970_, p_95971_);
        p_282464_.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
        this.Edit.render(p_282464_, p_95969_, p_95970_, p_95971_);
        this.pos.render(p_282464_, p_95969_, p_95970_, p_95971_);
    }

    public String pos(){
        Vec3 pos = this.alt.pos();
        return String.format("%.1f",pos.x)+" "+String.format("%.1f",pos.y)+" "+String.format("%.1f",pos.z);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics p_283688_) {
        if(renderBackground) super.renderBackground(p_283688_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
