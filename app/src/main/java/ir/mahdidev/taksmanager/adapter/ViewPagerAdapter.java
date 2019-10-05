package ir.mahdidev.taksmanager.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;


import ir.mahdidev.taksmanager.fragment.TaskFragment;
import ir.mahdidev.taksmanager.model.UserModel;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private String[] status = {"TODO" , "DOING" , "DONE"};
    private long userId;
    private boolean isAdmin ;
    public ViewPagerAdapter(FragmentManager fm , long userId , boolean isAdmin) {
        super(fm);
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    @Override
    public Fragment getItem(int position) {
        return TaskFragment.newInstance(status[position] , userId , isAdmin);
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

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE ;
    }
}
