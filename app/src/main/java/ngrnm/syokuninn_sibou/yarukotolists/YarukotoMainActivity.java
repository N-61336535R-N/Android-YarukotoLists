package ngrnm.syokuninn_sibou.yarukotolists;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ngrnm.syokuninn_sibou.yarukotolists.Database.BackupUtils.Restorer;

/**
 * Created by ryo on 2018/02/19.
 */

public class YarukotoMainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FragmentManager manager;
    private ViewPager viewPager;
    private YarukotoFragmentPagerAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ymain);
        setViews();
    }
    
    private void setViews() {
        // Fragment でツールバーを扱えるようにする
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        
        manager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new YarukotoFragmentPagerAdapter(manager);
        viewPager.setAdapter(adapter);
        // ページ移動した時に初期化されると、リストの開いてたページが戻ってしまう...。
        viewPager.setOffscreenPageLimit(3);
        
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
    
    
    /**
     * Back ボタンの挙動はここですべて管理する。
     */
    @Override
    public void onBackPressed() {
        //現在のタブ(Fragment)の バックスタックの登録数をチェックして0であればPopUpは存在しない
        if (0 < adapter.getCurrentFragment().getChildFragmentManager().getBackStackEntryCount()) {
            /* その画面内で戻る処理 */
            adapter.getCurrentFragment().getChildFragmentManager().popBackStack();
            return;
        } else if (viewPager.getCurrentItem() != 0) {
            //１番のタブに戻すような処理をここで実装するここではchangeTabというメソッドがあるという想定
            viewPager.setCurrentItem(0, true);
            return;
        }
        super.onBackPressed();
    }
    
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onRestart() {
        boolean rollbackFlag = Restorer.getRollbackFlag();
        if (rollbackFlag)
            finishAndRemoveTask();
        super.onRestart();
    }
    
}
