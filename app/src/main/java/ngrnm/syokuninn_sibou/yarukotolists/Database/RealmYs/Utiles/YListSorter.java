package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles;

import java.util.LinkedList;

import io.realm.Realm;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YItem;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YLI_Interface;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YList;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YPathTable;

/**
 * Created by ryo on 2018/02/24.
 */

abstract class YListSorter {
    protected LinkedList<Integer> tmpOrderList = new LinkedList<>();
    protected YPathTable yPTable;
    
    
    public YListSorter(YPathTable yPT) {
        mkYListTmp(yPT);
        this.yPTable = yPT;
    }
    protected void mkYListTmp(YPathTable yPTable) {
        tmpOrderList.addAll(yPTable.getChildIDs());
    }
    
    
    public String getLI_title(Realm realm, Integer li_id) {
        YLI_Interface yLI;
        if (yPTable.isItem()) {
            yLI = realm.where(YItem.class).equalTo("id", li_id).findFirst();
        } else {
            yLI = realm.where(YList.class).equalTo("id", li_id).findFirst();
        }
        return yLI.getTitle();
    }
}
