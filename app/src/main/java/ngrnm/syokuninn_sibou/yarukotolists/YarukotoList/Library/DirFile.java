package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library;

import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * ディレクトリ構造でファイルを管理したい時に使う
 * ファイル読み込み・書き込みの簡易ライブラリ
 */

public class DirFile {
    private static String rootDir;
    
    // [引数] parentDir = context.getfilesdir().getpath();
    public static void setDirFile(String parentDir) {
       rootDir = parentDir + "/";
    }
    
    
    public static void makeDirFile(String[] dirs) {
        for (String dir : dirs) {
            new File(rootDir+dir).mkdir();
        }
    }
    
    // 文字列連結（変数2つ以上）
    private static String combinStr(String parentDir, String[] dirs) {
        StringBuilder buff = new StringBuilder();
        buff.append(parentDir);
        buff.append("/");
        for (String dirName : dirs) {
            buff.append(dirName);
            buff.append("/");
        }
        return buff.toString();
    }
    
    
    public static String readAll(File file) {
        BufferedInputStream bis = null;
        byte[] data = new byte[(int)file.length()];
        
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            try {
                bis.read(data);
                bis.close();
            } catch (IOException e) {
                System.out.println("[IOException]");
            }
        } catch (FileNotFoundException e) {
            System.out.println("[FileNotFound] ファイル読み込みに失敗しました");
        }
        
        String fstr = null;
        try {
            fstr = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fstr;
    }
    
    
    public static void writeAll(File file, String data) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(data);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
