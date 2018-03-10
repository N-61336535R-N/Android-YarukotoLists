package ngrnm.syokuninn_sibou.yarukotolists;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.YLsIsFragment;


/**
 * やること～ のリスト関係を編集するタブ
 */
public class YFragment extends Fragment {
    
    public static YFragment newInstance() {
        YFragment frag = new YFragment();
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
        Fragment ycateg_frag = new YCategoryFragment();
        transaction.add(R.id.y_screen, ycateg_frag, "category");
        transaction.commit();
    */
        final FragmentManager cfm = getChildFragmentManager();
        FragmentTransaction transaction = cfm.beginTransaction();
    
        // やることリスト（編集）画面に移動
        YLsIsFragment yLsIs = YLsIsFragment.newInstance(0, 1);
        
        transaction.add(R.id.y_screen, yLsIs, "root");
        transaction.commit();
    
    }
    
    
}
