package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.realm.RealmList;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YPathTable;

/**
 * Created by ryo on 2018/02/24.
 */

public class YListOrder_Custom extends YListSorter {
    
    public YListOrder_Custom(YPathTable yPTable) {
        super(yPTable);
    }
    public YListOrder_Custom(YPathTable yPTable, List<Integer> order_custom) {
        super(yPTable);
        setOrderList(order_custom);
    }
    
    
    public void swapOrderList(RealmList<Integer> order_list, int idx_a, int idx_b) {
        Collections.swap(order_list, idx_a, idx_b);
    }
    
    public LinkedList<Integer> getOrderList() {
        return tmpOrderList;
    }
    public void setOrderList(List<Integer> srcO_Custom) {
        tmpOrderList.clear();
        tmpOrderList.addAll(srcO_Custom);
    }
    
    
    public void add(int posi, Integer yLorI) {
        tmpOrderList.add(posi, yLorI);
    }
    public void rm(int posi) {
        tmpOrderList.remove(posi);
    }
    
}
