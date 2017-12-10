package ngrnm.syokuninn_sibou.yarukotolists.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import ngrnm.syokuninn_sibou.yarukotolists.R;

/**
 * Created by ryo on 2017/09/29.
 */

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private BaseAdapter adapter;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        
        // ListViewのインスタンスを生成
        ListView listView = (ListView) findViewById(R.id.list_view);
        
        // BaseAdapter を継承したadapterのインスタンスを生成
        // レイアウトファイル list.xml を activity_main.xml に 
        // inflate するためにadapterに引数として渡す
        adapter = new SettingActivityListVAdapter(this.getApplicationContext(), R.layout.setting_list, SettingConsts.scenes, SettingConsts.icons);
        
        // ListViewにadapterをセット
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(this);
        
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        Intent intent = new Intent(this.getApplicationContext(), SettingMoldActivity.class);
        SettingConsts.setPref_num(position);
        /*
        // clickされたpositionのtextとphotoのID
        String selectedText = SettingConsts.scenes[position];
        int selectedPhoto = SettingConsts.icons[position];
        // インテントにセット
        intent.putExtra("Text", selectedText);
        intent.putExtra("Photo", selectedPhoto);
        */
        // Activity をスイッチする
        startActivity(intent);
    }
}
