package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import io.realm.Realm;
import io.realm.RealmList;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YCategory;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YCtgryOrder;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YList;
import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.YCLIsBasePageFragment;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.AddImage;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Grids.GridAdapter;

/**
 * Created by ryo on 2018/02/19.
 */

public class YCategoryFragment extends YCLIsBasePageFragment {
    // gridView 関連
    private GridAdapter adapter;
    private GridView gV;
    
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ycategory, null);
        
        // GridViewのインスタンスを生成
        gV = (GridView) view.findViewById(R.id.gridview);
        updateList();
    
        registerForContextMenu(gV);
    
        // gVのボタンの設定
        gV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int posi, long id) {
                
                // やることリスト（編集）画面に移動
                final YList yl = ((YCategory) adapter.getItem(posi)).getHaveList(realm);
                FragmentTransaction cft = getParentFragment().getChildFragmentManager().beginTransaction();
                YLsIsFragment yLsIs = YLsIsFragment.newInstance(yl.getId(), 1);
                
                cft.addToBackStack(null);  // 戻るボタンで戻ってこれるように
                cft.replace(R.id.y_screen, yLsIs);
                cft.addToBackStack(null);  // 戻るボタンで戻ってこれるように
                
                cft.commit();
            }
        });
        
        registerForContextMenu(gV);
    /*
        //  タイマー設定画面に移動するためのボタン(↓の横長ボタン)
        Button sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YLibraryActivity.this, TimerSetActivity.class);
                startActivity(intent);
            }
        });
        // FloatingActionButton（↘︎のタイマーアイコン）
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // やることリスト（編集）画面に移動
                Intent intent = new Intent(YLibraryActivity.this, TimerActivity.class);
                startActivity(intent);
            }
        });
    */
        return view;
    }
    
    
    
    
    protected void updateList() {
        // デフォルト画像が準備できてない（No_Image.pngがない）場合は、セッティングし直す。
        // 
        String imgRootDir = Consts.rootPath + "ctg_img/";
        if (!new File(imgRootDir+"No_Image.png").exists())  setAllImage("CategoryImgSources", imgRootDir);
        
        // Category 一覧を取得
        final RealmList<Integer> sorted_ctgIDs = realm.where(YCtgryOrder.class).findFirst().getYO();
        
        // Adapter を作成。Base でも Array でもいけた。
        LinkedList<YCategory> yctgList = new LinkedList<>();
        for (int id : sorted_ctgIDs) {
            yctgList.add(realm.where(YCategory.class).equalTo("id", id).findFirst());
        }
        adapter = new GridAdapter(getContext(), yctgList, imgRootDir);
        // GridView に Adapter をセット
        gV.setAdapter(adapter);
    }
    
    
    public void setAllImage(String ImDir, String outPath) {
        // アセットの defaultImage をエンコードしてコピー
        AssetManager assetManager = getResources().getAssets();
        String[] fileList = null;
        InputStream input = null;
        FileOutputStream output = null;
        try {
            // ファイルリストを作成する
            fileList = assetManager.list(ImDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        File outF = new File(outPath);
        if (!outF.exists()) outF.mkdirs();
        boolean isClose = false;
        for(int i=0; i< fileList.length ; i++){
            try {
                input = assetManager.open(ImDir + "/" + fileList[i]);
                // 保存先のパス
                output = new FileOutputStream(new File(outPath + fileList[i]));
                
                if (i == fileList.length-1) { isClose = true; }
                AddImage.add(input, output, isClose);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    
    
    
    
    
    protected void add_Dialog() {
        if (realm.where(YCategory.class).findAll().size() <= Consts.LIMIT_Category) {
            final EditText editView = new EditText(getActivity());
            editView.setInputType(InputType.TYPE_CLASS_TEXT);
            AlertDialog.Builder ADBuilder
                    = new AlertDialog.Builder(getActivity())
                    .setTitle("カテゴリー名 を入力")
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
                add(realm, newItemTitle);
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
                    .setMessage("上限：" + Consts.LIMIT_Category + " 個を超えました。\nこれ以上は追加できません。\n\n有料版をお試しください。")
                    .setPositiveButton("キャンセル", (dialog, whichButton) -> {
                        // Okボタンを押した時の処理。何もしないver
                    });
            // OKボタンの設定
            AlertDialog ErrorDialog = ADBuilder.create();
            ErrorDialog.show();
            Toast.makeText(getActivity(), "上限：" + Consts.LIMIT_Category + " 個を超えました", Toast.LENGTH_SHORT).show();
        }
    }
    
    // Category の 追加のみを行うメソッド。
    public static void add(Realm realm, String ctgryName) {
        YList yCLstLst = new YList("List 0");
        YList yCLst = new YList("ctgList");
        
        yCLst.getLists().add(yCLstLst);  //List 変更時に変えるの忘れない。
        
        YCategory yctg = new YCategory(ctgryName);
        yctg.setHaveListID(yCLst.getId());
        
        realm.beginTransaction();
        // Realmへオブジェクトをコピーします。
        // これ以降の変更は、返り値のオブジェクトに対して行う必要があります
        YList rYLst = realm.copyToRealm(yCLst);  //id で分割したので、こっちも別途登録の必要あり。
        YCategory rYCtg = realm.copyToRealm(yctg);
        realm.commitTransaction();
    
        YCtgryOrder yco = realm.where(YCtgryOrder.class).findFirst();
        if (yco == null) {
            yco = new YCtgryOrder();
            realm.beginTransaction();
            // Realmへオブジェクトをコピーします。
            // これ以降の変更は、返り値のオブジェクトに対して行う必要があります
            YCtgryOrder ryco = realm.copyToRealmOrUpdate(yco);
            realm.commitTransaction();
            ryco.initYO(realm);
        } else {
            yco.addNewYCtg(realm, rYCtg.getId());
        }
    }
    
    
    
    
    @Override
    protected String getSelectedItemTitle(int posi) {
        return realm.where(YCtgryOrder.class).findFirst().getYCtgAtPosiFromYO(realm, posi).getTitle();
    }
    
    @Override
    protected void editListItem(int posi, String title) {
        YCtgryOrder yco = realm.where(YCtgryOrder.class).findFirst();
        yco.editYctg(realm, posi, title);
    }
    @Override
    protected void removeListItem(int posi) {
        realm.where(YCtgryOrder.class).findFirst().removeYctg(realm, posi);
    }
    
    
    
}
