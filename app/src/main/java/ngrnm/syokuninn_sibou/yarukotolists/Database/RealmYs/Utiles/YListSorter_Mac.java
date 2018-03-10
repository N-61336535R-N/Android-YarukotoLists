package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles;

import java.util.Collections;
import java.util.LinkedList;

import io.realm.Realm;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YPathTable;

/**
 * Created by ryo on 2018/02/24.
 */

public class YListSorter_Mac extends YListSorter {
    
    public YListSorter_Mac(YPathTable yPT) {
        super(yPT);
    }
    
    public LinkedList<Integer> sort_mac50onn(final Realm realm) {
        Collections.sort(tmpOrderList, (id1, id2) -> getLI_title(realm,id1).compareTo( getLI_title(realm,id2) ) );
        return tmpOrderList;
    }
    
}
