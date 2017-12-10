package ngrnm.syokuninn_sibou.yarukotolists.Database;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ryo on 2017/11/06.
 */

public class SQLiteUtils {
    /**
     * DBファイルをSDカードにコピーする
     * AndroidManifest.xmlにWRITE_EXTERNAL_STORAGEを設定すること
     *
     * @param Context context メソッド呼び出し元(Activity等)のContext
     * @param String inDBName コピー元となるデータベースファイル名 
     * @return コピーに成功した場合true
     * @throws IOException なんかエラーが起きた場合にthrow
     */
    public static boolean copyDBtoSD(Context context, String inDBName, String outSDPath) throws IOException {
        
        final String TAG = "copyDBtoSD";
        
        //保存先(SDカード)のディレクトリを確保
        File outDir = new File(outSDPath);
        //保存先の存在確認
        if (!outDir.exists() && !outDir.mkdirs()) {
            throw new IOException("FAILED_TO_CREATE_PATH_ON_SD");
        }
        
        final String inDBPath = context.getDatabasePath(inDBName).getPath();
        final String outFilePath = new StringBuilder()
                .append(outSDPath)
                .append("/")
                .append(inDBName)
                .append("")
                .append((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()))
                .toString();
        
        Log.i(TAG, "copy from(DB): "+inDBPath);
        Log.i(TAG, "copy to(SD)  : "+outFilePath);
        
        FileChannel channelSource = new FileInputStream(inDBPath).getChannel();
        FileChannel channelTarget = new FileOutputStream(outFilePath).getChannel();
        
        channelSource.transferTo(0, channelSource.size(), channelTarget);
        
        channelSource.close();
        channelTarget.close();
        
        return true;
    }
}
