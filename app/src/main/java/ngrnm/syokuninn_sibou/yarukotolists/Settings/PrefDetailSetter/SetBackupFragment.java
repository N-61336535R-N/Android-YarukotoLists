package ngrnm.syokuninn_sibou.yarukotolists.Settings.PrefDetailSetter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import ngrnm.syokuninn_sibou.yarukotolists.Database.BackupUtils.Backuper;
import ngrnm.syokuninn_sibou.yarukotolists.Database.BackupUtils.Restorer;
import ngrnm.syokuninn_sibou.yarukotolists.MainActivity;
import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Utils.Dialogs.GuruguruDialog;
import ngrnm.syokuninn_sibou.yarukotolists.Utils.Dialogs.MoldAlertDialogFragment;
import ngrnm.syokuninn_sibou.yarukotolists.Utils.Dialogs.mkMoldDialog;
import ngrnm.syokuninn_sibou.yarukotolists.Utils.DirPicker.DirSelectDialog;
import ngrnm.syokuninn_sibou.yarukotolists.Utils.FilePicker.FileSelectionDialog;

/**
 * Created by ryo on 2018/03/04.
 */

public class SetBackupFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static int CODE_BACKUP = 1618585;
    private static int CODE_RESTORE = 5376868;
    
    public static final String JSON_SimpleAdd = "json-add";
    public static final String JSON_SelectAdd = "json-slectadd";
    public static final String JSON_Rollback = "json-rollback";
    public static final String Realm_Rollback = "realm-rollback";
    
    
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.setting_p8_backup, rootKey);
        
        // pref ボタンの onClick の設定
        setBackupPath();
        setBackupButton();
        
        setRestoreButton();
    }
    
    private void setBackupPath() {
        final String prefKey = "prefKey_BackupPath";
        
        PreferenceScreen prefScreen = (PreferenceScreen)findPreference(prefKey);
        final SharedPreferences sp = prefScreen.getSharedPreferences();
        final String defaultPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(prefKey, defaultPath);
        editor.apply();
        setSummaries(sp);
        
        prefScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // 外部ストレージ読み込みパーミッション要求
                requestReadExternalStoragePermission();
    
                DirSelectDialog SDFrag = new DirSelectDialog();
                Bundle bundleSDir = new Bundle();
                bundleSDir.putString("dirPath", sp.getString(prefKey, defaultPath));
                SDFrag.setArguments(bundleSDir);
                
                // ファイル選択ダイアログを表示
                SDFrag.setOnDirSelectDialogListener(new DirSelectDialog.OnDirSelectDialogListener() {
                    /**
                     * ディレクトリ選択完了イベント
                     * 決定 ボタンを押すと実行される。
                     */
                    @Override
                    public void onClickDirSelect(String filePath) {
                        if (filePath != null) {
                            // 選択したディレクトリを、preferenceに保存
                            SharedPreferences.Editor editor = getPreferenceScreen().getSharedPreferences().edit();
                            editor.putString(prefKey, filePath);
                            editor.apply();
                        } else {
                            throw new NullPointerException("ファイルパスの取得に失敗しました。");
                        }
                    }
                });
                
                // 表示
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                SDFrag.show(ft, "dialog");
                return true;
            }
        });
    }
    
    private void setBackupButton() {
        final String prefKey = "prefKey_goBackup";
        
        // PreferenceScreenからのIntent
        PreferenceScreen prefScreen = (PreferenceScreen)findPreference(prefKey);
        prefScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            /**
             * 設定画面：「バックアップする」を押した時。
             */
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // 外部ストレージ読み込みパーミッション要求
                requestReadExternalStoragePermission();
    
                SharedPreferences sharedPrefs = preference.getSharedPreferences();
                final String destiPath = sharedPrefs.getString("prefKey_BackupPath", "Error");
                /*
                if (destiPath.equals("Error")) {
                    //ダイアログを表示
                    String title = "バックアップ失敗",
                            message = "バックアップ先を指定してください。";
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    mkMoldDialog.mkCheckDialogFragm(title, message).show(ft, "dialog");
                    return false;
                }*/
                
                final Backuper backuper;
                try {
                    backuper = new Backuper(destiPath);
                } catch (FileNotFoundException e) {
                    //ダイアログを表示
                    String title = "バックアップ失敗",
                            message = "バックアップ先が見つかりませんでした。";
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    mkMoldDialog.mkCheckDialogFragm(title, message).show(ft, "dialog");
                    // Toast を表示
                    Toast.makeText(getContext(), "バックアップ失敗", Toast.LENGTH_LONG).show();
                    return false;
                }
                
                MoldAlertDialogFragment kakuninFragment = new MoldAlertDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", "確認");
                bundle.putString("message", "バックアップを実行します。\nよろしいですか？");
                kakuninFragment.setArguments(bundle);
                kakuninFragment.setOnClickedPositiveButtonListener(true, new MoldAlertDialogFragment.OnClickedPositiveButtonListener() {
                    @Override
                    public void OnClickedPositiveButtonListener() {
                        // 作業進行中のDialogを表示
                        GuruguruDialog GDlog = new GuruguruDialog(getActivity());
                        GDlog.setDoInBackgroundListener(new GuruguruDialog.DoInBackgroundListener() {
                            @Override
                            public void DoInBackgroundListener() {
                                // バックアップ実行
                                try {
                                    backuper.backup();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                
                                String title = "バックアップ完了！",
                                        message = destiPath + " にバックアップしました。";
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                mkMoldDialog.mkCheckDialogFragm(title, message).show(ft, "dialog");
                            }
                        });
                        // 非同期(スレッド)処理の実行
                        GDlog.execute();
                    }
                });
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                kakuninFragment.show(ft, "dialog");
                
                return true;
            }
        });
    }
    
    
    private void setRestoreButton() {
        final String prefKey = "prefKey_goRestore";
        
        PreferenceScreen prefScreen = (PreferenceScreen)findPreference(prefKey);
        final SharedPreferences sp = prefScreen.getSharedPreferences();
        final String defaultPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(prefKey, defaultPath);
        editor.apply();
    
        prefScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            /**
             * 設定画面：「復元する」ボタン を押した時
             */
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // 外部ストレージ読み込みパーミッション要求
                requestReadExternalStoragePermission();
    
                String fpath = sp.getString(prefKey, defaultPath);
                Bundle bundleSDir = new Bundle();
                bundleSDir.putString("dirPath", fpath);
                FileSelectionDialog dlg = new FileSelectionDialog(getContext(), "zip", new FileSelectionDialog.OnFileSelectListener() {
                    /**
                     * ファイル選択Dialog：「～.zip」押した時
                     */
                    @Override
                    public void onFileSelect(File F) {
                        final String filePath = F.getAbsolutePath();
                        
                        // まずはアクセス可能かを確認。
                        final Restorer restore;
                        try {
                            restore = new Restorer(filePath, sp.getString("prefkey_selectlist_restore_mode", "json"));
                        } catch (FileNotFoundException e) {
                            //ダイアログを表示
                            String title = "失敗",
                                    message = "ファイルを取得できませんでした。";
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            mkMoldDialog.mkCheckDialogFragm(title, message).show(ft, "dialog");
                            // Toast を表示
                            Toast.makeText(getContext(), "復元 失敗", Toast.LENGTH_LONG).show();
                            return;
                        }
    
    
                        String selected = sp.getString("prefkey_selectlist_restore_mode", "");
                        // 確認ダイアログを準備。
                        MoldAlertDialogFragment kakuninFragment = new MoldAlertDialogFragment();
                        String text = restoreExplains.get(selected);
                        Bundle bundle = new Bundle();
                        if (!selected.equals(JSON_Rollback) && !selected.equals(Realm_Rollback)) {
                            bundle.putString("title", "確認");
                            bundle.putString("message", text+"\nを実行します！\nよろしいですか？");
                            kakuninFragment.setArguments(bundle);
                            kakuninFragment.setOnClickedPositiveButtonListener(true, new MoldAlertDialogFragment.OnClickedPositiveButtonListener() {
                                @Override
                                public void OnClickedPositiveButtonListener() {
                                    // 作業進行中のDialogを表示
                                    GuruguruDialog GDlog = new GuruguruDialog(getActivity());
                                    GDlog.setDoInBackgroundListener(new GuruguruDialog.DoInBackgroundListener() {
                                        @Override
                                        public void DoInBackgroundListener() {
                                            // 復元を実行
                                            try {
                                                restore.restoreAll();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            String title = "復元 完了！",
                                                    message = filePath + " から データベース に追加しました。";
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            mkMoldDialog.mkCheckDialogFragm(title, message).show(ft, "dialog");
                                        }
                                    });
                                    // 非同期(スレッド)処理の実行
                                    GDlog.execute();
                                }
                            });
                        } else {
                            bundle.putString("title", "確認");
                            bundle.putString("message", text+"\nを実行します！\n復元の際に、自動で再起動しますが、しばらくお待ち下さい。\n実行してよろしいですか？");
                            kakuninFragment.setArguments(bundle);
                            kakuninFragment.setOnClickedPositiveButtonListener(true, new MoldAlertDialogFragment.OnClickedPositiveButtonListener() {
                                @Override
                                public void OnClickedPositiveButtonListener() {
                                    // 復元を実行
                                    try {
                                        // DataBaseSetting のフラグを立てるだけ。
                                        restore.restoreAll();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    int waitperiod = 1000; // 5sec
                                    restart(getActivity().getApplication(), waitperiod);  // 再起動する
                                }
                            });
                        }
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        kakuninFragment.show(ft, "dialog");
    
                    }
                });
                dlg.show(new File(fpath));
            
                return true;
            }
        });
    
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void restart(Context cnt, int period){
        // intent 設定で自分自身のクラスを設定
        Intent mainActivity = new Intent(cnt, MainActivity.class);
        
        // PendingIntent , ID=0
        PendingIntent pendingIntent = PendingIntent.getActivity(cnt,
                0, mainActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        
        // AlarmManager のインスタンス生成
        AlarmManager alarmManager = (AlarmManager)cnt.getSystemService(Context.ALARM_SERVICE);
        
        // １回のアラームを現在の時間から period 秒後 に実行させる
        if(alarmManager != null){
            long trigger = System.currentTimeMillis() + period;
            alarmManager.setExact(AlarmManager.RTC, trigger, pendingIntent);
        }
        
        // アプリ終了
        getActivity().finish();
    }
    
    
    
    private static final Map<String, String> restoreExplains = new HashMap<String, String>() {
        {
            put(JSON_SimpleAdd, "【追加】    Simple add");
            put(JSON_SelectAdd, "【選択追加】Select add");
            put(JSON_Rollback, "【入れ替え】Rollback (json)");
            put(Realm_Rollback, "【入れ替え】Rollback (realm)");
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        sp.registerOnSharedPreferenceChangeListener(this);
        setSummaries(sp);
    }
    private void setSummaries(final SharedPreferences sp) {
        // 取得方法
        //final boolean onOff = sp.getBoolean("on_off_preference", false);
        String key = "prefKey_BackupPath";
        findPreference(key).setSummary( sp.getString(key, "") );
        
        key = "prefkey_selectlist_restore_mode";
        String selected = sp.getString(key, "");
        String text = restoreExplains.get(selected);
        switch (selected) {
            case JSON_SimpleAdd:
                text += "：\n復元したデータを「全て追加」します。";
                
                break;
            case JSON_SelectAdd:
                text += "：\n復元したデータを「選んで追加」します。\n※ 未実装";
                
                break;
            case JSON_Rollback:
                text += "：\n現在のデータを全て削除し、復元したデータを\n追加します。（復元の前にバックアップすることをおすすめします）";
        
                break;
            case Realm_Rollback:
                text += "：\nデータベースごと入れ替えます。\n※[重要] 現在のデータは全て削除されます。\n(復元の前にバックアップをおすすめします)";
                break;
        }
        findPreference(key).setSummary(text);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setSummaries(sharedPreferences);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    
    
    /**
     * ファイルアクセス権限の確認Dialogを表示する
     */
    // 定数
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1; // 外部ストレージ読み込みパーミッション要求時の識別コード
    
    // 外部ストレージ読み込みパーミッション要求
    private void requestReadExternalStoragePermission() {
        if( PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission( getContext(), Manifest.permission.READ_EXTERNAL_STORAGE ) ) {
            // パーミッションは付与されている
            return;
        }
        // パーミッションは付与されていない。
        // パーミッションリクエスト
        ActivityCompat.requestPermissions( getActivity()
                , new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }
                , REQUEST_PERMISSION_READ_EXTERNAL_STORAGE 
        );
    }
    
    // パーミッション要求ダイアログの操作結果
    @Override
    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults ) {
        switch( requestCode ) {
            case REQUEST_PERMISSION_READ_EXTERNAL_STORAGE:
                if( grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED ) {
                    // 許可されなかった場合
                    Toast.makeText( getContext(), "Permission denied.", Toast.LENGTH_SHORT ).show();
                    return;
                }
                break;
            default:
                break;
        }
    }
}
