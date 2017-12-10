package ngrnm.syokuninn_sibou.yarukotolists.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ngrnm.syokuninn_sibou.yarukotolists.R;

/**
 * Created by ryo on 2017/09/28.
 */

public class SettingMoldActivity extends AppCompatActivity implements SettingFragment.OnFragmentInteractionListener {
    public static Intent createInstance(Context con) {
        Intent intent = new Intent(con, SettingFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_mold);
    }
}
