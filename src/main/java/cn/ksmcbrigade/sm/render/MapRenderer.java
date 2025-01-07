package cn.ksmcbrigade.sm.render;

import cn.ksmcbrigade.sm.SimpleMap;
import cn.ksmcbrigade.sm.config.Config;
import cn.ksmcbrigade.sm.config.mode.MapMode;
import cn.ksmcbrigade.sm.config.mode.MapPositionMode;
import cn.ksmcbrigade.sm.records.PosRecord;
import cn.ksmcbrigade.sm.utils.LevelUtils;
import cn.ksmcbrigade.sm.utils.MapDataMixinAccessor;
import cn.ksmcbrigade.sm.utils.MapUtils;
import cn.ksmcbrigade.sm.utils.PlayerPosUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;

@Mod.EventBusSubscriber(modid = SimpleMap.MODID,value = Dist.CLIENT)
public class MapRenderer {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void renderBackground(RenderGuiEvent.Post event){
        GuiGraphics drawContext = event.getGuiGraphics();
        Minecraft MC = Minecraft.getInstance();
        int screen_width = drawContext.guiWidth();

        int size = Config.SIZE.get();
        int width = size+1,height = size+1;
        int y = 1,x = screen_width-1;

        if(Config.MAP_POSITION.get()== MapPositionMode.LEFT){
            x = 1;
        }

        int pos = y+width+1;

        ResourceLocation mapRes = Maps.getMapLocation(size);
        update(mapRes,size);

        if(Config.RENDER_MODE.get()==MapMode.NORMAL){
            if(Config.MAP_POSITION.get()== MapPositionMode.LEFT){
                drawContext.fill(x,y,x+width,y+height+1, Color.WHITE.getRGB(), Config.MAP_BACKGROUND.get());
            }
            else{
                drawContext.fill(x,y,x-width,y+height+1, Color.WHITE.getRGB(), Config.MAP_BACKGROUND.get());
            }
        }

        if (mapRes != null) {
            if(Config.MAP_POSITION.get()== MapPositionMode.LEFT){
                drawContext.blit(mapRes,x,y+1,0,0,size,size,size,size);
            }
            else{
                drawContext.blit(mapRes,x-width,y+1,0,0,size,size,size,size);
            }
        }

        if(Config.SHOW_POSITION.get()) drawContext.drawCenteredString(MC.font, PlayerPosUtils.stringPos(),x-(width/2),pos,Color.WHITE.getRGB());
        if(Config.SHOW_BIOME.get()) {
            try {
                drawContext.drawCenteredString(MC.font, PlayerPosUtils.biome(),x-(width/2),pos+9,Color.WHITE.getRGB());
                pos+=9;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Config.SHOW_TIME.get()) drawContext.drawCenteredString(MC.font, LevelUtils.time(),x-(width/2),pos+9,Color.WHITE.getRGB());
    }

