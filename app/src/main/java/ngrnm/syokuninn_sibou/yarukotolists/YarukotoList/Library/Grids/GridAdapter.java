package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Grids;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Lists.ViewData;

/**
 * Created by ryo on 2017/09/30.
 */


// ArrayAdapter<ViewData> を継承した GridAdapter クラスのインスタンス生成
public class GridAdapter extends ArrayAdapter<ViewData> {
    
    /*  GridView の生成の諸々  */
    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
    
    
    private LayoutInflater inflater;
    private int layoutId;
    
    public GridAdapter(Context context, int layoutId, ViewData[] objects) {
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
