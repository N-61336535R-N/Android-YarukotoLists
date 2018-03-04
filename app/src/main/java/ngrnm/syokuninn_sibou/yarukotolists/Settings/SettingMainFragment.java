package ngrnm.syokuninn_sibou.yarukotolists.Settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import ngrnm.syokuninn_sibou.yarukotolists.R;

/**
 * Created by ryo on 2017/09/28.
 */

public class SettingMainFragment extends Fragment {
    private BaseAdapter adapter;
    private ListView lV;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        /* view 周りの設定 */
        View view = inflater.inflate(R.layout.fragment_setting, null);
        
        
        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に 
        // inflate するためにadapterに引数として渡す
        adapter = new SettingActivityListVAdapter(getContext(), R.layout.setting_list, SettingConsts.scenes, SettingConsts.icons);
        /* リストビュー */
        lV = (ListView) view.findViewById(R.id.list_view);
        
        // order 系の初期化。すでに済んでいる場合は飛ばされる予定。
        //realm.where(YList.class).equalTo("id", parentYL_id).findFirst().initOrder(realm);
        
        // 再描画のために一つにまとめた
        lV.setAdapter(adapter);
        
        //リスト項目が選択された時のイベントを追加
        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                
                SettingFragment sttgFragment = SettingFragment.getInstance(pos);
                Bundle bundle = new Bundle();
                bundle.putInt("posi", pos);
                
                sttgFragment.setArguments(bundle);
                
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.setting_screen, sttgFragment);
                ft.addToBackStack(null);
                ft.commit();
                
            }
        });
        
        return view;
    }
    
    
}
