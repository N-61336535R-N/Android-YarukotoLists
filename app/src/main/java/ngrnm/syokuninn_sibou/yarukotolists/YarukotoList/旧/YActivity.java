package ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.旧;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import ngrnm.syokuninn_sibou.yarukotolists.R;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.SettingMoldActivity;

/**
 * Created by ryo on 2017/10/02.
 */

public abstract class YActivity extends AppCompatActivity {
    protected abstract Checker getLibC();
    
    private YActivity instance = null;
    public YActivity getInstance() {
        return instance;
    }
    public void setInstance(YActivity instance) {
        this.instance = instance;
    }
    protected Context getThisActivity() {
        return getInstance().getApplicationContext();
    }
    
    
    // ↗︎ メニューの中身設定
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.y_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_add_list:
                if (getLibC().getSize() +1 < getLibC().getLIMIT_kind()) {
                    final EditText editView = new EditText(this);
                    editView.setInputType(InputType.TYPE_CLASS_TEXT);
                    AlertDialog.Builder ADBuilder
                            = new AlertDialog.Builder(this)
                            .setTitle("名前を入力")
                            .setView(editView)
                            .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // キャンセルボタンを押した時の処理。何もしないver
                                }
                            });
                    // OKボタンの設定
                    ADBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String newItemTitle = editView.getText().toString();
                            // サムネイル画像は、指定次第、その場で imgDirPath まで移動させる。
                            // 第２引数は、移動先内での、その画像ファイルの名前のみ
                            // 今は、"No_Image"で統一
                            getLibC().addNew(newItemTitle, "No_Image");
                            Toast.makeText(getThisActivity(), "「" + editView.getText().toString() + "」の作成に成功しました!", Toast.LENGTH_SHORT).show();
                            updateList();
                        }
                    });
                    AlertDialog TsuikaDialog = ADBuilder.create();
                    TsuikaDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(editView, 0);
                        }
                    });
                    TsuikaDialog.show();
                } else {
                    AlertDialog.Builder ADBuilder
                            = new AlertDialog.Builder(this)
                            .setTitle("エラー")
                            .setMessage("上限：" + getLibC().getLIMIT_kind() + " 個を超えました。\nこれ以上は追加できません。\n\n有料版をお試しください。")
                            .setPositiveButton("キャンセル", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // Okボタンを押した時の処理。何もしないver
                                }
                            });
                    // OKボタンの設定
                    AlertDialog ErrorDialog = ADBuilder.create();
                    ErrorDialog.show();
                    Toast.makeText(getThisActivity(), "上限：" + getLibC().getLIMIT_kind() + " 個を超えました", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.edit_mode:
                Toast.makeText(this, "(未)選択", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(YActivity.this, SettingMoldActivity.class);
                startActivity(intent);
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    
    public void removeList(int posi) {
        getLibC().remove(posi);
    }
    protected abstract void updateList();
    
    
}