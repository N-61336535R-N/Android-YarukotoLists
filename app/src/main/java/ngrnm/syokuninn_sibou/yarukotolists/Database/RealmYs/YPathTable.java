package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ryo on 2018/03/08.
 */

public class YPathTable extends RealmObject {
    @PrimaryKey
    private int id;
    private boolean isItem;
    private int parentID;
    private RealmList<Integer> childIDs = new RealmList<>();  //Item の場合は使われない
    
    
    public YPathTable() {}
    public YPathTable(boolean isItem, int parentID) {
        this.isItem = isItem;
        this.parentID = parentID;
        this.setId();
    }
    public YPathTable(boolean isItem, int parentID, int id) {
        this(isItem, parentID);
        setId(id);
    }
    
    
    public int getId() {
        return id;
    }
    private boolean isNotExistingId(Realm realm, int candidate) {
        return realm.where(YPathTable.class).equalTo("id", candidate).findAll().size() == 0;
    }
    public void setId(int id) {
        Realm realm = Realm.getDefaultInstance();
        if (!isNotExistingId(realm, id)) throw new IllegalArgumentException("そのidはすでに存在しています。");
        this.id = id;
        realm.close();
    }
    public void setId() {
        // このスレッドのためのRealmインスタンスを取得
        Realm realm = Realm.getDefaultInstance();
        while (true) {
            int i = (int) UUID.randomUUID().getMostSignificantBits();
            if (isNotExistingId(realm, i)) {
                this.id = i;
                break;
            }
        }
        realm.close();
    }
    public boolean isItem() {
        return isItem;
    }
    public void setItem(boolean item) {
        isItem = item;
    }
    public int getParentID() {
        return parentID;
    }
    public void setParentID(int parentID) {
        this.parentID = parentID;
    }
    public RealmList<Integer> getChildIDs() {
        return childIDs;
    }
    public void setChildIDs(RealmList<Integer> childIDs) {
        this.childIDs = childIDs;
    }
    
    
    public YList getYListById(Realm realm, int search_id) {
        return realm.where(YList.class).equalTo("id", search_id).findFirst();
    }
    public YItem getYItemById(Realm realm, int search_id) {
        return realm.where(YItem.class).equalTo("id", search_id).findFirst();
    }
    
    
    public void del_YLI(Realm realm) {
        // id が示すデータの痕跡を全て消す。一つ残らず。
        // 実行元は削除される実体。
        //System.out.println(toString());
        //System.out.println("rm_id : "+id);
    
        if (isItem) {
            // データ本体（自分を削除）
            YItem rmYI = realm.where(YItem.class).equalTo("id", id).findFirst();
            if (rmYI != null)  rmYI.deleteFromRealm();
        } else {
            // 子も全て削除
            for (Integer childLI_id : childIDs) {
                YPathTable rmYPT = realm.where(YPathTable.class).equalTo("id", childLI_id).findFirst();
                rmYPT.del_YLI(realm);  //再帰呼出し。
            }
            
            // データ本体（自分を削除）
            YList rmYL = realm.where(YList.class).equalTo("id", id).findFirst();
            if (rmYL != null)  rmYL.deleteFromRealm();
        }
    }
    
    
    @Override
    public String toString() {
        String message
                = "id : "+id+
                "\nisItem : "+isItem+
                "\nparentID : "+parentID+
                "\nchildIDs : ";
        String childList = toStringChildList();
        return message + childList;
    }
    private String toStringChildList() {
        StringBuilder sb = new StringBuilder("{ ");
        for (int childID : childIDs) {
            sb.append(childID).append(", ");
        }
        sb.append("}");
    
        return sb.toString();
    }
}
