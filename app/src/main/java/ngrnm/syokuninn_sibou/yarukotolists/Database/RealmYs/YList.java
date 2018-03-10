package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles.YListOrder_Custom;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles.YListSorter_Mac;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles.YListSorter_Win;


/**
 * Created by ryo on 2018/02/21.
 */
public class YList extends RealmObject implements YLI_Interface {
    @PrimaryKey
    private int id=0;
    private String title;  //List名
    private String imgName;  //画像の名前
    
    //Viewを作る際に読まれるリスト。順番を考慮。最低限の情報に絞ったYLIをリストアップ。
    private String orderKind = "custom";
    private String orderKindOpt = "win";
    private RealmList<Integer> order_list = new RealmList<>();
    private RealmList<Integer> order_custom = new RealmList<>();  //カスタム並び順のみ別に用意する方向で！
    
    @Ignore
    private YListSorter_Mac ySortMac;
    @Ignore
    private YListSorter_Win ySortWin;
    @Ignore
    private YListOrder_Custom yOrderCustom;  //カスタム並び順のみ別に用意する方向で！
    
    
    public YList(String title, int id) {
        this.title = title;
        this.imgName = "No_Image";
        this.id = id;
    }
    //作らないとエラーになる。要素はコピーされるっぽいので、空でOK
    public YList() {}
    
    
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getImgName() {
        return imgName;
    }
    
    public String getOrderKind() {
        return orderKind;
    }
    public String getOrderKindOpt() {
        return orderKindOpt;
    }
    public RealmList<Integer> getOrder_custom() {
        return order_custom;
    }
    
    // change
    public void setTitle(String title) {
        this.title = title;
    }
    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
    
    public void setOrderKind(String orderKind) {
        this.orderKind = orderKind;
    }
    public void setOrderKindOpt(String orderKindOpt) {
        this.orderKindOpt = orderKindOpt;
    }
    
    
    /**
     * Sort系のメソッド
     * 
     * [基本]
     *      ● initOrder で1度だけ初期化
     *      ● View に渡すのは、order_list。
     *          Custom を渡したい場合も、一旦 order_list をcustomで上書きして、order_list の方を渡す。
     *          実際の操作は sortCustom() の呼び出しだけでOK
     *      ●
     */
    private YPathTable getYPTable(Realm realm) {
        //RealmResults test = realm.where(YPathTable.class).findAll();
        return realm.where(YPathTable.class).equalTo("id", this.id).findFirst();
    }
    
    /* orderの初期化 */
    public void initOrder(Realm realm) {
        initCustom(realm);
        if (order_list.size() == 0) {
            sortO(realm);
        }
    }
    private void initCustom(Realm realm) {
        if (yOrderCustom == null) {
            YPathTable yPTable = getYPTable(realm);
            
            realm.beginTransaction();
            if (order_custom.size() == 0) {
                yOrderCustom = new YListOrder_Custom(yPTable);
            } else {
                if (order_custom.size() == yPTable.getChildIDs().size()) {
                    yOrderCustom = new YListOrder_Custom(yPTable, order_custom);
                } else throw new IllegalStateException("custom の要素数が不正です。");
            }
            realm.commitTransaction();
        }
    }
    
    /* order_custom, order_list を更新するためのメソッド */
    private void updateO_list(Realm realm, List<Integer> targetList, List<Integer> upsetList) {
        realm.beginTransaction();
        targetList.clear();
        targetList.addAll(upsetList);
        realm.commitTransaction();
    }
    public void add_yLI(Realm realm, int posi, Integer add_id) {
        // custom を同期
        initCustom(realm);
        // YLI_Wrapper に変換
        addC(realm, posi, add_id);
        YPathTable yPTable = getYPTable(realm);
        realm.executeTransaction(realm1 -> yPTable.getChildIDs().add(0, add_id));
        
        sortO(realm);
    }
    public void rm_LI(Realm realm, final int posi) {
        // custom を同期
        initCustom(realm);
        rmC(realm, posi);
        
        // 子List も辿って全て削除
        YPathTable yPTable = getYPTable(realm);
        Integer yID = order_list.get(posi);
        YPathTable rmYPT = realm.where(YPathTable.class).equalTo("id", yID).findFirst();
        realm.executeTransaction(realm1 -> {
            // 削除対象のpathTable内の del を呼び出す。
            rmYPT.del_YLI(realm);
            yPTable.getChildIDs().remove(yID);
        });
        
        sortO(realm);
    }
    
    
    /* Custom */
    private void addC(Realm realm, int posi, Integer yID) {
        initCustom(realm);
        yOrderCustom.add(posi, yID);
    }
    private void rmC(Realm realm, int posi) {
        initCustom(realm);
        yOrderCustom.rm(posi);
    }
    public void swapC(Realm realm, int idx_a, int idx_b) {
        initCustom(realm);
        yOrderCustom.swapOrderList(order_list, idx_a, idx_b);
        order_list = new RealmList<>();
        updateO_list(realm, order_list, yOrderCustom.getOrderList());
    }
    public void moveC(Realm realm, int before_p, int after_p) {
        /* 50音・日付順をカスタムに反映させる唯一の方法 */
        // 50音・日付にソートした状態で、どれか一つを移動させる。
        realm.beginTransaction();
        if (after_p != before_p) {
            orderKind = "custom";
            Integer li_id = order_list.get(before_p);
            if (after_p < before_p) {
                order_list.remove(before_p);
                order_list.add(after_p, li_id);
                updateO_list(realm, order_custom, order_list);
            } else if (before_p < after_p) {
                order_list.add(after_p, li_id);
                order_list.remove(before_p);
                updateO_list(realm, order_custom, order_list);
            }
        }
        realm.commitTransaction();
    }
    
    
    /* その他 */
    public void sortO(Realm realm) {
        if (orderKind.equals("custom")) {
            // custom に入れ替え
            initCustom(realm);
            if (order_custom.size() != yOrderCustom.getOrderList().size()) {
                updateO_list(realm, order_custom, yOrderCustom.getOrderList());
            }
            updateO_list(realm, order_list, order_custom);
        } else {
            YPathTable yPTable = getYPTable(realm);
            switch (orderKindOpt) {
                case "win": switch (orderKind) {
                    case "50":
                        ySortWin = new YListSorter_Win(yPTable);
                        updateO_list(realm, order_list, ySortWin.sort_win50onn(realm));  // transaction 含む。
                        break;
                    case "date":
                        
                        break;
                    }
                    break;
                case "mac": switch (orderKind) {
                    case "50":
                        ySortMac = new YListSorter_Mac(yPTable);
                        updateO_list(realm, order_list, ySortMac.sort_mac50onn(realm));  // transaction 含む。
                        break;
                    case "date":
    
                        break;
                    }
                default:
                    throw new IllegalArgumentException("その Sort パターンは未実装です。");
            }
        }
    }
    
    
    public RealmList<Integer> getO_list() {
        return order_list;
    }
    
}
