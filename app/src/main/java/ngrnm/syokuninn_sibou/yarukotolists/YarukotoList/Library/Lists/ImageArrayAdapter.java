package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Lists;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YItem;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YLI;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YLI_Interface;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YList;
import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;

/*
public class ImageArrayAdapter extends ArrayAdapter<YLI> {
    String imgRootDir = Consts.rootPath + "list_img/";
    
    private int resourceId;
    private List<YLI> items;
    private LayoutInflater inflater;
    
    public ImageArrayAdapter(Context context, int resourceId, List<YLI> items) {
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
        
        YLI item = this.items.get(position);
        
        // テキストをセット
        TextView appInfoText = (TextView)view.findViewById(R.id.item_text);
        appInfoText.setText(item.getLI_Title());
        
        // サムネイルをセット
        ImageView appInfoImage = (ImageView) view.findViewById(R.id.item_image);
        String imgKind = item.getImagePath();
        switch ( imgKind ) {
            case "No_Image":
                // アイコンをセット
                appInfoImage.setImageResource(R.drawable.directory_icon);
                break;
            default:
                // 画像をセット
                appInfoImage.setImageBitmap( BitmapFactory.decodeFile(imgRootDir + imgKind) );
        }
        return view;
    }
}
*/


public class ImageArrayAdapter extends ArrayAdapter<YLI> {
    Realm realm;
    String imgRootDir = Consts.rootPath + "list_img/";
    
    private int resourceId;
    private List<YLI> items;
    private LayoutInflater inflater;
    
    public ImageArrayAdapter(Context context, int resourceId, List<YLI> items, Realm realm) {
        super(context, resourceId, items);
        
        this.resourceId = resourceId;
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        this.realm = realm;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = this.inflater.inflate(this.resourceId, null);
        }
        
        YLI yli_maneger = this.items.get(position);
        YLI_Interface yli;
        if (yli_maneger.isItem()) {
            yli = realm.where(YItem.class).equalTo("id", yli_maneger.getLI_id()).findFirst();
        } else {
            yli = realm.where(YList.class).equalTo("id", yli_maneger.getLI_id()).findFirst();
        }
        
        // テキストをセット
        ( (TextView)view.findViewById(R.id.item_text) ).setText(yli.getTitle());
        
        // サムネイルをセット
        ImageView appInfoImage = (ImageView) view.findViewById(R.id.item_image);
        String imgKind = yli.getImgName();
        switch ( imgKind ) {
            case "No_Image":
                // アイコンをセット
                if (yli_maneger.isItem()) {
                    appInfoImage.setImageResource(R.drawable.txt_icon);
                } else {
                    appInfoImage.setImageResource(R.drawable.directory_icon);
                }
                break;
            default:
                // 画像をセット
                appInfoImage.setImageBitmap( BitmapFactory.decodeFile(imgRootDir + imgKind) );
        }
        return view;
    }
    
    public YLI get(int posi) {
        return items.get(posi);
    }
}

