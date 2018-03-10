package ngrnm.syokuninn_sibou.yarukotolists.Settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.PrefDetailSetter.SetBackupFragment;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.PrefDetailSetter.Set_p1Fragment;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.PrefDetailSetter.Set_p2Fragment;

/**
 * Created by ryo on 2017/09/28.
 */

public class SettingMainFragment extends Fragment {
    /* 設定画面のデザインもろもろ */
    // 設定 のカテゴリ一覧
    static final String[] scenes = {
            // Scenes of Isle of Wight
            "表示",
            "構造",
            "バックアップ・復元"
    };
    // アイコン一覧
    static final int[] icons = {
            R.drawable.ic_build_black_24dp,
            R.drawable.ic_build_black_24dp,
            R.drawable.ic_build_black_24dp,
    };
    
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
        
        
        /* リストビュー */
        adapter = new SettingActivityListVAdapter(getContext(), R.layout.setting_list, scenes, icons);
        lV = (ListView) view.findViewById(R.id.list_view);
        
        // 今のところ、設定は preference に保存でいく予定。後々 realm に移行するかも？
        //realm.where(YList.class).equalTo("id", parentYL_id).findFirst().initOrder(realm);
        lV.setAdapter(adapter);
        
        //リスト項目が選択された時のイベントを追加
        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
    
                PreferenceFragmentCompat prefragc = null;
                switch (scenes[pos]) {
                    case "表示":
                        prefragc = new Set_p1Fragment();
                        break;
                    case "構造":
                        prefragc = new Set_p2Fragment();
                        break;
                    case "バックアップ・復元":
                        prefragc = new SetBackupFragment();
                        break;
                    default:
                        throw new IllegalArgumentException("その設定項目は未実装のようです...。");
                }
                /*
                Bundle bundle = new Bundle();
                bundle.putInt("posi", pos);
                prefragc.setArguments(bundle);
                */
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.setting_screen, prefragc);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        
        return view;
    }
    
    
}
