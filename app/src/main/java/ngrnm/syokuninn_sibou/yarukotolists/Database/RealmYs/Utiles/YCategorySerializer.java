package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YCategory;

/**
 * Created by ryo on 2018/03/04.
 */

public class YCategorySerializer implements JsonSerializer<YCategory> {
    
    
    @Override
    public JsonElement serialize(YCategory src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("title", src.getTitle());
        jsonObject.addProperty("imgName", src.getImgName());
        jsonObject.addProperty("haveListID", src.getHaveListID());
        return jsonObject;
    }
    
}
