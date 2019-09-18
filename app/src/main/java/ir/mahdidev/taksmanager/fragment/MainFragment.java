package ir.mahdidev.taksmanager.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.adapter.ViewPagerAdapter;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.EventBusMessage;
import ir.mahdidev.taksmanager.util.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private UserModel userModel;
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private com.github.clans.fab.FloatingActionButton addTask ;
    private com.github.clans.fab.FloatingActionButton removeTasks ;
    private com.github.clans.fab.FloatingActionButton editProfile ;
    private FloatingActionMenu fab_menu;


    public MainFragment() {
    }
    public static MainFragment newInstance(UserModel userModel) {
        Bundle args = new Bundle();
        args.putSerializable(Const.USER_MODEL_LOGGED_IN_MAIN_FRAGMENT_BUNDLE_KEY , userModel);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        EventBus.getDefault().register(this);
        super.onAttach(context);
        mainFragmentInterface = (MainFragmentInterface) context;
        TaskDialogFragment taskDialogFragment = (TaskDialogFragment) getFragmentManager()
                .findFragmentByTag(Const.ADD_DIALOG_FRAGMENT_TAG);
        DeleteAllTasksFragment deleteAllTasksFragment = (DeleteAllTasksFragment) getFragmentManager()
                .findFragmentByTag(Const.DELETE_ALL_TASK_DIALOG_FRAGMENT_TAG);
        if (taskDialogFragment != null ) {
            taskDialogFragment.setTargetFragment(this, Const.TARGET_REQUSET_CODE_MAIN_FRAGMENT);
        }
        if (deleteAllTasksFragment != null) {
            deleteAllTasksFragment.setTargetFragment(this , Const.TARGET_REQUSET_CODE_MAIN_FRAGMENT);
        }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            userModel = (UserModel) bundle.getSerializable(Const.USER_MODEL_LOGGED_IN_MAIN_FRAGMENT_BUNDLE_KEY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initViewPagerAndTablayout();
        initFab();
    }
    private void initViews(View v) {
        tableLayout  = v.findViewById(R.id.tablayout);
        viewPager    = v.findViewById(R.id.view_pager);
        addTask      =  v.findViewById(R.id.add_task);
        removeTasks  =  v.findViewById(R.id.remove_tasks);
        editProfile  =  v.findViewById(R.id.edit_profile);
        fab_menu     =  v.findViewById(R.id.fab_menu);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_task :{
                TaskDialogFragment dialogFragment = TaskDialogFragment.newInstance(Const.Add_TASK_MODE , Const.TASK_DIALOG_DEFAULT_TASK_ID
                        , userModel.getId() , userModel.getIsAdmin()==1);
                dialogFragment.setTargetFragment(MainFragment.this , Const.TARGET_REQUSET_CODE_MAIN_FRAGMENT);
                dialogFragment.show(getFragmentManager() , Const.ADD_DIALOG_FRAGMENT_TAG);
                fab_menu.close(true);
                break;
            }
            case R.id.remove_tasks : {
                TaskDialogFragment dialogFragment = TaskDialogFragment.newInstance(Const.DELETE_TASK_MODE , Const.TASK_DIALOG_DEFAULT_TASK_ID
                        , userModel.getId(), userModel.getIsAdmin()==1);
                dialogFragment.setTargetFragment(MainFragment.this , Const.TARGET_REQUSET_CODE_MAIN_FRAGMENT);
                dialogFragment.show(getFragmentManager() , Const.DELETE_ALL_TASK_DIALOG_FRAGMENT_TAG);
                fab_menu.close(true);
                break;
            }
            case R.id.edit_profile : {
                mainFragmentInterface.onReceive();
                fab_menu.close(true);
                break;
            }
        }
    }
    private void initViewPagerAndTablayout() {

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager() , userModel.getId()
                , userModel.getIsAdmin() == 1);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tableLayout.setupWithViewPager(viewPager);
    }
    private void initFab(){
        addTask.setOnClickListener(this);
        removeTasks.setOnClickListener(this);
        editProfile.setOnClickListener(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)return;

        switch (requestCode) {
            case Const.TARGET_REQUSET_CODE_MAIN_FRAGMENT: {
                viewPagerAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
    public MainFragmentInterface mainFragmentInterface ;
    public interface MainFragmentInterface{
        void onReceive();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDataChangeEvent(EventBusMessage eventBusMessage){
        if (eventBusMessage.isEditClicked()){
            viewPagerAdapter.notifyDataSetChanged();
            EventBus.getDefault().removeStickyEvent(EventBusMessage.class);
        }
    }

}
