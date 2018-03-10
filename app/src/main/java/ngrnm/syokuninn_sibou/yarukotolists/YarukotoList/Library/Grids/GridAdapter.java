package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Grids;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.disuse.YCategory;
import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Lists.ViewData;

/**
 * Created by ryo on 2017/09/30.
 */


// ArrayAdapter<ViewData> を継承した GridAdapter クラスのインスタンス生成
class GridAdapter_org extends ArrayAdapter<ViewData> {
    
    /*  GridView の生成の諸々  */
    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
    
    
    private LayoutInflater inflater;
    private int layoutId;
    
    public GridAdapter_org(Context context, int layoutId, ViewData[] objects) {
        super(context, 0, objects);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            // main.xml の <GridView .../> に grid_items.xml を inflate して convertView とする
            convertView = inflater.inflate(layoutId, parent, false);
            // ViewHolder を生成
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textview);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        ViewData data = getItem(position);
        holder.textView.setText(data.getTitle());
        Bitmap bmp = BitmapFactory.decodeFile(data.getImagePath());
        holder.imageView.setImageBitmap(bmp);
        
        return convertView;
    }
}










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
            currentView = inflater.inflate(R.layout.grid_items, parent, false);
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
