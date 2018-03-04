package ngrnm.syokuninn_sibou.yarukotolists;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YCategory;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.Consts;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Library.DirFile;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.YCategoryFragment;

/**
 * Created by ryo on 2017/09/03.
 */

public class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ycategory);
    
        /* ルートフォルダを、Consts に登録 */
        Consts.rootPath = getApplicationContext().getFilesDir().getPath() + "/";
        // DirFile の初期化（ルートディレクトリの設定）
        DirFile.setDirFile(Consts.rootPath);
    
        // Realm 全体の初期化［一度でOK］
        Realm.init(this);
        RealmConfiguration config
                = new RealmConfiguration
                .Builder()
                .name(Consts.realmDBname)
                .deleteRealmIfMigrationNeeded()
                .build();
        //Realm.deleteRealm(config);
        Realm.setDefaultConfiguration(config);
        
        /* Realm の初期化 */
        // このスレッドのためのRealmインスタンスを取得
        Realm realm = Realm.getDefaultInstance();
        // categoryが一つも登録されていない場合、デモを一つ追加。
        if (realm.where(YCategory.class).findAll().size() == 0) {
            YCategoryFragment.add(realm, "Category 0");
        }
        realm.close();
    
        // やることリスト（編集）画面に移動
        //Intent intent = new Intent(MainActivity.this, YCategoryActivity.class);
        Intent intent = new Intent(MainActivity.this, YarukotoMainActivity.class);
        startActivity(intent);
    
        finish();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
/*    
    public static final String TAG = MigrationExampleActivity.class.getName();
    
    private LinearLayout rootLayout = null;
    private Realm realm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_migration_example);
        
        rootLayout = ((LinearLayout) findViewById(R.id.container));
        rootLayout.removeAllViews();
        
        // 3 versions of the databases for testing. Normally you would only have one.
        copyBundledRealmFile(this.getResources().openRawResource(R.raw.default0), "default0.realm");
        copyBundledRealmFile(this.getResources().openRawResource(R.raw.default1), "default1.realm");
        copyBundledRealmFile(this.getResources().openRawResource(R.raw.default2), "default2.realm");
        
        // When you create a RealmConfiguration you can specify the version of the schema.
        // If the schema does not have that version a RealmMigrationNeededException will be thrown.
        RealmConfiguration config0 = new RealmConfiguration.Builder()
                .name("default0.realm")
                .schemaVersion(3)
                .build();
        
        // You can then manually call Realm.migrateRealm().
        try {
            Realm.migrateRealm(config0, new Migration());
        } catch (FileNotFoundException ignored) {
            // If the Realm file doesn't exist, just ignore.
        }
        realm = Realm.getInstance(config0);
        showStatus("Default0");
        showStatus(realm);
        realm.close();
        
        // Or you can add the migration code to the configuration. This will run the migration code without throwing
        // a RealmMigrationNeededException.
        RealmConfiguration config1 = new RealmConfiguration.Builder()
                .name("default1.realm")
                .schemaVersion(3)
                .migration(new Migration())
                .build();
        
        realm = Realm.getInstance(config1); // Automatically run migration if needed
        showStatus("Default1");
        showStatus(realm);
        realm.close();
        
        // or you can set .deleteRealmIfMigrationNeeded() if you don't want to bother with migrations.
        // WARNING: This will delete all data in the Realm though.
        RealmConfiguration config2 = new RealmConfiguration.Builder()
                .name("default2.realm")
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();
        
        realm = Realm.getInstance(config2);
        showStatus("default2");
        showStatus(realm);
        realm.close();
    }
    
    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(this.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String realmString(Realm realm) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Person person : realm.where(Person.class).findAll()) {
            stringBuilder.append(person.toString()).append("\n");
        }
        
        return (stringBuilder.length() == 0) ? "<data was deleted>" : stringBuilder.toString();
    }
    
    private void showStatus(Realm realm) {
        showStatus(realmString(realm));
    }
    
    private void showStatus(String txt) {
        Log.i(TAG, txt);
        TextView tv = new TextView(this);
        tv.setText(txt);
        rootLayout.addView(tv);
    }
    */
}
