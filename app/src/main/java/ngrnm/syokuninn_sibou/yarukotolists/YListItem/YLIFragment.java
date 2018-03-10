package ngrnm.syokuninn_sibou.yarukotolists.YListItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ngrnm.syokuninn_sibou.yarukotolists.R;


/**
 * やること～ のリスト関係を編集するタブ
 * 
 * YList, YItem のFragment表示の管理を行う。
 */
public class YLIFragment extends Fragment {
    
    public static YLIFragment newInstance() {
        YLIFragment frag = new YLIFragment();
        return frag;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_y, null);
    }
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    /*
        final FragmentManager cfm = getChildFragmentManager();
        FragmentTransaction transaction = cfm.beginTransaction();
        Fragment ycateg_frag = new YLI_GridViewFragment();
        transaction.add(R.id.y_screen, ycateg_frag, "category");
        transaction.commit();
    */
        final FragmentManager cfm = getChildFragmentManager();
        FragmentTransaction transaction = cfm.beginTransaction();
    
        // やることリスト（編集）画面に移動
        YLI_RecyclerViewFragment yLsIs = YLI_RecyclerViewFragment.newInstance(0, 1);
        
        transaction.add(R.id.y_screen, yLsIs, "root");
        transaction.commit();
    
    }
    
    
}
