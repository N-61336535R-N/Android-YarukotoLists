package ngrnm.syokuninn_sibou.yarukotolists;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import ngrnm.syokuninn_sibou.yarukotolists.Schedule.YScheduleFragment;

/**
 * Created by ryo on 2018/02/19.
 */

public class YarukotoFragmentPagerAdapter extends FragmentPagerAdapter {
    public YarukotoFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
    private Fragment mCurrentFragment;
    private Fragment homeFragment = null;
    
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (mCurrentFragment != object) {
            mCurrentFragment = (Fragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }
    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
    
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (homeFragment == null || homeFragment.getChildFragmentManager().getBackStackEntryCount() == 0) {
                    if (homeFragment != null) System.out.println("getBackStackEntryCount  :  "+homeFragment.getChildFragmentManager().getBackStackEntryCount());
                    homeFragment = YFragment.newInstance();
                    return homeFragment;
                } else return homeFragment;
            case 1:
                return YScheduleFragment.newInstance(android.R.color.holo_green_light);
            case 2:
                return YTimerFragment.newInstance(android.R.color.holo_red_dark);
        }
        return null;
    }
    
    @Override
    public int getCount() {
        return 3;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return "ページ" + (position + 1);
    }
}
