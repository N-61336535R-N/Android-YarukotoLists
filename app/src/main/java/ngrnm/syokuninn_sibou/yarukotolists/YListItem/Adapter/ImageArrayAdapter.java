package ngrnm.syokuninn_sibou.yarukotolists.YListItem.Adapter;

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
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YLI_Wrapper;
import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;

/*
public class ImageArrayAdapter extends ArrayAdapter<Integer> {
    String imgRootDir = Consts.rootPath + "list_img/";
    
    private int resourceId;
    private List<Integer> LI_IDs;
    private LayoutInflater inflater;
    
    public ImageArrayAdapter(Context context, int resourceId, List<Integer> LI_IDs) {
        super(context, resourceId, LI_IDs);
        
        this.resourceId = resourceId;
        this.LI_IDs = LI_IDs;
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
        
        Integer item = this.LI_IDs.get(position);
        
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


public class ImageArrayAdapter extends ArrayAdapter<Integer> {
    Realm realm;
    String imgRootDir = Consts.rootPath + "list_img/";
    
    private int resourceId;
    private List<Integer> LI_IDs;
    private LayoutInflater inflater;
    
    public ImageArrayAdapter(Context context, int resourceId, List<Integer> LI_IDs, Realm realm) {
        super(context, resourceId, LI_IDs);
        
        this.resourceId = resourceId;
        this.LI_IDs = LI_IDs;
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
        
        YLI_Wrapper yli = new YLI_Wrapper(realm, LI_IDs.get(position));
        
        // テキストをセット
        ( (TextView)view.findViewById(R.id.item_text) ).setText(yli.getLI_title());
        
        // サムネイルをセット
        ImageView appInfoImage = (ImageView) view.findViewById(R.id.item_image);
        String imgKind = yli.getLI_imgName();
        switch ( imgKind ) {
            case "No_Image":
                // アイコンをセット
                if (yli.isItem()) {
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
    
    public Integer get(int posi) {
        return LI_IDs.get(posi);
    }
}

