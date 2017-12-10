package ngrnm.syokuninn_sibou.yarukotolists.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;

import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils.Backuper;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils.DirSelectDialog;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils.DirSelectDialog.OnDirSelectDialogListener;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils.GuruguruDialog;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils.MoldAlertDialogFragment;


/**
 * int SettingConsts.setPref_xmls[SettingConsts.getPref_num()]
 * によって、移動する設定画面を切り替えてる。
 */

public class SettingFragment extends PreferenceFragmentCompat implements OnDirSelectDialogListener, SharedPreferences.OnSharedPreferenceChangeListener {
    
    private OnFragmentInteractionListener mListener;
    
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(SettingConsts.setPref_xmls[SettingConsts.getPref_num()], rootKey);
    
        if (SettingConsts.getPref_num() == SettingConsts.KOUMOKU_num-1) {
            setBackupPath();
            setBackupButton();
        }
    }
    
    private void setBackupPath() {
        PreferenceScreen prefScreen = (PreferenceScreen)findPreference("prefKey_BackupPath");
        final SharedPreferences sp = prefScreen.getSharedPreferences();
        final String defaultPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("prefKey_BackupPath", defaultPath);
        editor.apply();
        setSummaries(sp);
        
        prefScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
    
                DirSelectDialog SDFrag = new DirSelectDialog();
                Bundle bundleSDir = new Bundle();
                bundleSDir.putString("dirPath", sp.getString("prefKey_BackupPath", defaultPath));
                SDFrag.setArguments(bundleSDir);
    
                // ファイル選択ダイアログを表示
                SDFrag.setOnDirSelectDialogListener(SettingFragment.this);
    
                // 表示
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SDFrag.show(ft, "dialog");
                return true;
            }
        });
    }
    /**
     * ディレクトリ選択完了イベント
     */
    @Override
    public void onClickDirSelect(String filePath) {
        if (filePath != null) {
            // 選択したディレクトリを、preferenceに保存
            SharedPreferences.Editor editor = getPreferenceScreen().getSharedPreferences().edit();
            editor.putString("prefKey_BackupPath", filePath);
            editor.apply();
        } else {
            throw new NullPointerException("ファイルパスの取得に失敗しました。");
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        sp.registerOnSharedPreferenceChangeListener(this);
        setSummaries(sp);
    }
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    private void setSummaries(final SharedPreferences sp) {
        // 取得方法
        //final boolean onOff = sp.getBoolean("on_off_preference", false);
        final String text = sp.getString("prefKey_BackupPath", "");
        findPreference("prefKey_BackupPath").setSummary(text);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setSummaries(sharedPreferences);
    }
    

    
    private void setBackupButton() {
        // PreferenceScreenからのIntent
        PreferenceScreen prefScreen = (PreferenceScreen)findPreference("prefKey_goBackup");
        prefScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
    
                SharedPreferences sharedPrefs = preference.getSharedPreferences();
                final String destiPath = sharedPrefs.getString("prefKey_BackupPath", "Error");
                if (destiPath.equals("Error")) {
                    //ダイアログを表示
                    String title = "バックアップ失敗",
                            message = "バックアップ先を指定してください。";
                    makeADialog(title, message);
                    // Toast を表示
                    Toast.makeText(getContext(), "バックアップ失敗", Toast.LENGTH_LONG).show();
                    return false;
                }
            
                final Backuper backuper;
                try {
                    backuper = new Backuper(destiPath);
                } catch (FileNotFoundException e) {
                    //ダイアログを表示
                    String title = "バックアップ失敗",
                            message = "バックアップ先が見つかりませんでした。";
                    makeADialog(title, message);
                    // Toast を表示
                    Toast.makeText(getContext(), "バックアップ失敗", Toast.LENGTH_LONG).show();
                    return false;
                }
                
                
                MoldAlertDialogFragment kakuninFragment = new MoldAlertDialogFragment();
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
                                makeADialog(title, message);
                                // Toast を表示
                                //Toast.makeText(getContext(), "バックアップ完了", Toast.LENGTH_LONG).show();
                            }
                        });
                        // 非同期(スレッド)処理の実行
                        GDlog.execute();
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString("title", "確認");
                bundle.putString("message", "バックアップを実行します。\nよろしいですか？");
                kakuninFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                kakuninFragment.show(ft, "dialog");
    
                return true;
            }
        });
    }
    
    
    
    
    
    private void makeADialog(String title, String message) {
        MoldAlertDialogFragment fragment = new MoldAlertDialogFragment();
        fragment.setOnClickedPositiveButtonListener(false, new MoldAlertDialogFragment.OnClickedPositiveButtonListener() {
            @Override
            public void OnClickedPositiveButtonListener() {}
        });
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        fragment.show(ft, "dialog");
    }
    
    
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    
    
    
    
    public interface OnFragmentInteractionListener {}
}
