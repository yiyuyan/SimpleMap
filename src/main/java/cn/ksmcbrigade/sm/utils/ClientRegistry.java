package cn.ksmcbrigade.sm.utils;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRegistry {

    private static Map<Class<? extends Entity>, ResourceLocation> entityShaderMap = new ConcurrentHashMap<>();

    public static synchronized void registerKeyBinding(KeyMapping... keys) {
        for (KeyMapping key : keys) {
            Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, key);
        }
    }

    public static KeyMapping registerKeyBinding(KeyMapping key) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, key);
        return key;
    }

    public static void registerEntityShader(Class<? extends Entity> entityClass, ResourceLocation shader) {
        entityShaderMap.put(entityClass, shader);
    }

    public static ResourceLocation getEntityShader(Class<? extends Entity> entityClass) {
        return entityShaderMap.get(entityClass);
    }
}
