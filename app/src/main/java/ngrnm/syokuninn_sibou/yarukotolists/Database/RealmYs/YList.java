package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs;

import java.util.List;
import java.util.UUID;

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
    
    private RealmList<YList> haveLists = new RealmList<>();
    private RealmList<YItem> haveItems = new RealmList<>();
    
    //Viewを作る際に読まれるリスト。順番を考慮。最低限の情報に絞ったYLIをリストアップ。
    private String orderKind = "custom";
    private String orderKindOpt = "win";
    private RealmList<YLI> order_list = new RealmList<>();
    private RealmList<YLI> order_custom = new RealmList<>();  //カスタム並び順のみ別に用意する方向で！
    
    @Ignore
    private YListSorter_Mac ySortMac;
    @Ignore
    private YListSorter_Win ySortWin;
    @Ignore
    private YListOrder_Custom yOrderCustom;  //カスタム並び順のみ別に用意する方向で！
    
    
    public YList(String title) {
        this.title = title;
        imgName = "No_Image";
        setId();
    }
    //作らないとエラーになる。要素はコピーされるっぽいので、空でOK
    public YList() {}
    
    
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public RealmList<YList> getLists() {
        return haveLists;
    }
    public RealmList<YItem> getItems() {
        return haveItems;
    }
    public String getImgName() {
        return imgName;
    }
    
    public void setId() {
        // このスレッドのためのRealmインスタンスを取得
        Realm realm = Realm.getDefaultInstance();
        while (true) {
            int i = (int) UUID.randomUUID().getMostSignificantBits();
            if (realm.where(YList.class).equalTo("id", i).findAll().size() == 0) {
                this.id = i;
                break;
            }
        }
        realm.close();
    }
    // change
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setImgName(String imgName) {
        this.imgName = imgName;
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
    public String getOrderKind() {
        return orderKind;
    }
    public void setOrderKind(String orderKind) {
        this.orderKind = orderKind;
    }
    public String getOrderKindOpt() {
        return orderKindOpt;
    }
    public void setOrderKindOpt(String orderKindOpt) {
        this.orderKindOpt = orderKindOpt;
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
            realm.beginTransaction();
            if (order_custom.size() == 0) {
                yOrderCustom = new YListOrder_Custom(haveLists, haveItems);
            } else {
                if (order_custom.size() == (haveItems.size() + haveLists.size())) {
                    yOrderCustom = new YListOrder_Custom(order_custom);
                } else throw new IllegalStateException("custom の要素数が不正です。");
            }
            realm.commitTransaction();
        }
    }
    
    /* order_custom, order_list を更新するためのメソッド */
    private void updateO_list(Realm realm, List<YLI> oList, List<YLI> replaceList) {
        realm.beginTransaction();
        oList.clear();
        oList.addAll(replaceList);
        realm.commitTransaction();
    }
    public void add_List(Realm realm, int posi, final YList yL) {
        // custom を同期
        initCustom(realm);
        // YLI に変換
        YLI yLorI = new YLI(false, yL.getId());
        addC(realm, posi, yLorI);
        realm.executeTransaction(realm1 -> haveLists.add(0, yL));
        
        sortO(realm);
    }
    public void add_Item(Realm realm, int posi, final YItem yI) {
        // custom を同期
        initCustom(realm);
        // YLI に変換
        YLI yLorI = new YLI(true,  yI.getId());
        addC(realm, posi, yLorI);
        realm.executeTransaction(realm1 -> haveItems.add(0, yI));
        
        sortO(realm);
    }
    public void rm_LI(Realm realm, final int posi) {
        // custom を同期
        initCustom(realm);
        // YLI に変換
        rmC(realm, posi);
        YLI yli = order_list.get(posi);
        if (yli.isItem()) {
            YItem rmYI = realm.where(YItem.class).equalTo("id", yli.getLI_id()).findFirst();
            realm.executeTransaction(realm1 -> {
                haveItems.remove(rmYI);
                if (rmYI != null)  rmYI.deleteFromRealm();
            });
        } else {
            YList rmYL = realm.where(YList.class).equalTo("id", yli.getLI_id()).findFirst();
            realm.executeTransaction(realm1 -> {
                haveLists.remove(rmYL);
                if (rmYL != null)  rmYL.deleteFromRealm();
            });
        }
        sortO(realm);
    }
    
    
    /* Custom */
    private void addC(Realm realm, int posi, YLI yLorI) {
        initCustom(realm);
        yOrderCustom.add(posi, yLorI);
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
            YLI mli = order_list.get(before_p);
            if (after_p < before_p) {
                order_list.remove(before_p);
                order_list.add(after_p, mli);
                updateO_list(realm, order_custom, order_list);
            } else if (before_p < after_p) {
                order_list.add(after_p, mli);
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
            switch (orderKindOpt) {
                case "win": switch (orderKind) {
                    case "50":
                        ySortWin = new YListSorter_Win(haveLists, haveItems);
                        updateO_list(realm, order_list, ySortWin.sort_win50onn(realm));  // transaction 含む。
                        break;
                    case "date":
                        
                        break;
                    }
                    break;
                case "mac": switch (orderKind) {
                    case "50":
                        ySortMac = new YListSorter_Mac(haveLists, haveItems);
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
    
    
    public RealmList<YLI> getO_list() {
        return order_list;
    }
    
}
