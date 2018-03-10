package ngrnm.syokuninn_sibou.yarukotolists.Database.BackupUtils;

import android.util.JsonReader;
import android.util.JsonToken;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import io.realm.Realm;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.DataBaseSettings;
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
            case SetBackupFragment.Realm_Rollback:
                /* YDB.realm 自体を入れ替える。 */
                
                break;
                
            case SetBackupFragment.JSON_SimpleAdd:
                for (Class yclass : Consts.realmClasses) {
                    ins = zipCU.getZipFileInputStream("YDB/"+yclass.getName()+".json");
                    convertJSONToRealmData(ins, yclass, "data");
                }
                for (Class yclass : Consts.realmOptClasses) {
                    ins = zipCU.getZipFileInputStream("YDB/"+yclass.getName()+".json");
                    convertJSONToRealmData(ins, yclass, "opt");
                }
                break;
                
            case SetBackupFragment.JSON_SelectAdd:
                for (Class yclass : Consts.realmClasses) {
                    ins = zipCU.getZipFileInputStream("YDB/"+yclass.getName()+".json");
                    convertJSONToRealmData(ins, yclass, "data");
                }
                for (Class yclass : Consts.realmOptClasses) {
                    ins = zipCU.getZipFileInputStream("YDB/"+yclass.getName()+".json");
                    convertJSONToRealmData(ins, yclass, "opt");
                }
                break;
                
            case SetBackupFragment.JSON_Rollback:
                setReserveRollback_inDBSetting(SetBackupFragment.JSON_Rollback);
                break;
                
            default:
                throw new IllegalArgumentException("その復元モードは未実装です...");
        }
        zipCU.close();
    }
    private void setReserveRollback_inDBSetting(String pattern) {
        Realm realm = Realm.getDefaultInstance();
        DataBaseSettings dbSeter = realm.where(DataBaseSettings.class).equalTo("id", 0).findFirst();
        realm.beginTransaction();
        if (dbSeter == null) {
            dbSeter = new DataBaseSettings();
            realm.copyToRealm(dbSeter);
        }
        dbSeter.rollbackFlag = true;
        dbSeter.rollbackPattern = pattern;
        dbSeter.destiZipPath = destiZipPath;
        realm.commitTransaction();
        realm.close();
    }
    public static boolean getRollbackFlag() {
        Realm realm = Realm.getDefaultInstance();
        DataBaseSettings dbSeter = realm.where(DataBaseSettings.class).equalTo("id", 0).findFirst();
        boolean rollbackFlag = dbSeter.rollbackFlag;
        realm.close();
        return rollbackFlag;
    }
    
    private LinkedList<JSONObject> convertInStreamToJSONObject(InputStream instream, Class clazz) throws IOException {
        if (instream == null) throw new IOException("InputStream がnullです。");
        LinkedList<JSONObject> jsonObjects = new LinkedList<>();
        JsonReader reader = new JsonReader(new InputStreamReader(instream, "UTF-8"));
        
        Map<String, String> json_map;
        
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                json_map = new HashMap<String, String>();
                
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    String value;
                    if (reader.peek() != JsonToken.NULL) {
                        value = reader.nextString();
                    } else {
                        reader.skipValue();
                        value = null;
                    }
                    json_map.put(name, value);
                    
                }
                
                // primary key だった場合の処理
                String primVal = null;//json_map.get(Consts.primaryKeyName);
                if (primVal == null) {
                    // null なら、値を作る。
                    // （各 RealmObject クラスに、initPrim メソッドを作る。(Interface で取ってくる？)）
                }
                Realm realm = Realm.getDefaultInstance();
/*                while (realm.where(clazz).equalTo(Consts.primaryKeyName, primVal).findAll().size() > 0) {
                    //// 衝突が起こっているので、衝突が起きなくなるまで primary key の value を変え続ける。 ////
                    // 具体的には、primary はPathなので、title に (n) を追加して、Pathを作り直す。
                    // 作り直したら、json_map を更新する。
                }*/
            
                jsonObjects.add(new JSONObject(json_map));
    
            }
            reader.endArray();
        } finally {
            reader.close();
        }

    /*
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObjectFromJson(City.class, json);
            }
        });
    */
        return jsonObjects;
    }
    
    
    // JSON ファイルから読み込んで Realm に登録。
    private void convertJSONToRealmData(InputStream instream, Class yclass, String type) throws IOException {
        Realm realm = Realm.getDefaultInstance();
        
        // Open a transaction to store items into the realm
        realm.beginTransaction();
        try {
            switch (type) {
                case "data":
                    realm.createAllFromJson(yclass, instream);
                    //realm.createObjectFromJson(yclass, instream);
                    break;
                case "opt":
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
