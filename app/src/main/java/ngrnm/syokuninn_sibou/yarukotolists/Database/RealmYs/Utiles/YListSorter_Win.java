package ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles;

import java.util.Collections;
import java.util.LinkedList;

import io.realm.Realm;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YPathTable;

/**
 * Created by ryo on 2018/02/24.
 */

public class YListSorter_Win extends YListSorter {
    private LinkedList<Integer> tmpLists;
    private LinkedList<Integer> tmpItems;
    
    public YListSorter_Win(YPathTable yPT) {
        super(yPT);
    }
    @Override
    public void mkYListTmp(YPathTable yPTable) {
        this.tmpLists = new LinkedList<Integer>();
        this.tmpItems = new LinkedList<Integer>();
    
        for (Integer yLI_id : yPTable.getChildIDs()) {
            if (!yPTable.isItem())
                 tmpLists.add(yLI_id);
            else tmpItems.add(yLI_id);
        }
        
        this.tmpOrderList.addAll(tmpLists);
        this.tmpOrderList.addAll(tmpItems);
    }
    
    public LinkedList<Integer> sort_win50onn(final Realm realm) {
        this.tmpOrderList.clear();
        Collections.sort( tmpLists, (id1, id2) -> getLI_title(realm, id1).compareTo( getLI_title(realm, id2) ) );
        this.tmpOrderList.addAll(tmpLists);
        Collections.sort( tmpItems, (id1, id2) -> getLI_title(realm, id1).compareTo( getLI_title(realm, id2) ) );
        this.tmpOrderList.addAll(tmpItems);
        return tmpOrderList;
    }
    
}
