package ngrnm.syokuninn_sibou.yarukotolists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import io.realm.Realm;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YCategory;
import ngrnm.syokuninn_sibou.yarukotolists.Database.RealmYs.YLI;
import ngrnm.syokuninn_sibou.yarukotolists.Settings.SettingMoldActivity;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils.DeleteDialog;
import ngrnm.syokuninn_sibou.yarukotolists.YarukotoList.Utils.EditDialog;

/**
 * Created by ryo on 2018/02/26.
 */

public abstract class YCLIsBasePageFragment extends Fragment {
    // 今のところ、これで問題なさそう。
    protected Realm realm = null;
    
    private static final int FLAGCODE_EDIT = 68314;
    private static final int FLAGCODE_DELETE = 392581;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        // このスレッドのためのRealmインスタンスを取得
        realm = Realm.getDefaultInstance();
    }
    
    
    
    
    /* リスト長押しの処理（コンテキストメニュー） */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        
        AdapterView.AdapterContextMenuInfo adapterInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String title = null;
        switch (v.getId()) {
            case R.id.gridview:
                GridView gv = (GridView) v;
                title = ( (YCategory)gv.getItemAtPosition(adapterInfo.position) ).getTitle();
                break;
            case R.id.listview:
                ListView lv = (ListView) v;
                title = ( (YLI)lv.getItemAtPosition(adapterInfo.position) ).getLI_title(realm);
                break;
        }
        menu.setHeaderTitle(title);
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int posi = info.position;
        switch (item.getItemId()) {
            case R.id.listview_edit:
                EditDialog E_dialog = new EditDialog();
                E_dialog.setSelectedItemName(getSelectedItemTitle(posi), posi);
                E_dialog.setTargetFragment(this, FLAGCODE_EDIT);
                E_dialog.show(getFragmentManager(), "e_dialog");
                return true;
            case R.id.listview_delete:
                DeleteDialog D_dialog = new DeleteDialog();
                D_dialog.setSelectedItemName(getSelectedItemTitle(posi), posi);
                D_dialog.setTargetFragment(this, FLAGCODE_DELETE);
                D_dialog.show(getFragmentManager(), "d_dialog");
                return true;
        }
        return false;
    }
    protected abstract String getSelectedItemTitle(int posi);
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 指定したrequestCodeで戻ってくる
        switch (requestCode) {
            case FLAGCODE_EDIT:
                if (resultCode == EditDialog.getRCode_OK()) {
                    int posi = data.getIntExtra("posi", -1);
                    String newTitle = data.getStringExtra("newTitle");
                    editListItem(posi, newTitle);
                    updateList();
                }
                break;
            /* DeleteDialog の実行後 */
            case FLAGCODE_DELETE:
                if (resultCode == DeleteDialog.getRCode_OK()) {
                    int posi = data.getIntExtra("posi", -1);
                    removeListItem(posi);
                    updateList();
                }
                break;
        }
    }
    
    protected abstract void editListItem(int posi, String newTitle);
    protected abstract void removeListItem(int posi);
    
    protected abstract void updateList();
    
    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }
    
    
    
    /**  ↗︎ オプションメニューの中身設定  */
    private MenuInflater menuInflater;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.y_menu, menu);
        this.menuInflater = menuInflater;
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menuInflater.inflate(R.menu.y_menu, menu);
        super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_add_list:
                add_Dialog();
                return true;
            case R.id.edit_mode:
                Toast.makeText(getContext(), "(未)選択", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getContext(), "設定", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SettingMoldActivity.class);
                startActivity(intent);
                return true;
        }
        
        Toast.makeText(getContext(), "未実装です...", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
    
    
    
    
    protected abstract void add_Dialog();
    
    
    @Override
    public void onDetach() {
        super.onDetach();
        realm.close(); // Remember to close Realm when done.
    }
    
}
