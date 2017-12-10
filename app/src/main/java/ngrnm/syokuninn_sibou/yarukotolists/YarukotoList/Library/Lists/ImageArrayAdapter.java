package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Lists;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ngrnm.syokuninn_sibou.yarukotolists.R;



public class ImageArrayAdapter extends ArrayAdapter<ViewData> {
    
    private int resourceId;
    private List<ViewData> items;
    private LayoutInflater inflater;
    
    public ImageArrayAdapter(Context context, int resourceId, List<ViewData> items) {
        super(context, resourceId, items);
        
        this.resourceId = resourceId;
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = this.inflater.inflate(this.resourceId, null);
        }
        
        ViewData item = this.items.get(position);
        
        // テキストをセット
        TextView appInfoText = (TextView)view.findViewById(R.id.item_text);
        appInfoText.setText(item.getTitle());
        
        // サムネイルをセット
        ImageView appInfoImage = (ImageView) view.findViewById(R.id.item_image);
        String imgPath = item.getImagePath();
        switch ( imgPath ) {
            case "No_Image":
                // アイコンをセット
                appInfoImage.setImageResource(R.drawable.directory_icon);
                break;
            default:
                // 画像をセット
                appInfoImage.setImageBitmap( BitmapFactory.decodeFile(imgPath) );
        }
        return view;
    }
}
