package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.旧;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.AddImage;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.LibraryChecker;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Lists.ViewData;


/**
 *  ＜＜＜＜＜〜〜〜〜〜  やることカテゴリ一覧（独立画面ver）  〜〜〜〜〜＞＞＞＞＞
 * やることリストの「カテゴリタイル（カテゴリ選択）画面」
 * タイマーへは、タイマーセット画面へのリンクのみ
 * 
 * 
 * ■ VDatas が用意できれば、画面描画はできる。
 */
public class YCategoryActivity extends YLibraryActivity {
    private static final String kind = "Category";
    @Override protected String getKind() {
        return kind;
    }
    
    private LibraryChecker LibC;
    @Override protected LibraryChecker getLibC() {
        return LibC;
    }
    @Override protected void setLibC(LibraryChecker LibC) {
        this.LibC = LibC;
    }
    
    @Override protected AdapterView getAdptrView() {
        return gV;
    }
    
    
    private ViewData[] VDatas;
    GridView gV;
    
    
    @Override protected int getLayoutResID() {
        return R.layout.activity_ycategory;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // gVのボタンの設定
        gV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                String message = position + "番目が選択されました。";
                Toast.makeText(YCategoryActivity.this, message, Toast.LENGTH_LONG).show();*/
                
                // 選択された「リストの内容を保存したファイル」の場所（名前）を取得
    
                /** （未）未実装。
                 *  ● 「リストの内容を保存したファイル」の場所を、YListerActivityの intent に引数として渡す。
                 *  ● 渡された先(YListerActivity)で、「読み込み中」のメッセージを表示
                 *  ● 
                 */
                
                // やることリスト（編集）画面に移動
                Intent intent = new Intent(YCategoryActivity.this, YListerActivity.class);
                //intent.putExtra("ListPath", List_Datas[1][position]);
                // Stack に、ディレクトリ名のみ保存
                Consts.inLibrary( VDatas[position].getTitle() );
                Consts.hierarchy++;
                //ntent.putExtra("CategoryName", VDatas[position].title);
                //String msg = "Consts.libraryName.size()：" + Consts.libraryName.size();
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    
                startActivity(intent);
            }
        });
        
    }
    
    @Override
    protected void updateList() {
        // デフォルト画像が準備できてない（No_Image.pngがない）場合は、
        // セッティングし直す。
        String NoImage = Consts.rootPath + "img/";
        if (!new File(NoImage).exists()) {
            setAllImage("CategoryImgSources", NoImage);
        }
    
        // GridView に表示する項目の登録
        int test = LibC.getSize();
        VDatas = new ViewData[test];
        for (int i = 0; i< LibC.getSize(); i++) {
            String childPath = LibC.getKindDirPath()+LibC.getNames().get(i)+"/";
            switch (LibC.getImgNames().get(i)) {
                case "No_Image":
                    VDatas[i] = new ViewData(NoImage+"No_Image.png", LibC.getNames().get(i));
                    break;
                default:
                    VDatas[i] = new ViewData(childPath+Consts.LIB_IMG_NAME, LibC.getNames().get(i));
            }
        }
        // GridViewのインスタンスを生成
        gV = (GridView) findViewById(R.id.gridview);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を activity_ycategory.xmlry.xml に inflate するためにGridAdapterに引数として渡す
//        GridAdapter adapter = new GridAdapter(this.getApplicationContext(), R.layout.grid_items, VDatas);
        // gVにadapterをセット
//        gV.setAdapter(adapter);
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
    
}
