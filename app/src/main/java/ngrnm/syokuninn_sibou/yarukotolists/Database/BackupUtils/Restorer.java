package ngrnm.syokuninn_sibou.yarukotolists.Database.BackupUtils;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.PrefDetailSetter.SetBackupFragment;

/**
 * Created by ryo on 2017/10/01.
 */

public class Restorer {
    private String mode;
    private File realmRootDir = new File(Consts.rootPath + Consts.realmDBname);
    private String destiZipPath;
    private ZipCompressUtils zipCU;
    
    public Restorer(String destiPath, String mode) throws FileNotFoundException {
        // 入力元のDirectoryとの接続を確立する。
        this.destiZipPath = destiPath;
        File destiDir = new File(destiPath);
        if (!destiDir.exists()) {
            throw new FileNotFoundException();
        }
        this.mode = mode;
    }
    
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public void restoreAll() throws Exception {
        /* 指定された zip ファイルから、Backup データを復元 */
        zipCU = new ZipCompressUtils(destiZipPath);
    
        InputStream ins;
        switch (mode) {
            case SetBackupFragment.Realm_REPLACE:
                /* YDB.realm 自体を入れ替える。 */
                
                break;
            case SetBackupFragment.JSON_SKIP:
                for (Class yclass : Consts.realmClasses) {
                    ins = zipCU.getZipFileInputStream("YDB/"+yclass.getName()+".json");
                    convertJSONToRealmData(ins, yclass, "data");
                }
                for (Class yclass : Consts.realmClasses) {
                    ins = zipCU.getZipFileInputStream("YDB/"+yclass.getName()+".json");
                    convertJSONToRealmData(ins, yclass, "order");
                }
                break;
            case SetBackupFragment.JSON_UPDATE:
                for (Class yclass : Consts.realmClasses) {
                    ins = zipCU.getZipFileInputStream("YDB/"+yclass.getName()+".json");
                    convertJSONToRealmData(ins, yclass, "data");
                }
                for (Class yclass : Consts.realmClasses) {
                    ins = zipCU.getZipFileInputStream("YDB/"+yclass.getName()+".json");
                    convertJSONToRealmData(ins, yclass, "order");
                }
                break;
            default:
                throw new IllegalArgumentException("その復元モードは未実装です...");
        }
        zipCU.close();
    }
    
    private void convertInStreamToJSONObject(InputStream instream) throws UnsupportedEncodingException {
        JsonReader reader = new JsonReader(new InputStreamReader(instream, "UTF-8"));
        JSONArray jarr;
        
        Map<String, String> city = new HashMap<String, String>();
        city.put("name", "København");
        city.put("votes", "9");
        final JSONObject json = new JSONObject(city);
    /*
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObjectFromJson(City.class, json);
            }
        });
    */
    }
    
    // JSON ファイルから読み込んで Realm に登録。
    private void convertJSONToRealmData(InputStream instream, Class yclass, String type) throws IOException {
        Realm realm = Realm.getDefaultInstance();
        
        // Open a transaction to store items into the realm
        realm.beginTransaction();
        try {
            switch (type) {
                case "data":
                    //realm.createAllFromJson(yclass, instream);
                    realm.createObjectFromJson(yclass, instream);
                    break;
                case "order":
                    // 今のところ、扱いは未定。 //
                    // せいぜい custom の順番のみなので、オプションで選ぶ感じにする？
                    break;
            }
            realm.commitTransaction();
        } catch (IOException e) {
            // Remember to cancel the transaction if anything goes wrong.
            realm.cancelTransaction();
            e.printStackTrace();
        } finally {
            if (instream != null) {
                instream.close();
            }
        }
    }
    
    
    
}
