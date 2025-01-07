package cn.ksmcbrigade.sm.config;

import cn.ksmcbrigade.sm.SimpleMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorldPointConfigs {

    public final File config;

    public Map<String,PointConfigs> pointConfigsMap = new HashMap<>();

    public WorldPointConfigs(File config) throws IOException {
        this.config = config;
        this.save(false);
        this.load();
    }

    public void save(boolean exists) throws IOException {
        if(!config.exists() || exists){
            FileUtils.write(config,get().toString());
        }
    }

    public void load() throws IOException {
        JsonArray array = JsonParser.parseString(FileUtils.readFileToString(config)).getAsJsonArray();
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            PointConfigs pointConfigs = PointConfigs.parse(object);
            pointConfigsMap.put(pointConfigs.name,pointConfigs);
        }
    }

    public void add(PointConfig config) throws IOException {
        this.add(levelName(),config);
    }

    public void add(String level,PointConfig config) throws IOException {
        this.getOrCreatePointConfigs(level).configs.add(config);
        save(true);
    }

    public boolean replace(PointConfig or,PointConfig target) {
        return this.replace(levelName(),or,target);
    }

    public boolean replace(String level,PointConfig or,PointConfig target) {
        try {
            this.getOrCreatePointConfigs(level).configs.set(this.getOrCreatePointConfigs(level).configs.indexOf(or),target);
            save(true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int points(String level) throws IOException {
        return this.getOrCreatePointConfigs(level).configs.size();
    }

    public int points() throws IOException {
        return points(SimpleMap.configs.levelName());
    }
    
    public String levelName(){
        return (Minecraft.getInstance().hasSingleplayerServer()?Minecraft.getInstance().getSingleplayerServer().getWorldData().getLevelName(): Minecraft.getInstance().getCurrentServer().ip);
    }

    public JsonArray get(){
        JsonArray array = new JsonArray();
        for (String s : pointConfigsMap.keySet()) {
            array.add(pointConfigsMap.get(s).get());
        }
        return array;
    }

    public PointConfigs getOrCreatePointConfigs(String level) throws IOException {
        if(pointConfigsMap.containsKey(level)) {
            return pointConfigsMap.get(level);
        }
        else{
            PointConfigs pointConfigs = new PointConfigs(level);
            pointConfigsMap.put(level,pointConfigs);
            save(true);
            return pointConfigs;
        }
    }

    public static class PointConfigs{
        private final String name;
        public ArrayList<PointConfig> configs = new ArrayList<>();

        public PointConfigs(String name){
            this.name = name;
        }

        public PointConfigs(String name,ArrayList<PointConfig> configs){
            this.name = name;
            this.configs = configs;
        }

        public static PointConfigs parse(JsonObject object){
            JsonArray array = object.getAsJsonArray("points");
            ArrayList<PointConfig> configArrayList = new ArrayList<>();
            for (JsonElement element : array) {
                configArrayList.add(PointConfig.parse(element.getAsJsonObject()));
            }
            return new PointConfigs(object.get("name").getAsString(),configArrayList);
        }

        public JsonObject get(){
            JsonObject object = new JsonObject();
            JsonArray array = new JsonArray();
            for (PointConfig pointConfig : configs) {
                array.add(pointConfig.get());
            }
            object.addProperty("name",name);
            object.add("points",array);
            return object;
        }
    }

    public record PointConfig(String name,ResourceLocation levelResourceLocation, Vec3 pos){
        public JsonObject get(){
            JsonObject object = new JsonObject();
            object.addProperty("name",name);
            object.addProperty("level",levelResourceLocation.toString());
            object.addProperty("pos",pos.x+";"+pos.y+";"+pos.z);
            return object;
        }

        public String command(){
            return "execute in "+levelResourceLocation + " run tp " + pos.toString().replace("(","").replace(")","").replace(",","");
        }

        public String gotoCommand(){
            return "#goto "+pos.toString().replace("(","").replace(")","").replace(",","");
        }

        public static PointConfig parse(JsonObject object){
            ResourceLocation location = ResourceLocation.tryParse(object.get("level").getAsString());
            String[] pos = object.get("pos").getAsString().split(";");
            return new PointConfig(object.get("name").getAsString(),location,new Vec3(Double.parseDouble(pos[0]),Double.parseDouble(pos[1]),Double.parseDouble(pos[2])));
        }
    }
}
