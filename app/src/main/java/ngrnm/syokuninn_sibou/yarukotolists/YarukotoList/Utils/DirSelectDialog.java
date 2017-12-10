package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

import ngrnm.syokuninn_sibou.yarukotolists.R;


/**
 * ディレクトリ選択ダイアログ
 */
public class DirSelectDialog extends DialogFragment implements OnClickListener {
    
    Bundle bundleSDir;
    
    /** リスナー */
    private OnDirSelectDialogListener listener = null;
    
    /** ファイル情報 */
    private File fileData = null;
    
    /** 表示中のファイル情報リスト */
    private List<File> viewFileDataList = null;
    
    
    /**
     * 選択イベント
     *
     * @param dialog ダイアログ
     * @param which 選択位置
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        show(this.viewFileDataList.get(which).getAbsolutePath() + "/");
    }
    
    private void show(String dirPath) {
        DirSelectDialog SDFrag = new DirSelectDialog();
        Bundle bundleSDir = new Bundle();
        bundleSDir.putString("dirPath", dirPath);
        SDFrag.setArguments(bundleSDir);
    
        // ファイル選択ダイアログを表示
        SDFrag.setOnDirSelectDialogListener(listener);
    
        // 表示
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        SDFrag.show(ft, "dialog");
    }
    
    /**
     * ダイアログを表示
     *
     *  dirPath : ディレクトリのパス
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bundleSDir = savedInstanceState;
        final String dirPath = getArguments().getString("dirPath");
        
        // ダイアログを生成
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(dirPath);
        dialog.setIcon(R.drawable.directory_icon);
        dialog.setItems(getFList(dirPath).toArray(new String[0]), this);
        
        dialog.setPositiveButton("決 定", new OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int value) {
                
                DirSelectDialog.this.listener.onClickDirSelect(dirPath);
            }
        });
        
        dialog.setNeutralButton("上 へ", new OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int value) {
                
                if (!"/".equals(dirPath)) {
                    
                    String dirPathNew = dirPath.substring(0, dirPath.length() - 1);
                    dirPathNew = dirPathNew.substring(0, dirPathNew.lastIndexOf("/") + 1);
                    
                    // 1つ上へ
                    show(dirPathNew);
                    
                } else {
                    
                    // 現状維持
                    show(dirPath);
                }
            }
        });
        
        dialog.setNegativeButton("キャンセル", new OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int value) {
                
                DirSelectDialog.this.listener.onClickDirSelect(null);
            }
        });
        
        return dialog.create();
    }
    
    
    private List<String> getFList(String dirPath) {
        // ファイル情報
        this.fileData = new File(dirPath);
    
        // ファイルリスト
        File[] fileArray = this.fileData.listFiles();
    
        // 名前リスト
        List<String> nameList = new ArrayList<String>();
    
        if (fileArray != null) {
        
            // ファイル情報マップ
            Map<String, File> map = new HashMap<String, File>();
        
            for (File file : fileArray) {
            
                if (file.isDirectory()) {
                
                    nameList.add(file.getName() + "/");
                    map.put(nameList.get(map.size()), file);
                }
            }
        
            // ソート
            Collections.sort(nameList);
        
            // ファイル情報リスト
            this.viewFileDataList = new ArrayList<File>();
        
            for (int i = 0; i < nameList.size(); i++) {
            
                this.viewFileDataList.add(map.get(nameList.get(i)));
            }
        }
        return nameList;
    }
    
    
    /**
     * リスナーを設定
     *
     * @param listener 選択イベントリスナー
     */
    public void setOnDirSelectDialogListener(OnDirSelectDialogListener listener) {
        
        this.listener = listener;
    }
    
    /**
     * ボタン押下インターフェース
     */
    public interface OnDirSelectDialogListener {
        
        /**
         * 選択イベント
         *
         * @param filePath ファイル
         */
        public void onClickDirSelect(String filePath);
    }
}
