package ngrnm.syokuninn_sibou.yarukotolists.Database.BackupUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles.YCategorySerializer;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.Utiles.YCtgryOrderSerializer;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;

/**
 * Created by ryo on 2017/10/01.
 */

public class Backuper {
    private File backupTmpDir;
    private File outputDir;
    private ZipCompressUtils zipCU = new ZipCompressUtils();
    
    public Backuper(String outputPath) throws FileNotFoundException {
        // 出力先のDirectoryとの接続を確立する。
        this.outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            throw new FileNotFoundException();
        }
    }
    
    public void backup() throws Exception {
        /* 現在の日付の取得 */
        //Calendarクラスのオブジェクトを生成する
        Calendar cl = Calendar.getInstance();
        //フォーマットを指定する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        System.out.println(sdf.format(cl.getTime()));
    
        backupTmpDir = new File(Consts.rootPath+"BackupDatas/"+sdf.format(cl.getTime()) + "/YDB/");
        if (!backupTmpDir.exists())  backupTmpDir.mkdirs();
        
        /* tmp ディレクトリに、Realm のデータベースを JSON に変換したファイル群を保存 */
        convertRealmDBsToJSON();
        
    
        // originF 以下の全フォルダを zip圧縮
        // outputDir に保存する
        LinkedList<File> zipFilesList = new LinkedList<>();
        zipFilesList.add( backupTmpDir );
        zipFilesList.add(new File(Consts.rootPath + "ctg_img"));
        zipFilesList.add(new File(Consts.rootPath + "YDB.realm"));
        zipCU.compressFiles(zipFilesList, outputDir.getPath()+"/BackupYDB["+sdf.format(cl.getTime())+"].zip");
        
        // tmp ファイルを削除。
        recursiveDeleteFile(backupTmpDir);
    }
    
    // クラスごとに1ファイルを作るイメージ。
    private void convertRealmDBsToJSON() throws JsonProcessingException {
        /* Realm の初期化 */
        // このスレッドのためのRealmインスタンスを取得
        Realm realm = Realm.getDefaultInstance();
    
        Gson gson = null;
        try {
            gson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }
                
                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .registerTypeAdapter(Class.forName("io.realm.YCategoryRealmProxy"), new YCategorySerializer())
                    .registerTypeAdapter(Class.forName("io.realm.YCtgryOrderRealmProxy"), new YCtgryOrderSerializer())
                    .create();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    
        for (Class realmCass : Consts.realmClasses) {
            RealmResults rresults = realm.where(realmCass).findAll();
    
            try {
                FileWriter fw = new FileWriter(backupTmpDir.getPath() +"/"+ realmCass.getName() + ".json", true);
                BufferedWriter bw = new BufferedWriter(fw);
    
                for (Object robj : rresults) {
                    String testJsonString = gson.toJson(robj);
                    bw.write(testJsonString);
                }
    
                bw.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        
    /*
        //YCtgryOrder yctgO = realm.where(YCtgryOrder.class).findFirst();
        testJSON tJSON = new testJSON();
        tJSON.id = 35;
        tJSON.getTestRealmList().add("testです！");
        try {
            FileWriter fos = new FileWriter(backupTmpDir.getPath() + "/YCtgryOrder.json", true);
            BufferedWriter bw = new BufferedWriter(fos);
            
            //String testJsonString = mapper.writeValueAsString(tJSON);
            String testJsonString = new Gson().toJson(tJSON);
            Log.d("sample", testJsonString);
            bw.write(testJsonString);
            
            bw.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }*/
        
        realm.close();
    }
    
    
    
    private static void recursiveDeleteFile(final File file) throws Exception {
        // 存在しない場合は処理終了
        if (!file.exists()) {
            return;
        }
        // 対象がディレクトリの場合は再帰処理
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                recursiveDeleteFile(child);
            }
        }
        // 対象がファイルもしくは配下が空のディレクトリの場合は削除する
        file.delete();
    }
}