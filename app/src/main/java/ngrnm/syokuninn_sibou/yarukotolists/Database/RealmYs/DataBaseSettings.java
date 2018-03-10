package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ryo on 2018/03/09.
 */

public class DataBaseSettings extends RealmObject {
    @PrimaryKey
    private int id = 0;
    
    // 復元【入れ替え】系で使う設定変数
    public boolean rollbackFlag = false;
    public String rollbackPattern = null;
    public String destiZipPath = null;
    
}
