package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ryo on 2018/03/02.
 */

public class YCtgryOrder extends RealmObject {
    @PrimaryKey
    private int id = 0;
    private RealmList<Integer> yctgOrder = new RealmList<>();
    private RealmList<Integer> yctgOrder_Custom = new RealmList<>();
    private String orderKind = "custom";
    
    public RealmList<Integer> getYO() {
        return yctgOrder;
    }
    public RealmList<Integer> getYO_C() {
        return yctgOrder_Custom;
    }
    
    
    /* JSON 用の Getter */
    public int getId() {return id;}
    public String getOrderKind() {return orderKind;}
    
    public void setId(int id) {
        this.id = id;
    }
    public void setYctgOrder(RealmList<Integer> yctgOrder) {
        this.yctgOrder = yctgOrder;
    }
    public void setYctgOrder_Custom(RealmList<Integer> yctgOrder_Custom) {
        this.yctgOrder_Custom = yctgOrder_Custom;
    }
    
    
    public YCategory getYCtgAtPosiFromYO(Realm realm, int posi) {
        return realm.where(YCategory.class).equalTo("id", getYO().get(posi)).findFirst();
    }
    
    public void initYO_C(Realm realm) {
        RealmResults<YCategory> yctgs = realm.where(YCategory.class).findAll();
        if (yctgs.size() > yctgOrder_Custom.size()) {
            realm.beginTransaction();
            //yctgOrder_Custom.clear();
            for (YCategory yctg : yctgs) {
                if (!yctgOrder_Custom.contains(yctg.getId())) yctgOrder_Custom.add(yctg.getId());
            }
            realm.commitTransaction();
        }
    }
    public void initYO(Realm realm) {
        initYO_C(realm);
        realm.beginTransaction();
        if (orderKind.equals("custom")) {
            yctgOrder.clear();
            yctgOrder.addAll(yctgOrder_Custom);
        }
        realm.commitTransaction();
    }
    
    
    public void setOrderKind(String orderKind) {
        this.orderKind = orderKind;
    }
    
    public void addNewYCtg(Realm realm, Integer new_yctg) {
        realm.beginTransaction();
        yctgOrder_Custom.add(0, new_yctg);
        yctgOrder.add(0, new_yctg);
        realm.commitTransaction();
        yOSort(realm);
    }
    
    public void removeYctg(Realm realm, int posi) {
        YCategory rmYCtg = getYCtgAtPosiFromYO(realm, posi);
        Integer id = rmYCtg.getId();
        
        realm.beginTransaction();
        // Integer を順番と勘違いしてないかを確認する必要あり。
        yctgOrder.remove(id);
        yctgOrder_Custom.remove(id);
        rmYCtg.getHaveList(realm).deleteFromRealm();  //中身を消すのを忘れない！
        rmYCtg.deleteFromRealm();
        realm.commitTransaction();
    }
    
    public void editYctg(Realm realm, int posi, String newTitle) {
        YCategory yctg = getYCtgAtPosiFromYO(realm, posi);
        realm.beginTransaction();
        yctg.setTitle(newTitle);
        // 実装次第、img あたりも追加。
        realm.commitTransaction();
    }
    
    public void ySorter_50(Realm realm) {
        
    }
    
    public void yOSort(Realm realm) {
        switch (orderKind) {
            case "custom":
                initYO(realm);
                break;
            case "50":
                ySorter_50(realm);
                break;
        }
    }
    
    public void moveYCtgry(Realm realm, int before_p, int after_p) {
        /* 50音・日付順をカスタムに反映させる唯一の方法 */
        // 50音・日付にソートした状態で、どれか一つを移動させる。
        realm.beginTransaction();
        if (after_p < before_p) {
            orderKind = "custom";
            
            Integer my = yctgOrder.get(before_p);
            yctgOrder.remove(before_p);
            yctgOrder.add(after_p, my);
        } else if (before_p < after_p) {
            orderKind = "custom";
    
            Integer my = yctgOrder.get(before_p);
            yctgOrder.add(after_p, my);
            yctgOrder.remove(before_p);
        }
        realm.commitTransaction();
    }
    
}
