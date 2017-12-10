package ngrnm.syokuninn_sibou.yarukotolists;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.DirFile;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.YCategoryActivity;

/**
 * Created by ryo on 2017/09/03.
 */

public class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ycategory);
    
        /* ルートフォルダを、Consts に登録 */
        Consts.rootPath = getApplicationContext().getFilesDir().getPath() + "/";
        // DirFile の初期化（ルートディレクトリの設定）
        DirFile.setDirFile(Consts.rootPath);
    
    
    
        // やることリスト（編集）画面に移動
        Intent intent = new Intent(MainActivity.this, YCategoryActivity.class);
        startActivity(intent);
    
    
    
        finish();
    }
    
}
