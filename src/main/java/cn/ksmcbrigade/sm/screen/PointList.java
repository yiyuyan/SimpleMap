package cn.ksmcbrigade.sm.screen;

import cn.ksmcbrigade.sm.SimpleMap;
import cn.ksmcbrigade.sm.config.WorldPointConfigs;
import cn.ksmcbrigade.sm.config.mode.PointAction;
import cn.ksmcbrigade.sm.screen.edit.EditPointScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class PointList extends ContainerObjectSelectionList<PointList.Entry> {
    public PointList(Minecraft p_193862_,PointsScreen p_193861_) throws IOException {
        super(p_193862_, p_193861_.width + 85, p_193861_.height, 20, p_193861_.height - 32, 20);for (WorldPointConfigs.PointConfig config : SimpleMap.configs.getOrCreatePointConfigs(SimpleMap.configs.levelName()).configs) addEntry(new Entry(config));
    }

    public void ref(boolean only) throws IOException{
        this.clearEntries();
        if(!only){
            for (WorldPointConfigs.PointConfig config : SimpleMap.configs.getOrCreatePointConfigs(SimpleMap.configs.levelName()).configs) {
                addEntry(new Entry(config));
            }
        }
        else{
            for (WorldPointConfigs.PointConfig pointConfig : SimpleMap.configs.getOrCreatePointConfigs(SimpleMap.configs.levelName()).configs.stream().filter(config -> config.levelResourceLocation().equals(Minecraft.getInstance().level.dimension().location())).toList()) {
                addEntry(new Entry(pointConfig));
            }
            this.setScrollAmount(0);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class Entry extends ContainerObjectSelectionList.Entry<Entry> {

        public WorldPointConfigs.PointConfig config;
        public Button tp;
        public Button gotoB;
        public Button edit;
        public Button del;

        public Entry(WorldPointConfigs.PointConfig config) {
            this.config = config;
            this.tp = Button.builder(Component.literal("TP"), (p_269618_) -> {
                Minecraft.getInstance().getConnection().sendCommand(config.command());
                Minecraft.getInstance().setScreen(null);
            }).bounds(0, 0, 25, 20).build();
            this.gotoB = Button.builder(Component.literal("GOTO"), (p_269616_) -> {
                if(ModList.get().isLoaded("baritone")){
                    Minecraft.getInstance().getConnection().sendChat(config.gotoCommand());
                    Minecraft.getInstance().setScreen(null);
                }
            }).bounds(0, 0, 30, 20).build();
            this.edit = Button.builder(Component.literal("EDIT"), (p_269616_) -> {
                try {
                    Minecraft.getInstance().setScreen(new EditPointScreen(new PointsScreen(),config, PointAction.EDIT,true));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).bounds(0, 0, 30, 20).build();
            this.del = Button.builder(Component.literal("DEL"), (p_269616_) -> {
                try {
                    SimpleMap.configs.getOrCreatePointConfigs(SimpleMap.configs.levelName()).configs.remove(this.config);
                    SimpleMap.configs.save(true);
                    PointList.this.removeEntry(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).bounds(0, 0, 30, 20).build();
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int i, int i1, int i2, int i3, int i4, int i5, int i6, boolean b, float v) {
            int k = i2 + 90 - Minecraft.getInstance().font.width(config.pos().toString())/4;
            int nameX = calculateTextX(i2, config.name());
            int posX = calculateTextX(i2, pos())+2+Minecraft.getInstance().font.width(config.name());
            guiGraphics.drawString(Minecraft.getInstance().font, config.name(), nameX, i1 + i4 / 2 - 4, 16777215, false);
            guiGraphics.drawString(Minecraft.getInstance().font, pos(), nameX+Minecraft.getInstance().font.width(config.name())+5 ,i1 + i4 / 2 - 4, 16777215, false);
            this.tp.setX(calculateButtonX(i2, 0,this.tp));
            this.tp.setY(i1);
            this.tp.render(guiGraphics, i5, i6, v);
            this.gotoB.setX(calculateButtonX(i2, 1,this.gotoB));
            this.gotoB.setY(i1);
            this.gotoB.render(guiGraphics, i5, i6, v);
            this.edit.setX(calculateButtonX(i2, 2,this.edit));
            this.edit.setY(i1);
            this.edit.render(guiGraphics, i5, i6, v);
            this.del.setX(calculateButtonX(i2, 3,this.del));
            this.del.setY(i1);
            this.del.render(guiGraphics, i5, i6, v);
        }

        private int calculateButtonX(int i2, int buttonIndex,Button button) {
            Minecraft minecraft = Minecraft.getInstance();
            int baseX = calculateTextX(i2,config.name())+ minecraft.font.width(config.name()) + 5 + minecraft.font.width(pos());
            int buttonWidth = button.getWidth();
            return baseX + buttonIndex * (buttonWidth + 5);
        }

        private int calculateTextX(int i2, String text) {
            Minecraft minecraft = Minecraft.getInstance();
            int baseX = i2 - 65 - 20;
            return baseX - minecraft.font.width(text)/2;
        }

        public String pos(){
            Vec3 pos = config.pos();
            return String.format("%.1f",pos.x)+","+String.format("%.1f",pos.y)+","+String.format("%.1f",pos.z);
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return List.of();
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return List.of(tp,gotoB,edit,del);
        }
    }
}
