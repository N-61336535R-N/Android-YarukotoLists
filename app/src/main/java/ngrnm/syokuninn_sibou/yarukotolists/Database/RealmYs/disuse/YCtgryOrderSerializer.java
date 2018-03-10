package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.disuse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by ryo on 2018/03/04.
 */

public class YCtgryOrderSerializer implements JsonSerializer<YCtgryOrder> {
    
    
    @Override
    public JsonElement serialize(YCtgryOrder src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        /*jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("orderKind", src.getOrderKind());
        jsonObject.add("yctgOrder", new JsonArrayBuilder().addAll(src.getYO()));
        jsonObject.add("ListRoot", new JsonArrayBuilder().addAll(src.getYO_C()));*/
        return jsonObject;
    }
    
}
