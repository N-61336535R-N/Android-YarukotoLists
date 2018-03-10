package ngrnm.syokuninn_sibou.yarukotolists.YListItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Deque;

import io.realm.Realm;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YItem;
import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.YListItem.Items.History;
import ngrnm.syokuninn_sibou.yarukotolists.YListItem.Items.urdo_TW;


/**
 * ＜＜＜＜＜〜〜〜〜〜  やることリストの内容(Item)の編集画面  〜〜〜〜〜＞＞＞＞＞
 * 
 * いずれは Fragment にして、ダイアログっぽい感じにする予定。
 * 
 */
public class YItemEditActivity extends AppCompatActivity {
    Realm realm;
    // Item の ID
    int itemID;
    /** 選択した項目の内容の一時保管場所
     *  [0] タイトル
     *  [1] 概要
     *  [2] 詳細メモ
     */
    private static final int Item_num = Consts.Item_num;
    
    private EditText[] ET = new EditText[Item_num];
    
    private static Deque<History> ItemCs_after = new ArrayDeque<>();  //「進む」の格納場所
    
    
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
        // bundle から realm の item id を取得。
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        itemID = bundle.getInt("itemID");
    
        // このスレッドのためのRealmインスタンスを取得
        realm = Realm.getDefaultInstance();
        YItem yItem = realm.where(YItem.class).equalTo("id", itemID).findFirst();
        String[] kindList = {yItem.getTitle(), yItem.getTxtGaiyou(), yItem.getTxtSyousai()};
        
        int[] vidList = {R.id.eTxtTitle, R.id.eTxtGaiyou, R.id.eTxtSyousai};
    
        for (int i=0; i<Item_num; i++){
            ET[i] = (EditText)findViewById(vidList[i]);
            ET[i].setText(kindList[i]);
            ET[i].addTextChangedListener(new urdo_TW(i));
        }
        urdo_TW.allow_push = true;
    }
    
    // ↗︎ オプションメニューの中身設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_yitem_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        switch (id) {
            case R.id.menu_undo:
                undoButtonClick();
                Toast.makeText(this, "(未)undo", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_redo:
                redoButtonClick();
                Toast.makeText(this, "(未)redo", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_save:
                saveButtonClick();
                Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT).show();
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
    
    private void saveButtonClick() {
        /* 入力されたデータを保存 */
        YItem yitem = realm.where(YItem.class).equalTo("id", itemID).findFirst();
        realm.beginTransaction();
        yitem.setTitle(ET[0].getText().toString());
        yitem.setTxtGaiyou(ET[1].getText().toString());
        yitem.setTxtSyousai(ET[2].getText().toString());
        realm.commitTransaction();
    }
    
    
    @Override
    protected void onPause() {
        super.onPause();
        // 戻るボタンが押されたら、保存し、ItemsChecker を再度実行して再読み込みする。
        saveButtonClick();
        //ItC.check();
        Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT).show();
    }

    
    /**
     * 【未】戻る・進むボタン
     */
    private void undoButtonClick() {
        // 読み込み
        urdo_TW.allow_push = false;
        if ( urdo_TW.peek() ) {
            ItemCs_after.addFirst(urdo_TW.history_now);
            History unH = urdo_TW.pop();
            ET[unH.getTag()].setText(unH.getS());
        }
    }
    
    private void redoButtonClick() {
        // 読み込み
        //urdo_TW.allow_push = false;
        if ( ItemCs_after.peekFirst() != null ) {
            //ItemCs_after.addFirst(urdo_TW.history);
            History reH = ItemCs_after.pollFirst();
            ET[reH.getTag()].setText(reH.getS());
        }
        //urdo_TW.allow_push = true;
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }
}
