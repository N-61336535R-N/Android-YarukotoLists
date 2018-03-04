package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles;

import java.util.LinkedList;

import io.realm.RealmList;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YItem;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YLI;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YList;

/**
 * Created by ryo on 2018/02/24.
 */

abstract class YListSorter {
    protected LinkedList<YLI> tmpOrderList = new LinkedList<>();
    
    protected void mkYListTmp(RealmList<YList> haveLists, RealmList<YItem> haveItems) {
        for (YList yL : haveLists) {
            tmpOrderList.add(new YLI(false, yL.getId()));
        }
        for (YItem yI : haveItems) {
            tmpOrderList.add(new YLI(true, yI.getId()));
        }
    }
    
}
