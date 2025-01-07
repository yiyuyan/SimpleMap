package cn.ksmcbrigade.sm.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class PlayerPosUtils {

    private static Minecraft MC = Minecraft.getInstance();

    public static void init(){
        if(MC==null){
            MC = Minecraft.getInstance();
        }
    }

    public static BlockPos getOnPos(){
        init();
        return MC.player.getOnPos();
    }

    public static Vec3 pos(){
        init();
        return MC.player.position();
    }

    public static String stringPos(){
        init();
        Vec3 pos = pos();
        return String.format("%.0f", pos.x) + " " + String.format("%.0f", pos.y) + " " + String.format("%.0f", pos.z);
    }

    public static String biome(){
        init();
        ResourceLocation location = MC.level.registryAccess().registry(Registries.BIOME).get().getKey(MC.level.getBiomeManager().getBiome(MC.player.blockPosition()).get());
        return I18n.get("biome."+location.getNamespace()+"."+location.getPath());
    }
}
