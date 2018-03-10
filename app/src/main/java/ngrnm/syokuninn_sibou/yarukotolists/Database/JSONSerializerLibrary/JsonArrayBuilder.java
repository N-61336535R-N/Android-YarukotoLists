package ngrnm.syokuninn_sibou.yarukotolists.Database.JSONSerializerLibrary;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import java.util.List;

/**
 * Created by ryo on 2018/03/04.
 */

public class JsonArrayBuilder {
    private JsonArray jsonArray = new JsonArray();
    
    public JsonArray addAll(List<Integer> cList) {
        for (int item : cList) {
            jsonArray.add(new JsonPrimitive(item));
        }
        return jsonArray;
    }
}
