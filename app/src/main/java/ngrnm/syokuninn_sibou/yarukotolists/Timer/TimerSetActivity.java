package ngrnm.syokuninn_sibou.yarukotolists.Timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ngrnm.syokuninn_sibou.yarukotolists.R;


/**
 * Created by M.R on 2017/05/05.
 */

public class TimerSetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_set);
        
        Button returnButton = (Button) findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        

        // FloatingActionButton（↘︎のタイマーアイコン）
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // やることリスト（編集）画面に移動
                Intent intent = new Intent(TimerSetActivity.this, TimerActivity.class);
                startActivity(intent);
            }
        });
    
    }
    
    
    // ↗︎ オプションメニューの中身設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.y_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        switch (id) {
            case R.id.menu_add_list:
                Toast.makeText(this, "(未)タイマーセット追加", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "(未)設定", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_save_timer_set:
                Toast.makeText(this, "(未)タイマーリスト保存", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.system_exit:
                Toast.makeText(this, "(未)終了", Toast.LENGTH_SHORT).show();
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
}
