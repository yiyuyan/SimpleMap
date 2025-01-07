package cn.ksmcbrigade.sm.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class LevelUtils {

    private static Minecraft MC = Minecraft.getInstance();

    public static void init(){
        if(MC==null){
            MC = Minecraft.getInstance();
        }
    }

    public static String time(){
        init();
        long time = MC.player.level().getDayTime();
        if (time > 24000)
        {
            time %= 24000;
        }

        int m = (int) (((time % 1000) / 1000f) * 60);
        int h = (int) time / 1000 + 6;
        if (h >= 24)
        {
            h -= 24;
        }

        return ((h < 10) ? "0" + h : h) + ":" + ((m < 10)?"0"+m:m);
    }

    public static ResourceKey<Level> getLevel(){
        init();
        return MC.level.dimension();
    }
}
