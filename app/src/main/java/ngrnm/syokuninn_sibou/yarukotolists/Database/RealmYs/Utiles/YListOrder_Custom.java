package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.realm.RealmList;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YItem;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YLI;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YList;

/**
 * Created by ryo on 2018/02/24.
 */

public class YListOrder_Custom extends YListSorter {
    
    public YListOrder_Custom(RealmList<YList> haveLists, RealmList<YItem> haveItems) {
        mkYListTmp(haveLists, haveItems);
    }
    public YListOrder_Custom(RealmList<YLI> order_custom) {
        setOrderList(order_custom);
    }
    
    
    public void swapOrderList(RealmList<YLI> order_list, int idx_a, int idx_b) {
        Collections.swap(order_list, idx_a, idx_b);
    }
    
    public LinkedList<YLI> getOrderList() {
        return tmpOrderList;
    }
    public void setOrderList(List<YLI> srcO_Custom) {
        tmpOrderList.clear();
        tmpOrderList.addAll(srcO_Custom);
    }
    
    
    public void add(int posi, YLI yLorI) {
        tmpOrderList.add(posi, yLorI);
    }
    public void rm(int posi) {
        tmpOrderList.remove(posi);
    }
    
}
