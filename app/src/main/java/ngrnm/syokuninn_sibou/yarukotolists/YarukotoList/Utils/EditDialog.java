package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/*
 * 削除ダイアログを生成する内部クラス
 * 内部クラスは外部クラスのインスタンスを直接参照できないため，
 * Activity#getActivity()で外部クラスのインスタンスを取得している．
 */
public class EditDialog extends DialogFragment {
    /* 選択したListViewアイテム */
    private String selectedItemName = null;
    private int posi;
    
    private static final int RESULTCODE_OK = 1;
    private static final int RESULTCODE_NG = 0;
    
    
    // 削除ダイアログの作成．
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText editView = new EditText(getActivity());
        editView.setInputType(InputType.TYPE_CLASS_TEXT);
        editView.setText(selectedItemName);
        editView.setSelection(0, selectedItemName.length());
    
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("名前の変更")
                .setMessage("新しい名前を入力してください")
                .setView(editView)
                .setNegativeButton("キャンセル", (DialogInterface dialog, int whichButton) 
                        -> getTargetFragment().onActivityResult(getTargetRequestCode(), RESULTCODE_NG, getActivity().getIntent())
                );
        
        // positiveを選択した場合の処理．
        // リスナーはDialogInterface#onClickListener()
        // を使うことに注意．
        builder.setPositiveButton("OK", (dialog, which) -> {
            String newItemTitle = editView.getText().toString();
            Intent intent = getActivity().getIntent();
            intent.putExtra("newTitle", newItemTitle);
            intent.putExtra("posi", posi);
            getTargetFragment()
                    .onActivityResult(getTargetRequestCode(), RESULTCODE_OK, intent);
        });
        Dialog editDialog = builder.create();
        editDialog.setOnShowListener(arg0 -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(editView, 0);
        });
        
        return editDialog;
    }
    // 選択したアイテムをセットする．
    // HACK:削除ダイアログ自身に選択したアイテムを渡せないため，
    // ダイアログをユーザが呼び出した際に，Activityで選択した項目を保持しておく．
    public void setSelectedItemName(String selectedItemName, int posi) {
        this.selectedItemName = selectedItemName;
        this.posi = posi;
    }
    
    
    public static int getRCode_OK() {
        return RESULTCODE_OK;
    }
    public static int getResultcodeNg() {
        return RESULTCODE_NG;
    }
    
    
}
