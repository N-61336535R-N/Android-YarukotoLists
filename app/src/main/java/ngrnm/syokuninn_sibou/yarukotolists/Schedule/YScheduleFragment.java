package ngrnm.syokuninn_sibou.yarukotolists.Schedule;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ngrnm.syokuninn_sibou.yarukotolists.R;


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
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yschedule, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.fragment_main_linearlayout);
        linearLayout.setBackgroundResource(getArguments().getInt(BACKGROUND_COLOR));
        
        return view;
    }
    
}