    private static void update(ResourceLocation mapRes,int size){
        DynamicTexture map = Maps.getMap(mapRes);
        MapItemSavedData data = Maps.getSaveData(size,PlayerPosUtils.getOnPos().getX(), PlayerPosUtils.getOnPos().getZ(), (byte)0, false, false,LevelUtils.getLevel());
        ((MapDataMixinAccessor)data).set(size);
        MapUtils.update(Minecraft.getInstance().level,Minecraft.getInstance().player,data,size,Config.RENDER_MODE.get());
        for(int i = 0; i < size; ++i) {
            for(int j = 0; j < size; ++j) {
                int k = j + i * size;
                Objects.requireNonNull(map.getPixels()).setPixelRGBA(j, i, MapColor.getColorFromPackedId(data.colors[k]));
            }
        }
        PosRecord PLAYER = MapUtils.getMapPos(Minecraft.getInstance().player,data,size,Config.RENDER_MODE.get());
        if(PLAYER.x()<0 || PLAYER.y()>=size || PLAYER.x()>=size || PLAYER.y()<0) return;
        Objects.requireNonNull(map.getPixels()).setPixelRGBA(PLAYER.x(),PLAYER.y(),-256);
        if(PLAYER.y()+1<size){
            map.getPixels().setPixelRGBA(PLAYER.x(),PLAYER.y()+1,-256);
        }
        if(PLAYER.y()-1>=0){
           map.getPixels().setPixelRGBA(PLAYER.x(),PLAYER.y()-1,-256);
        }
        if(PLAYER.x()+1<size){
            map.getPixels().setPixelRGBA(PLAYER.x()+1,PLAYER.y(),-256);
        }
        if(PLAYER.x()-1>=0){
            map.getPixels().setPixelRGBA(PLAYER.x()-1,PLAYER.y(),-256);
        }

        if(Config.SHOW_MOBS.get()){
            for (Mob entitiesOfClass : Minecraft.getInstance().level.getEntitiesOfClass(Mob.class, new AABB(Minecraft.getInstance().player.getPosition(0), Minecraft.getInstance().player.getPosition(0)).inflate((double) size / 2))) {
                PosRecord MOB = MapUtils.getMapPos(entitiesOfClass,data,size,Config.RENDER_MODE.get());
                if(MOB.x()<0 || MOB.y()>=size || MOB.x()>=size || MOB.y()<0) continue;
                Objects.requireNonNull(map.getPixels()).setPixelRGBA(MOB.x(),MOB.y(),-65536);
            }
        }

        if(Config.SHOW_ANIMALS.get()){
            for (Mob entitiesOfClass : Minecraft.getInstance().level.getEntitiesOfClass(Animal.class, new AABB(Minecraft.getInstance().player.getPosition(0), Minecraft.getInstance().player.getPosition(0)).inflate((double) size / 2))) {
                PosRecord MOB = MapUtils.getMapPos(entitiesOfClass,data,size,Config.RENDER_MODE.get());
                if(MOB.x()<0 || MOB.y()>=size || MOB.x()>=size || MOB.y()<0) continue;
                Objects.requireNonNull(map.getPixels()).setPixelRGBA(MOB.x(),MOB.y(),-16711936);
            }
        }

        map.upload();
    }

    public static class Maps{
        private static Map<ResourceLocation,DynamicTexture> registeredMaps = new HashMap<>();

        @Nullable
        public static ResourceLocation getMapLocation(int size){
            ResourceLocation resourceLocation = ResourceLocation.tryBuild(SimpleMap.MODID,"map_"+size+"x"+size);
            if(!registeredMaps.containsKey(resourceLocation)){
                DynamicTexture dynamicTexture = new DynamicTexture(size,size,false);
                if (resourceLocation != null) {
                    Minecraft.getInstance().textureManager.register(resourceLocation,dynamicTexture);
                    registeredMaps.put(resourceLocation,dynamicTexture);
                }
            }
            return resourceLocation;
        }

        public static DynamicTexture getMap(ResourceLocation resourceLocation){
            if(registeredMaps.containsKey(resourceLocation)){
                return registeredMaps.get(resourceLocation);
            }
            else{
                int size = Integer.parseInt(resourceLocation.getPath().replace("map_","").split("x")[0]);
                DynamicTexture dynamicTexture = new DynamicTexture(size,size,false);
                Minecraft.getInstance().textureManager.register(resourceLocation,dynamicTexture);
                registeredMaps.put(resourceLocation,dynamicTexture);
                return dynamicTexture;
            }
        }

        public static MapItemSavedData getSaveData(int size,double p_164781_, double p_164782_, byte p_164783_, boolean p_164784_, boolean p_164785_, ResourceKey<Level> p_164786_){
            int r = size/2;
            int $$6 = size * (1 << p_164783_);
            int $$7 = Mth.floor((p_164781_ + r) / (double)$$6);
            int $$8 = Mth.floor((p_164782_ + r) / (double)$$6);
            int $$9 = $$7 * $$6 + $$6 / 2 - r;
            int $$10 = $$8 * $$6 + $$6 / 2 - r;
            return new MapItemSavedData($$9, $$10, p_164783_, p_164784_, p_164785_, false, p_164786_);
        }
    }
}
