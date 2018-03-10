package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Serializeer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YItem;

/**
 * Created by ryo on 2018/03/04.
 */

public class YItemSerializer implements JsonSerializer<YItem> {
    
    
    @Override
    public JsonElement serialize(YItem src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("title", src.getTitle());
        jsonObject.addProperty("imgName", src.getImgName());
    
        jsonObject.addProperty("txtGaiyou", src.getTxtGaiyou());
        jsonObject.addProperty("txtSyousai", src.getTxtSyousai());
        
        return jsonObject;
    }
    
}
