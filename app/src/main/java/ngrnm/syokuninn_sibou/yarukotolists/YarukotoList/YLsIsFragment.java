package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import io.realm.Realm;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YItem;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YLI_Wrapper;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YList;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YPathTable;
import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.YLIsBasePageFragment;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Lists.ImageArrayAdapter;

/**
 * Created by ryo on 2018/02/23.
 */

public class YLsIsFragment extends YLIsBasePageFragment {
    private int parentYL_id;
    
    /* 画面のセッティング */
    private ImageArrayAdapter adapter;
    private ListView lV;
    
    
    public static YLsIsFragment newInstance(int listId, int hierarchy) {
        YLsIsFragment yLsIs = new YLsIsFragment();
        // List のID を渡す。
        Bundle bundle = new Bundle();
        bundle.putInt("listID", listId);
        bundle.putInt("hierarchy_num", hierarchy+1);
        yLsIs.setArguments(bundle);
        return yLsIs;
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        /* view 周りの設定 */
        View view = inflater.inflate(R.layout.fragment_ylist, null);
        /* リストビュー */
        lV = (ListView) view.findViewById(R.id.listview);
        
        // YList の取得
        parentYL_id = getArguments().getInt("listID");
        // order 系の初期化。すでに済んでいる場合は飛ばされる予定。
        realm.where(YList.class).equalTo("id", parentYL_id).findFirst().initOrder(realm);
        
        // 再描画のために一つにまとめた
        updateList();
        
        //リスト項目が選択された時のイベントを追加
        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                /**(未)
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
                int yLI_id = adapter.get(pos);
                YLI_Wrapper yli = new YLI_Wrapper(realm, yLI_id);
                
                if ( ! yli.isItem() ) {
                    /* List が選択された */
                    // やることリスト（編集）画面に移動
                    FragmentTransaction cft
                            = getParentFragment().getChildFragmentManager().beginTransaction();
                    YLsIsFragment yLsIs
                            = YLsIsFragment.newInstance(yLI_id, getArguments().getInt("hierarchy_num")+1);
                    cft.replace(R.id.y_screen, yLsIs);
                    // 戻るボタンで戻ってこれるように
                    cft.addToBackStack(null);
                    cft.commit();
                } else {
                    /* Item が選択された */
                    // やることリスト（編集）画面に移動
                    Bundle bundle = new Bundle();
                    bundle.putInt("itemID", yLI_id);
                    //bundle.putInt("hierarchy_num", getArguments().getInt("hierarchy_num")+1);
                    Intent intent = new Intent(getActivity(), YItemEditActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    
        
        registerForContextMenu(lV);
    
        // FloatingActionButton（↘︎のタイマーアイコン）
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fab.setOnClickListener(view_ -> {
            // アイテム追加 に接続。
            addItem_Dialog();
        });
    
        return view;
    }
    
    
    @Override
    protected String getSelectedLI_title(int posi) {
        return new YLI_Wrapper(realm, adapter.get(posi)).getLI_title();
    }
    @Override
    protected void renameListItem(int posi, String newTitle) {
        YLI_Wrapper yli = new YLI_Wrapper(realm, adapter.get(posi));
        yli.setLI_title(realm, newTitle);
    }
    @Override
    protected void removeListItem(int posi) {
        YList parentYlst = realm.where(YList.class).equalTo("id", parentYL_id).findFirst();
        parentYlst.rm_LI(realm, posi);
    }
    
    
    protected void updateList() {
        final YList parentYList = realm.where(YList.class).equalTo("id", parentYL_id).findFirst();
        // adapterのインスタンスを作成
        parentYList.initOrder(realm);
        adapter = new ImageArrayAdapter(getContext(), R.layout.list_image_item, parentYList.getO_list(), realm);
        lV.setAdapter(adapter);
    }
    
    
    
    @Override
    protected void add_Dialog() {
        /**
         * List と、Item の 追加ダイアログ を実装する。
         * チェックボックスとか必要そう。
         */
        // List + Item の個数が多すぎないかをチェック（上限：(Consts.LIMIT_Lists+Consts.LIMIT_Items) の 2/3）
        if (realm.where(YList.class).equalTo("id", parentYL_id).findFirst().getO_list().size() <= (Consts.LIMIT_Lists+Consts.LIMIT_Items)*2/3) {
            final EditText editView = new EditText(getActivity());
            editView.setInputType(InputType.TYPE_CLASS_TEXT);
            
            AlertDialog.Builder ADBuilder
                    = new AlertDialog.Builder(getActivity())
                    .setTitle("リスト名 を入力")
                    .setView(editView)
                    .setNegativeButton("キャンセル", (dialog, whichButton) -> {
                        // キャンセルボタンを押した時の処理。何もしないver
                    });
            // OKボタンの設定
            ADBuilder.setPositiveButton("OK", (dialog, whichButton) -> {
                String newItemTitle = editView.getText().toString();
                // サムネイル画像は、指定次第、その場で imgDirPath まで移動させる。
                // 第２引数は、移動先内での、その画像ファイルの名前のみ
                // 今は、"No_Image"で統一
                add_List(realm, 0, newItemTitle);
                Toast.makeText(getContext(), "「" + editView.getText().toString() + "」の作成に成功しました!", Toast.LENGTH_SHORT).show();
                updateList();
            });
            AlertDialog TsuikaDialog = ADBuilder.create();
            /* Dialog表示と同時にキーボードを出す。 */
            TsuikaDialog.setOnShowListener(arg0 -> {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editView, 0);
            });
            TsuikaDialog.show();
            
        } else {
            AlertDialog.Builder ADBuilder
                    = new AlertDialog.Builder(getActivity())
                    .setTitle("エラー")
                    .setMessage("追加できる上限を超えました。\nこれ以上は追加できません。\n\n有料版をお試しください。")
                    .setPositiveButton("キャンセル", (dialog, whichButton) -> {
                        // Okボタンを押した時の処理。何もしないver
                    });
            // OKボタンの設定
            AlertDialog ErrorDialog = ADBuilder.create();
            ErrorDialog.show();
            Toast.makeText(getActivity(), "上限：" + Consts.LIMIT_Category + " 個を超えました", Toast.LENGTH_SHORT).show();
        }
    }
    
