package ngrnm.syokuninn_sibou.yarukotolists.Database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * Created by ryo on 2017/11/06.
 */

public class DatabaseSankou1 extends Activity {
    static final int DB_VERSION = 1;
    
    String s;
    String sd_dir;
    String sd_stt;
    String db_file;
    
    static SQLiteDatabase db;
    private TextView tv;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        boolean b;
        SQLiteHelper hlpr = new SQLiteHelper(getApplicationContext());
        db = hlpr.getWritableDatabase();
        
        tv = new TextView(this);
        setContentView(tv);
        
        ContentValues values = new ContentValues();
        Random r=new Random();
        values.put("data", r.nextInt());
        db.insert("tbl", null, values); //アプリが起動するごとに追加
        
        sd_dir= Environment.getExternalStorageDirectory().getPath();     //SDカードディレクトリ
        sd_stt=Environment.getExternalStorageState();           //SDカードの状態を取得
        db_file=db.getPath();   //DBのディレクトリとファイル名
        
        s="";
        s+="\n SDカードのパス = " + sd_dir;
        s+="\n SDカードの状態 = " + sd_stt;
        s+="\n SQLiteフォルダー = " + db_file;
        
        b=sd_stt.equals(Environment.MEDIA_MOUNTED);     //SDカードの状態
        if(b==false) {  //書込み状態でマウントされていない。
            s+="\n SDメモリが書込み状態でマウントされていません。";
            tv.setText(s);
            return;         //ディレクトリ作成失敗
        }
        
        File f = new File(sd_dir + "/test");
        b=f.exists();           //SDカードにtestディレクトリがあるか。
        if(b==false) {          //ディレクトリが存在しないので作成。
            b=f.mkdir();    //　sdcard/testディレクトリを作ってみる。
            if(b==false) {
                s+="\n フォルダの作成に失敗しました。";
                tv.setText(s);
                return;         //ディレクトリ作成失敗
            }
        }
        
        filecopy(db_file , sd_dir+"/test.db");  //DBのファイルをSDにコピー
        tv.setText(s);
    }
    
    private static class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context c) {
            super(c, "test.db", null, DB_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table tbl (" +
                    "_id integer primary key autoincrement," +
                    "data integer not null );");
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table tbl;");
            onCreate(db);
        }
    }
    
    //ファイルのコピー（チャネルを使用）
    private void filecopy(String file_src,String file_dist) {
        int err;
        FileInputStream fis;
        FileOutputStream fos;
        
        err=0;
        File fi = new File(file_src);
        File fo = new File(file_dist);
        try {
            fis=new FileInputStream(fi);
            FileChannel chi = fis.getChannel();
            
            fos=new FileOutputStream(fo);
            FileChannel cho = fos.getChannel();
            
            chi.transferTo(0, chi.size(), cho);
            chi.close();
            cho.close();
        }
        catch (FileNotFoundException e) {
            s+="\n FileNotFoundException " + e.getMessage();
            err=1;
        }
        catch (IOException e) {
            s+="\n IOException" + e.getMessage();
            err=2;
        }
        if(err==0) {
            s+="\n DBをSDカードにコピーしました。";
        }
    }
}
