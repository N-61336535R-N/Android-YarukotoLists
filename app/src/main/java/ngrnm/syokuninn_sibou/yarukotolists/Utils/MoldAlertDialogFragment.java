package ngrnm.syokuninn_sibou.yarukotolists.Utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by M.R on 2017/06/27.
 */

public final class MoldAlertDialogFragment extends DialogFragment {
    private OnClickedPositiveButtonListener listener;
    private boolean DoNegativeButtonSet = false;
    
    /**
     private void makeADialog(String title, String message) {
         DialogFragment fragment = new MoldAlertDialogFragment();
         Bundle bundle = new Bundle();
         bundle.putString("title", title);
         bundle.putString("message", message);
         fragment.setArguments(bundle);
         FragmentTransaction ft = getFragmentManager().beginTransaction();
         fragment.show(ft, "dialog");
     }
     ↑こんな感じで使う。
 
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //fragment.show(ft, "dialog"); のタイミングで、Fragment本体が拾いに来るイメージ
        AlertDialog.Builder adlog = new AlertDialog
                .Builder(getContext())
                .setTitle(getArguments().getString("title"))
                .setMessage(getArguments().getString("message"))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dismiss();
                        MoldAlertDialogFragment.this.listener.OnClickedPositiveButtonListener();
                    }
                });
                if (DoNegativeButtonSet) {
                    // OK ボタンに役割がある場合は、何もしない"キャンセル"ボタンを作成。
                    adlog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                }
        return adlog.create();
    }
    
    
    /**
     * リスナーを設定
     *
     * @param listener 選択イベントリスナー
     */
    public void setOnClickedPositiveButtonListener(boolean DoNegativeButtonSet, OnClickedPositiveButtonListener listener) {
        this.listener = listener;
        this.DoNegativeButtonSet = DoNegativeButtonSet;
    }
    
    /**
     * ボタン押下インターフェース
     */
    public interface OnClickedPositiveButtonListener {
        /**
         * 選択イベント
         */
        public void OnClickedPositiveButtonListener();
    }
    
}