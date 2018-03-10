package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ryo on 2018/02/24.
 */

public class YLI_Wrapper {
    private YPathTable yPT;
    private YLI_Interface yLI;
    
    public YLI_Wrapper(Realm realm, int id) {
        RealmResults testList = realm.where(YPathTable.class).findAll();
        yPT = realm.where(YPathTable.class).equalTo("id", id).findFirst();
        if (yPT.isItem()) {
            yLI = realm.where(YItem.class).equalTo("id", id).findFirst();
        } else {
            yLI = realm.where(YList.class).equalTo("id", id).findFirst();
        }
    }
    
    
    public boolean isItem() {
        return yPT.isItem();
    }
    
    public String getLI_title() {
        return yLI.getTitle();
    }
    public String getLI_imgName() {
        return yLI.getImgName();
    }
    
    public void setLI_title(Realm realm, String newTitle) {
        realm.beginTransaction();
        yLI.setTitle(newTitle);
        realm.commitTransaction();
    }
    public void setLI_imgName(Realm realm, String newImgName) {
        realm.beginTransaction();
        yLI.setImgName(newImgName);
        realm.commitTransaction();
    }
}
