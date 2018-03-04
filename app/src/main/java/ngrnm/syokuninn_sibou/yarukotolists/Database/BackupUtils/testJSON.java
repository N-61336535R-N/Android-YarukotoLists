package ngrnm.syokuninn_sibou.yarukotolists.Database.BackupUtils;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ryo on 2018/03/03.
 */


public class testJSON extends RealmObject {
    @PrimaryKey
    public int id;
    private RealmList<String> testRealmList;
    //private ArrayList<String> testArray;
    
    public testJSON() {
        initList();
    }
    public void initList() {
        testRealmList = new RealmList<>();
    }
    /*
    public ArrayList<String> getTestArray() {
        return testArray;
    }*/
    
    public RealmList<String> getTestRealmList() {
        return testRealmList;
    }
}