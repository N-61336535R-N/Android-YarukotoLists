package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ryo on 2018/02/21.
 */

public class YItem extends RealmObject implements YLI_Interface {
    @PrimaryKey
    private int id;
    private String title;  //Category名
    private String txtGaiyou;  //やることの概要
    private String txtSyousai;  //やることの詳細
    private String imgName;  //画像の名前
    
    public YItem(String title, int id) {
        this.title = title;
        this.imgName = "No_Image";
        this.id = id;
    }
    //作らないとエラーになる。要素はコピーされるっぽいので、空でOK
    public YItem() {}
    
    
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getTxtGaiyou() {
        return txtGaiyou;
    }
    public String getTxtSyousai() {
        return txtSyousai;
    }
    public String getImgName() {
        return imgName;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public void setTxtGaiyou(String txtGaiyou) {
        this.txtGaiyou = txtGaiyou;
    }
    public void setTxtSyousai(String txtSyousai) {
        this.txtSyousai = txtSyousai;
    }
    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
    
    
}
