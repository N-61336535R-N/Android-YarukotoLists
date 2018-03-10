package ngrnm.syokuninn_sibou.yarukotolists.YListItem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.disuse.YCategory;
import ngrnm.syokuninn_sibou.yarukotolists.R;

/**
 * Created by ryo on 2017/09/30.
 */


// This adapter is strictly to interface with the GridView and doesn't
// particular show much interesting Realm functionality.

// Alternatively from this example,
// a developer could update the getView() to pull items from the Realm.

public class GridAdapter extends BaseAdapter {
    
    private LayoutInflater inflater;
    private List<YCategory> yctgrys = null;
    private String imgRootDir;
    
    public GridAdapter(Context context, List<YCategory> details, String imgRootDir) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.yctgrys = details;
        this.imgRootDir = imgRootDir;
    }
    
    
    
    @Override
    public int getCount() {
        if (yctgrys == null) return 0;
        else return yctgrys.size();
    }
    
    @Override
    public Object getItem(int position) {
        if (yctgrys == null || yctgrys.get(position) == null) return null;
        else return yctgrys.get(position);
    }
    
    @Override
    public long getItemId(int i) { return i; }
    
    @Override
    public View getView(int position, View currentView, ViewGroup parent) {
        if (currentView == null) {
            currentView = inflater.inflate(R.layout.item_gridview, parent, false);
        }
        
        YCategory ylist = yctgrys.get(position);
        
/*        if (ylist != null) {
            ((TextView) currentView.findViewById(R.id.textview)).setText(ylist.getTitle());
            Bitmap bmp = BitmapFactory.decodeFile(imgRootDir + ylist.getImgName());
            ((ImageView) currentView.findViewById(R.id.imageview)).setImageBitmap(bmp);
        }
*/        
        return currentView;
    }
    
    
}
