package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;

/**
 * Created by ryo on 2018/02/21.
 */

public class YCategory extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;  //Category名
    private String imgName;  // + 画像の名前
    private int haveListID;
    
    public YCategory(String title) {
        setId();
        this.title = title;
        imgName = "No_Image.png";
    }
    //作らないとエラーになる。要素はコピーされるっぽいので、空でOK
    public YCategory() {}
    
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getImgName() {
        return imgName;
    }
    public int getHaveListID() {
        return haveListID;
    }
    
    public void setId() {
        // このスレッドのためのRealmインスタンスを取得
        Realm realm = Realm.getDefaultInstance();
        for (int i=1; i<= Consts.LIMIT_Category; i++) {
            if (realm.where(YCategory.class).equalTo("id", i).findAll().size() == 0) {
                this.id = i;
                break;
            }
        }
        realm.close();
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setHaveListID(int haveListID) {
        this.haveListID = haveListID;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
    
    
    public YList getHaveList(Realm realm) {
        return realm.where(YList.class).equalTo("id", haveListID).findFirst();
    }
}
