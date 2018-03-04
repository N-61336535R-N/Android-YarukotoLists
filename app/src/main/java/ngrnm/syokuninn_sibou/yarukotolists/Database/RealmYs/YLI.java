package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by ryo on 2018/02/24.
 */

public class YLI extends RealmObject {
    private boolean isItem;  // Item なら True
    //private String LI_title;
    //private String LI_imgName;
    private int LI_id;
    
    public YLI(boolean isItem, int id) {
        this.isItem = isItem;
//        this.LI_title = title;
//        this.LI_imgName = imgName;
        this.LI_id = id;
    }
    //作らないとエラーになる。要素はコピーされるっぽいので、空でOK
    public YLI() {}
    
    
    
    public boolean isItem() {
        return isItem;
    }
/*    public void setItem(boolean item) {
        isItem = item;
    }*/
    
    public int getLI_id() {
        return LI_id;
    }
    
    public String getLI_title(Realm realm) {
        if (isItem()) {
            return realm.where(YItem.class).equalTo("id", LI_id).findFirst().getTitle();
        } else {
            return realm.where(YList.class).equalTo("id", LI_id).findFirst().getTitle();
        }
    }
    public String getLI_imgName(Realm realm) {
        if (isItem()) {
            return realm.where(YItem.class).equalTo("id", LI_id).findFirst().getImgName();
        } else {
            return realm.where(YList.class).equalTo("id", LI_id).findFirst().getImgName();
        }
    }
    
    
    public void setLI_title(Realm realm, String newTitle) {
        realm.beginTransaction();
        if (isItem()) {
            realm.where(YItem.class).equalTo("id", LI_id).findFirst().setTitle(newTitle);
        }else {
            realm.where(YList.class).equalTo("id", LI_id).findFirst().setTitle(newTitle);
        }
        realm.commitTransaction();
    }
    public void setLI_imgName(Realm realm, String newImgName) {
        realm.beginTransaction();
        if (isItem()) {
            realm.where(YItem.class).equalTo("id", LI_id).findFirst().setImgName(newImgName);
        }else {
            realm.where(YList.class).equalTo("id", LI_id).findFirst().setImgName(newImgName);
        }
        realm.commitTransaction();
    }
}
