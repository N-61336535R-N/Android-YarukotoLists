package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by ryo on 2017/11/05.
 */

public class GuruguruDialog extends AsyncTask<String, Void, Void> {
        
    private Activity m_Activity;
    public ProgressDialog m_ProgressDialog;
    private DoInBackgroundListener listener;
    
    
    public GuruguruDialog(Activity activity) {
        // 呼び出し元のアクティビティ
        this.m_Activity = activity;
    }
    
    /*
     * 実行前の事前処理
     */
    @Override
    protected void onPreExecute() {
        
        // プログレスダイアログの生成
        this.m_ProgressDialog = new ProgressDialog(this.m_Activity);
        // プログレスダイアログの設定
        this.m_ProgressDialog.setMessage("実行中...");  // メッセージをセット
        // プログレスダイアログの表示
        this.m_ProgressDialog.show();
        
        return;
    }
    
    @Override
    protected Void doInBackground(String... ImagePath) {
        GuruguruDialog.this.listener.DoInBackgroundListener();
        return null;
    }
    /**
     * リスナーを設定
     *
     * @param listener 選択イベントリスナー
     */
    public void setDoInBackgroundListener(DoInBackgroundListener listener) {
        this.listener = listener;
    }
    /**
     * ボタン押下インターフェース
     */
    public interface DoInBackgroundListener {
        /**
         * 選択イベント
         */
        public void DoInBackgroundListener();
    }
    
    
    
    @Override
    protected void onPostExecute(Void vd) {
        
        //ここにdoInBackground終了後に行う処理を書く
    
        // プログレスダイアログを閉じる
        if (this.m_ProgressDialog != null && this.m_ProgressDialog.isShowing()) {
            this.m_ProgressDialog.dismiss();
        }
        
    }
    /*
    * キャンセル時の処理
    */
    @SuppressLint("LongLogTag")
    @Override
    protected void onCancelled() {
        super.onCancelled();
        
        Log.v("AsyncTaskProgressDialogSimpleThread", "onCancelled()");
        
        if (this.m_ProgressDialog != null) {
            
            Log.v("this.m_ProgressDialog.isShowing()", String.valueOf(this.m_ProgressDialog.isShowing()));
            
            // プログレスダイアログ表示中の場合
            if (this.m_ProgressDialog.isShowing()) {
                
                // プログレスダイアログを閉じる
                this.m_ProgressDialog.dismiss();
            }
        }
        
        return;
    }
}