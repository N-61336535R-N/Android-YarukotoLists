package ngrnm.syokuninn_sibou.yarukotolists.Utils.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;




/*
 * 削除ダイアログを生成する内部クラス
 * 内部クラスは外部クラスのインスタンスを直接参照できないため，
 * Activity#getActivity()で外部クラスのインスタンスを取得している．
 */
public class DeleteDialog extends DialogFragment {
    /* 選択したListViewアイテム */
    private String selectedItemName = null;
    private int posi;
    
    private static final int RESULTCODE_OK = 0;
    
    
    // 削除ダイアログの作成．
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("確認");
        builder.setMessage("「"+ selectedItemName +"」を削除すると 元に戻せません！\n本当によろしいですか？");
        
        // positiveを選択した場合の処理．
        // リスナーはDialogINterface#onClickListener()
        // を使うことに注意．
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getActivity().getIntent();
                intent.putExtra("posi", posi);
                getTargetFragment()
                        .onActivityResult(getTargetRequestCode(), RESULTCODE_OK, intent);
            }
        });
        return builder.create();
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
    
    
}
