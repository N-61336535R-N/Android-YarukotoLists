package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * Created by M.R on 2017/06/04.
 */

public class DataSet {
    private AssetManager assetManager;
    private Context context;
    
    public DataSet(AssetManager aM, Context ct) {
        assetManager = aM;
        context = ct;
    }
    
    
    public AssetManager getAssetManager() {
        return assetManager;
    }
    public Context getContext() {
        return context;
    }
}