    // いづれは↑と統合するけど、それまでの急場しのぎ。Floating Icon に接続した。
    protected void addItem_Dialog() {
        /**
         * List と、Item の 追加ダイアログ を実装する。
         * チェックボックスとか必要そう。
         */
        // List + Item の個数が多すぎないかをチェック（上限：(Consts.LIMIT_Lists+Consts.LIMIT_Items) の 2/3）
        if (realm.where(YList.class).equalTo("id", parentYL_id).findFirst().getO_list().size() <= (Consts.LIMIT_Lists+Consts.LIMIT_Items)*2/3) {
            final EditText editView = new EditText(getActivity());
            editView.setInputType(InputType.TYPE_CLASS_TEXT);
            
            AlertDialog.Builder ADBuilder
                    = new AlertDialog.Builder(getActivity())
                    .setTitle("項目名 を入力")
                    .setView(editView)
                    .setNegativeButton("キャンセル", (dialog, whichButton) -> {
                        // キャンセルボタンを押した時の処理。何もしないver
                    });
            // OKボタンの設定
            ADBuilder.setPositiveButton("OK", (dialog, whichButton) -> {
                String newItemTitle = editView.getText().toString();
                // サムネイル画像は、指定次第、その場で imgDirPath まで移動させる。
                // 第２引数は、移動先内での、その画像ファイルの名前のみ
                // 今は、"No_Image"で統一
                add_Item(realm, 0, newItemTitle);
                Toast.makeText(getContext(), "「" + editView.getText().toString() + "」の作成に成功しました!", Toast.LENGTH_SHORT).show();
                updateList();
            });
            AlertDialog TsuikaDialog = ADBuilder.create();
            /* Dialog表示と同時にキーボードを出す。 */
            TsuikaDialog.setOnShowListener(arg0 -> {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editView, 0);
            });
            TsuikaDialog.show();
            
        } else {
            AlertDialog.Builder ADBuilder
                    = new AlertDialog.Builder(getActivity())
                    .setTitle("エラー")
                    .setMessage("追加できる上限を超えました。\nこれ以上は追加できません。\n\n有料版をお試しください。")
                    .setPositiveButton("キャンセル", (dialog, whichButton) -> {
                        // Okボタンを押した時の処理。何もしないver
                    });
            // OKボタンの設定
            AlertDialog ErrorDialog = ADBuilder.create();
            ErrorDialog.show();
            Toast.makeText(getActivity(), "上限：" + Consts.LIMIT_Category + " 個を超えました", Toast.LENGTH_SHORT).show();
        }
        
    }
    
    // List の 追加のみを行うメソッド。
    public void add_List(Realm realm, int posi, String listName) {
        YPathTable ypTable_ORG = new YPathTable(false, parentYL_id);
        YList ylst_ORG = new YList(listName, ypTable_ORG.getId());
    
        // Realmへオブジェクトをコピーします。
        realm.beginTransaction();
        // これ以降の変更は、返り値のオブジェクトに対して行う必要があります
        realm.copyToRealm(ypTable_ORG);
        realm.copyToRealm(ylst_ORG);
        realm.commitTransaction();
    
        addParent(realm, posi, ylst_ORG.getId());
    }
    
    // Item の 追加のみを行うメソッド。
    public void add_Item(Realm realm, int posi, String itemName) {
        YPathTable ypTable_ORG = new YPathTable(true, parentYL_id);
        YItem yitm_ORG = new YItem(itemName, ypTable_ORG.getId());
    
        // Realmへオブジェクトをコピーします。
        realm.beginTransaction();
        // これ以降の変更は、返り値のオブジェクトに対して行う必要があります
        realm.copyToRealm(ypTable_ORG);
        realm.copyToRealm(yitm_ORG);
        realm.commitTransaction();
        
        addParent(realm, posi, yitm_ORG.getId());
    }
    
    private void addParent(Realm realm, int posi, int yLI_id) {
        // 親 List (今いるところ) に、Item を追加。
        YList parentYlst = realm.where(YList.class).equalTo("id", parentYL_id).findFirst();
        if (parentYlst != null) {
            parentYlst.add_yLI(realm, posi, yLI_id);
        } else throw new IllegalStateException("親 List が取得できませんでした。");
    
        // order_list を更新
        parentYlst.sortO(realm);
    }
    
    
}
