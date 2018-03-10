package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Serializeer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import ngrnm.syokuninn_sibou.yarukotolists.Database.JSONSerializerLibrary.JsonArrayBuilder;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YPathTable;

/**
 * Created by ryo on 2018/03/04.
 */

public class YPathTableSerializer implements JsonSerializer<YPathTable> {
    
    
    @Override
    public JsonElement serialize(YPathTable src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("isItem", src.isItem());
        jsonObject.addProperty("parentID", src.getParentID());
        jsonObject.add( "childIDs", new JsonArrayBuilder().addAll(src.getChildIDs()) );
        return jsonObject;
    }
    
}
