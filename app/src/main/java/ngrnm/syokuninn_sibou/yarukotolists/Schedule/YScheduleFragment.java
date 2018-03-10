package ngrnm.syokuninn_sibou.yarukotolists.Schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.SettingMoldActivity;


/**
 * やること～ の今日のスケジュール を編集するタブ
 */
public class YScheduleFragment extends Fragment {
    private final static String BACKGROUND_COLOR = "background_color";
    
    public static YScheduleFragment newInstance(@ColorRes int IdRes) {
        YScheduleFragment frag = new YScheduleFragment();
        Bundle b = new Bundle();
        b.putInt(BACKGROUND_COLOR, IdRes);
        frag.setArguments(b);
        return frag;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yschedule, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.fragment_main_linearlayout);
        linearLayout.setBackgroundResource(getArguments().getInt(BACKGROUND_COLOR));
        
        return view;
    }
    
    
    
    
    
    
    /**  ↗︎ オプションメニューの中身設定  */
    // 画面切り替えと同時に、オプションも切り替える方法を考える。
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        Log.d("(schedule)onCreate", "YScheduleFragment  ->  Create");
        menu.clear();
        //menuInflater.inflate(R.menu.menu_timer, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_add_list:
                Toast.makeText(getContext(), "(未)タイマーセット追加", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_save_timer_set:
                Toast.makeText(getContext(), "(未)タイマーリスト保存", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingMoldActivity.class);
                startActivity(intent);
                return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }
    
    
}
