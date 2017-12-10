package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ngrnm.syokuninn_sibou.yarukotolists.R;


/**
 * ＜＜＜＜＜〜〜〜〜〜  やることリストの内容(Item)の表示画面  〜〜〜〜〜＞＞＞＞＞
 */


public class YItemShowActivity extends AppCompatActivity {
    /** 選択した項目の内容の一時保管場所
     *  [0] タイトル
     *  [1] 概要
     *  [2] 詳細メモ    */
    private String[] ItemContents = new String[3];
    private String[] edit_history;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yitem_edit);
        
        /**
         * ● 項目名（管理番号）から項目のファイルを探し出して、
         *     内容を ItemContents に取り込み。
         * ● 
         *
         * ○ スタックに編集履歴を保存
         *       ○タイトル・概要・詳細メモごちゃ混ぜにして履歴を保存
         *              保存形式は、
         *                  1. 全部保存
         *                  2. 変更箇所だけ保存 ←用工夫
         *      → 古いデータは削除（履歴の数は [設定] 可能にする。デフォルト10）
         * ○ 戻る・進むで
         * */
    }
    
    // ↗︎ オプションメニューの中身設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yitem_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        switch (id) {
            case R.id.menu_redo:
                Toast.makeText(this, "(未)redo", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "(未)設定", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.system_exit:
                Toast.makeText(this, "(未)終了", Toast.LENGTH_SHORT).show();
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    
    
}
