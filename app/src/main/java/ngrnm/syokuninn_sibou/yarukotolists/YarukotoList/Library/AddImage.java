package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library;


import android.content.res.AssetManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by M.R on 2017/06/04.
 */

public class AddImage {
    private String LIMIT_kind;
    
    public AddImage(String LIMIT) {
        LIMIT_kind = LIMIT;
        
    }
    
    
    // [引数] PackageName = this.getPackageName() にしてください。
    public static void add(InputStream input, FileOutputStream output, boolean isClose) {
        try{
            // bufferの設定
            int DEFAULT_BUFFER_SIZE = 10240 * 4;
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            // assetsから読出して、内部メモリの/data/data/... に保存
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            if (isClose) {
                output.close();
                input.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        
        // 保存した"ディレクトリ(フォルダ)"のパスなので注意。
        // 取り出す際は、+[ファイル名] をすること。（〜/ はすでにあるのでくっつけなくてOK）
    }
    
    
}
