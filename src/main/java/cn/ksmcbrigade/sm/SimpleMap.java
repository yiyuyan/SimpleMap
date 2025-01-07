package cn.ksmcbrigade.sm;

import cn.ksmcbrigade.sm.config.Config;
import cn.ksmcbrigade.sm.config.WorldPointConfigs;
import cn.ksmcbrigade.sm.config.mode.PointAction;
import cn.ksmcbrigade.sm.screen.PointsScreen;
import cn.ksmcbrigade.sm.screen.edit.EditPointScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SimpleMap.MODID)
@Mod.EventBusSubscriber(modid = SimpleMap.MODID,value = Dist.CLIENT)
public class SimpleMap {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "sm";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final WorldPointConfigs configs;

    static {
        try {
            configs = new WorldPointConfigs(new File("config/sm-points-config.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SimpleMap() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        LOGGER.info("SimpleMap mod loaded.");
    }

    @SubscribeEvent
    public static void key(InputEvent.Key event) throws IOException {
        if(Config.POINT_SCREEN.isDown()){
            Minecraft.getInstance().setScreen(new PointsScreen());
        }
        if(Config.ADD_POINT.isDown()){
            Minecraft.getInstance().setScreen(new EditPointScreen(null,new WorldPointConfigs.PointConfig("Point#"+ (configs.points()+1),Minecraft.getInstance().level.dimension().location(),Minecraft.getInstance().player.getPosition(0)), PointAction.ADD,false));
        }
    }
}
