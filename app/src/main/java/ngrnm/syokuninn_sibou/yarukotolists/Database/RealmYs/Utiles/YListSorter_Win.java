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

public class YListSorter_Win extends YListSorter {
    private LinkedList<YLI> tmpLists;
    private LinkedList<YLI> tmpItems;
    
    public YListSorter_Win(RealmList<YList> haveLists, RealmList<YItem> haveItems) {
        mkYListTmp(haveLists, haveItems);
    }
    @Override
    public void mkYListTmp(RealmList<YList> haveLists, RealmList<YItem> haveItems) {
        this.tmpLists = new LinkedList<YLI>();
        this.tmpItems = new LinkedList<YLI>();
    
        for (YList yL : haveLists) {
            tmpLists.add(new YLI(false, yL.getId()));
        }
        for (YItem yI : haveItems) {
            tmpItems.add(new YLI(true, yI.getId()));
        }
        
        this.tmpOrderList.addAll(tmpLists);
        this.tmpOrderList.addAll(tmpItems);
    }
    
    public LinkedList<YLI> sort_win50onn(final Realm realm) {
        this.tmpOrderList = new LinkedList<>();
        Collections.sort(tmpLists, (l1, l2) -> l1.getLI_title(realm).compareTo(l2.getLI_title(realm)));
        this.tmpOrderList.addAll(tmpLists);
        Collections.sort(tmpItems, (l1, l2) -> l1.getLI_title(realm).compareTo(l2.getLI_title(realm)));
        this.tmpOrderList.addAll(tmpItems);
        return tmpOrderList;
    }
    
}
