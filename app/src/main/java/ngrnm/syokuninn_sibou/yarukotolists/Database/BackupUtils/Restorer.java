package ngrnm.syokuninn_sibou.yarukotolists.Database.BackupUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;

/**
 * Created by ryo on 2017/10/01.
 */

public class Restorer {
    private String mode;
    private File realmRootDir = new File(Consts.rootPath + Consts.realmDBname);
    private String destiZipPath;
    private ZipCompressUtils zipCU = new ZipCompressUtils();
    
    public Restorer(String destiPath, String mode) throws FileNotFoundException {
        // 入力元のDirectoryとの接続を確立する。
        this.destiZipPath = destiPath;
        File destiDir = new File(destiPath);
        if (!destiDir.exists()) {
            throw new FileNotFoundException();
        }
        
        this.mode = mode;
    }
    
    public void restoreAll() throws Exception {
        /* 指定された zip ファイルから、Backup データを復元 */
        switch (mode) {
            case "replace":
                /* YDB.realm 自体を入れ替える。 */
                
                break;
            case "JSON":
                InputStream ins;
                for (Class yclass : Consts.realmClasses) {
                    ins = zipCU.getZipFileInputStream(destiZipPath, yclass.getName()+".json");
                    convertJSONToRealmData(ins, yclass);
                }
                break;
            default:
                throw new IllegalArgumentException("その復元モードは未実装です。。。");
        }
    }
    
    // JSON ファイルから読み込んで Realm に登録。
    private void convertJSONToRealmData(InputStream instream, Class yclass) throws IOException {
        Realm realm = Realm.getDefaultInstance();
        
        // Open a transaction to store items into the realm
        realm.beginTransaction();
        try {
            realm.createAllFromJson(yclass, instream);
            realm.commitTransaction();
        } catch (IOException e) {
            // Remember to cancel the transaction if anything goes wrong.
            realm.cancelTransaction();
        } finally {
            if (instream != null) {
                instream.close();
            }
        }
    }
    
    
    
}
