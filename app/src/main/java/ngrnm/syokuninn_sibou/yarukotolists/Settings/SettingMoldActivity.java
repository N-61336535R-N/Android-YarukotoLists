package ngrnm.syokuninn_sibou.yarukotolists.Settings;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ngrnm.syokuninn_sibou.yarukotolists.R;

/**
 * Created by ryo on 2017/09/29.
 */

public class SettingMoldActivity extends AppCompatActivity {
    private Toolbar toolbar;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_mold);
        setViews();
    }
    
    private void setViews() {
        // Fragment でツールバーを扱えるようにする
        toolbar = (Toolbar) findViewById(R.id.setting_toolBar);
        setSupportActionBar(toolbar);
    
        SettingMainFragment settingFrag = new SettingMainFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.setting_screen, settingFrag);
        ft.commit();
    }
    
    
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            /* その画面内で戻る処理 */
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
    }
}
