package ngrnm.syokuninn_sibou.yarukotolists.Timer;

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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.SettingMoldActivity;
import ngrnm.syokuninn_sibou.yarukotolists.Timer.Utils.CountDown;
import ngrnm.syokuninn_sibou.yarukotolists.Utils.DebugUtils.SysOutPrintln;

import static android.support.v4.app.ActivityCompat.invalidateOptionsMenu;


/**
 * やることタイマー (時間表示) のタブ
 */
public class YTimerFragment extends Fragment {
    // タイマー
    private Button startButton, stopButton;
    private TextView timerText;  // .setLI_title("");した段階で、画面に表示される
    
    private static CountDown countDown;
    private static List<CountDown> countDownList;
    long millisLeft;
    
    
    private final static String BACKGROUND_COLOR = "background_color";
    
    public static YTimerFragment newInstance(@ColorRes int IdRes) {
        YTimerFragment frag = new YTimerFragment();
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
        View view = inflater.inflate(R.layout.fragment_ytimer, null);
    
        timerText = (TextView) view.findViewById(R.id.timer);
        timerText.setText("0:35 s");
    
    
        // インスタンス生成
        // CountDownTimer(long millisInFuture, long countDownInterval)
        // 3分= 3x60x1000 = 180000 msec
        millisLeft = 35000;
        countDown = new CountDown(millisLeft, 333, timerText);
    
    
    
        // トグルボタンをタップした時の処理
        ((ToggleButton) view.findViewById(R.id.toggleButton)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            
                // 開始
                if (isChecked) {
                    countDown = new CountDown(millisLeft, 333, timerText); //countMillisを残り時間にセット
                    countDown.start(); // タイマーをスタート
                
                    // 一時停止
                } else {
                    millisLeft = countDown.getMillisLeft();
                    long mm = millisLeft / 1000 / 60;
                    long ss = millisLeft/ 1000 % 60;
                    long ms = (millisLeft - ss * 1000 - mm * 1000 * 60) / 10;
                    timerText.setText(String.format("%1$02d:%2$02d.%3$02d ms", mm, ss, ms));
                    countDown.cancel(); // タイマーをストップ
                }
            }
        });
        /*
        // トグルボタンをロングタップした時の処理
        ((ToggleButton) view.findViewById(R.id.toggleButton)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                countDown.cancel(); // タイマーをストップ
                millisLeft = 15000; // カウントダウン時間を初期値にリセット
                ((TextView)view.findViewById(R.id.textView)).setLI_title(String.valueOf(millisLeft / 1000)); // テキストビューに初期値をセット
                ((ToggleButton)view.findViewById(R.id.toggleButton)).setChecked(false); // toggleボタンをオフにする
                return true;
            }
        });
    */
    
    
        Button sendButton = (Button) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 変更の必要あり。
                //Intent intent = new Intent(getActivity(), YListerActivity.class);
                //startActivity(intent);
            }
        });
    
        return view;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**  ↗︎ オプションメニューの中身設定  */
    // 画面切り替えと同時に、オプションも切り替える方法を考える。
    private Menu menu;
    private MenuInflater menuInflater;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        Log.d("(Timer)onCreate", "YTimerFragment  ->  Create");
        this.menu = menu;
        this.menuInflater = menuInflater;
        menu.clear();
        menuInflater.inflate(R.menu.menu_timer, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        
        return super.onOptionsItemSelected(item);
    }
    
    /*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        
        SysOutPrintln.printMark("setUserVisibleHint -> getView() : " + getView());
        if (isVisibleToUser && getView() != null) {
            // 画面が出来上がった後で処理する。
            //getActivity().invalidateOptionsMenu();
        }
    }*/
    
}
