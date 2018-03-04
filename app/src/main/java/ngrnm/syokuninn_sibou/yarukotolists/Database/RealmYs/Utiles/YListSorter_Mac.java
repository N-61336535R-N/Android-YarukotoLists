package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles;

import java.util.Collections;
import java.util.LinkedList;

import io.realm.Realm;
import io.realm.RealmList;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YItem;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YLI;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YList;

/**
 * Created by ryo on 2018/02/24.
 */

public class YListSorter_Mac extends YListSorter {
    
    public YListSorter_Mac(RealmList<YList> haveLists, RealmList<YItem> haveItems) {
        mkYListTmp(haveLists, haveItems);
    }
    
    public LinkedList<YLI> sort_mac50onn(final Realm realm) {
        Collections.sort(tmpOrderList, (l1, l2) -> l1.getLI_title(realm).compareTo(l2.getLI_title(realm)));
        return tmpOrderList;
    }
    
}
