package ir.mahdidev.taksmanager.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import ir.mahdidev.taksmanager.fragment.TaskFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String[] status = {"TODO" , "DOING" , "DONE"};
    public ViewPagerAdapter(FragmentManager fm ) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TaskFragment.newInstance(status[position]);
    }

    @Override
    public int getCount() {
        return status.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return status[position];
    }

}
