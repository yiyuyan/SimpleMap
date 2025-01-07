package cn.ksmcbrigade.sm.screen;

import cn.ksmcbrigade.sm.SimpleMap;
import cn.ksmcbrigade.sm.config.Config;
import cn.ksmcbrigade.sm.config.WorldPointConfigs;
import cn.ksmcbrigade.sm.config.mode.PointAction;
import cn.ksmcbrigade.sm.screen.edit.EditPointScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

public class PointsScreen extends Screen {

    private PointList list;

    public PointsScreen() {
        super(Component.literal("Points"));
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    protected void init() {
        this.minecraft = Minecraft.getInstance();
        try {
            this.list = new PointList(this.minecraft,this);
            this.addRenderableWidget(list);
            this.addRenderableWidget(new CheckBoxEx(width/2-this.minecraft.font.width(this.title)*6,2,50,16,Component.literal("Current level's points only"), Config.CURRENT_LEVEL_POINTS_ONLY.get(),(box)->{
                Config.CURRENT_LEVEL_POINTS_ONLY.set(box.selected());
                try {
                    this.list.ref(Config.CURRENT_LEVEL_POINTS_ONLY.get());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            this.addRenderableWidget(Button.builder(Component.literal("ADD"),(button)-> {
                try {
                    Minecraft.getInstance().setScreen(new EditPointScreen(new PointsScreen(),new WorldPointConfigs.PointConfig("Point#"+ (SimpleMap.configs.points()+1),Minecraft.getInstance().level.dimension().location(),Minecraft.getInstance().player.getPosition(0)), PointAction.ADD,false));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).pos(this.width/2-150,this.height - 30).width(150).build());
            this.addRenderableWidget(Button.builder(Component.literal("DONE"),(button)-> this.onClose()).pos(this.width/2,this.height - 30).width(150).build());
            this.list.ref(Config.CURRENT_LEVEL_POINTS_ONLY.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        super.render(p_281549_, p_281550_, p_282878_, p_282465_);
        p_281549_.drawCenteredString(Minecraft.getInstance().font,this.title,p_281549_.guiWidth()/2,12, Color.WHITE.getRGB());
    }
}
