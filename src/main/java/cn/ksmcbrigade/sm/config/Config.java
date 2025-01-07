package cn.ksmcbrigade.sm.config;

import cn.ksmcbrigade.sm.config.mode.MapMode;
import cn.ksmcbrigade.sm.config.mode.MapPositionMode;
import cn.ksmcbrigade.sm.utils.ClientRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.common.ForgeConfigSpec;

import java.awt.*;

public class Config {
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.EnumValue<MapPositionMode> MAP_POSITION = builder.defineEnum("map_position",MapPositionMode.RIGHT);
    public static final ForgeConfigSpec.BooleanValue DEATH_POS_RECORD = builder.define("death_position_record",true);
    public static final ForgeConfigSpec.BooleanValue SHOW_POSITION = builder.define("show_position",true);
    public static final ForgeConfigSpec.BooleanValue SHOW_BIOME = builder.define("show_biome",true);
    public static final ForgeConfigSpec.BooleanValue SHOW_TIME = builder.define("show_time",false);
    public static final ForgeConfigSpec.BooleanValue SHOW_MOBS = builder.define("show_mobs",true);
    public static final ForgeConfigSpec.BooleanValue SHOW_ANIMALS = builder.define("show_animals",false);
    public static final ForgeConfigSpec.BooleanValue CURRENT_LEVEL_POINTS_ONLY = builder.define("current_level_points_only",true);
    public static final ForgeConfigSpec.IntValue SIZE = builder.defineInRange("map_size",64,16,192);
    public static final ForgeConfigSpec.ConfigValue<Integer> MAP_BACKGROUND = builder.define("map_background_color", Color.WHITE.getRGB());
    public static final ForgeConfigSpec.EnumValue<MapMode> RENDER_MODE = builder.defineEnum("map_mode",MapMode.NORMAL);
    public static final ForgeConfigSpec SPEC = builder.build();

    public static final KeyMapping POINT_SCREEN = ClientRegistry.registerKeyBinding(new KeyMapping("sm.key.point_screen", InputConstants.KEY_F7,KeyMapping.CATEGORY_GAMEPLAY));
    public static final KeyMapping ADD_POINT = ClientRegistry.registerKeyBinding(new KeyMapping("sm.key.add_point", InputConstants.KEY_F8,KeyMapping.CATEGORY_GAMEPLAY));
}
