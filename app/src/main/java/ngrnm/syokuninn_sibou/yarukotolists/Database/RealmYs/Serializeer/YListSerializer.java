package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Serializeer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import ngrnm.syokuninn_sibou.yarukotolists.Database.JSONSerializerLibrary.JsonArrayBuilder;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YList;

/**
 * Created by ryo on 2018/03/04.
 */

public class YListSerializer implements JsonSerializer<YList> {
    
    
    @Override
    public JsonElement serialize(YList src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("title", src.getTitle());
        jsonObject.addProperty("imgName", src.getImgName());
    
        jsonObject.addProperty("orderKind", src.getOrderKind());
        jsonObject.addProperty("orderKindOpt", src.getOrderKindOpt());
        jsonObject.add("order_custom", new JsonArrayBuilder().addAll(src.getOrder_custom()) );
        
        return jsonObject;
    }
    
}
