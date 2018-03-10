package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;

//import de.timroes.android.listview.EnhancedListView;
import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Items.ItemsChecker;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.旧.Checker;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.旧.YActivity;

/**
 *  ＜＜＜＜＜〜〜〜〜〜  やることリストの内容(Item)一覧  〜〜〜〜〜＞＞＞＞＞
 * 
 * Items ⇆ Item は、タイトルが変わる可能性が高い。
 * よって、タイトルとファイル名は分離すべき
 * （タイトルをファイル名にしてしまうと、
 * 　　タイトルを変更したとき、Items のリストの中身が変わってしまう）
 * 管理番号を各々用意して、
 * それに対して
 * [0] タイトル
 * [1] 概要
 * [2] 詳細
 * のファイルを各々作る。
 */

public class YItemsActivity// extends YActivity 
{
    private static final String kind = "Item";
    
    private ItemsChecker ItC;
    private ItemsChecker FItC;
    
//    @Override
    protected Checker getLibC() {
        return ItC;
    }
    
    
    
    // 最初のリスト：カテゴリクリック時に、そのカテゴリの固有IDを取得
    //                              → その固有IDを元に、リストファイルから項目を取得
    /** 起動を早くするために、一度項目を取得したリストは一定期間保持し続けるスタイルに
     → 取得するときに、保持リストの総数が規定値を超えたら、一番古いリストを破棄する。
     */
/*    // リストビュー
    private EnhancedListView mListView;
    // リストビューに設定するリストとアダプター
    private LinkedList<String> mItemList;
    private ArrayAdapter<String> mAdapter;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yitems);
    
        // 多分、これで行ける。
        setInstance(this);
    
        // 初期値読み込み
        ItC = new ItemsChecker("list");  // リスト内容
        FItC = new ItemsChecker("finish");  // 終了リストの内容
        // リストビュー
        mListView = (EnhancedListView) findViewById(R.id.listview1);
    
        // 再描画のために一つにまとめた
        updateList();
        
        // アイテムクリック時のイベントを追加
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                // 選択アイテムを取得
                ListView listView = (ListView)parent;
                //String item = (String)listView.getItemAtPosition(pos);
                //Toast.makeText(YItemsActivity.this, item, Toast.LENGTH_LONG).show();
*/                /**(未)
                 * 次のintentに移動
                 * [実験] Preference.java に飛ぶ
                 * [実装] 項目の編集画面に飛ぶ
                 *      項目内に保存されている
                 *      ○ タイトル
                 *      ○ 概要
                 *      ○ 詳細
                 *      ○ メモ
                 *      ○ 達成度
                 *      ○ 各種時間
                 *      を読み込んで、編集できるようにする。
                 */
/*                Consts.ItemNumber = pos;
                // やることリスト（編集）画面に移動
                Intent intent = new Intent(YItemsActivity.this, YItemEditActivity.class);
                startActivity(intent);
            }
        });
        
*/        /* スワイプで 終了リストに移動する 設定
          *  追加削除をその都度やっていたら 負担が大きいので、
          *  追加削除を一旦、リスト内で"仮想的に"行う。
          *  ファイルを更新は画面遷移するタイミングで。
        */
/*        mListView.setDismissCallback(new EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {
                
                final String item = (String) mAdapter.getItem(position);
                /// 消す処理
                mItemList.remove(position);
                mAdapter.notifyDataSetChanged();
                return new EnhancedListView.Undoable() {
                    @Override
                    public void undo() {
                        // 元に戻す処理
                        mItemList.add(position, item);
                        mAdapter.notifyDataSetChanged();
                    }
                };
            }
        });
        mListView.enableSwipeToDismiss();
    }
    
    
    
    
    public void updateList() {
        // リストビューにアイテム追加
        mItemList = new LinkedList<String>();
        mItemList.addAll( ItC.getNames() );
        mAdapter = new ArrayAdapter<String>(this, R.layout.list_list_item, R.id.text, mItemList);
        mListView.setAdapter(mAdapter);
    }
    @Override
    public void removeList(int posi) {
        
    }
    
    
    
    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }
    
    // 画面遷移前に、mItemList の変更をディレクトリ構造に反映させる。
    @Override
    protected void onPause() {
        super.onPause();
        // 全て書き換えor変更箇所だけ書き換え
        // → 全て比較して、変更箇所のみ変更
        ItC.ReflectUpdate();
    }
    
    */
}

