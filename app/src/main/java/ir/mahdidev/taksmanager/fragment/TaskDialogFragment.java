package ir.mahdidev.taksmanager.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.util.Const;

public class TaskDialogFragment extends DialogFragment implements AddTaskFragment.AddFragmentInterface
, EditTaskFragment.EditFragmentInterface , DeleteAllTasksFragment.DeleteAllTaskFragmentInterface {

    private int modeTask ;
    private long taskId   ;
    private long userId   ;
    private boolean isAdmin ;

    public static TaskDialogFragment newInstance(int mode , long taskId , long userId , boolean isAdmin) {

        Bundle args = new Bundle();
        args.putInt(Const.MODE_TASK_BUNDLE_KEY, mode);
        args.putLong(Const.TASK_DIALOG_FRAGMENT_TASK_ID_BUNDLE_KEY , taskId);
        args.putLong(Const.TASK_DIALOG_FRAGMENT_USER_ID_BUNDLE_KEY , userId);
        args.putBoolean(Const.IS_ADMIN_BUNDLE_KEY , isAdmin);
        TaskDialogFragment fragment = new TaskDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            modeTask = bundle.getInt(Const.MODE_TASK_BUNDLE_KEY);
            taskId   = bundle.getLong(Const.TASK_DIALOG_FRAGMENT_TASK_ID_BUNDLE_KEY, Const.TASK_DIALOG_DEFAULT_TASK_ID);
            userId   = bundle.getLong(Const.TASK_DIALOG_FRAGMENT_USER_ID_BUNDLE_KEY , Const.TASK_DIALOG_DEFAULT_USER_ID);
            isAdmin  = bundle.getBoolean(Const.IS_ADMIN_BUNDLE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_container , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getContext().getTheme().applyStyle(R.style.MyAlertDialog , true);
        if (modeTask == Const.Add_TASK_MODE){
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            AddTaskFragment addTaskFragment = AddTaskFragment.newInstance(userId);
            transaction.replace(R.id.frame_layout, addTaskFragment , Const.ADD_FRAGMENT_TAG);
            transaction.commit();

        }else if (modeTask == Const.EDIT_TASK_MODE){
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            EditTaskFragment editTaskFragment = EditTaskFragment.newInstance(taskId , userId , isAdmin);
            transaction.replace(R.id.frame_layout, editTaskFragment);
            transaction.commit();
        } else if (modeTask == Const.DELETE_TASK_MODE){
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            DeleteAllTasksFragment deleteAllTasksFragment = DeleteAllTasksFragment.newInstance(userId);
            transaction.replace(R.id.frame_layout, deleteAllTasksFragment);
            transaction.commit();
        }
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.80), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    @Override
    public void onAddTaskClicked() {
        getDialog().cancel();
        Fragment fragment= getTargetFragment();
        Intent intent = new Intent();
        fragment.onActivityResult(getTargetRequestCode() , Activity.RESULT_OK , intent);
    }

    @Override
    public void onCancelAddTaskClicked() {
        getDialog().cancel();
    }

    @Override
    public void onEditTaskClicked() {
        getDialog().cancel();
        Fragment fragment= getTargetFragment();
        Intent intent = new Intent();
        fragment.onActivityResult(getTargetRequestCode() , Activity.RESULT_OK , intent);
    }

    @Override
    public void onDeleteAllTaskClicked() {
        getDialog().cancel();
        Fragment fragment= getTargetFragment();
        Intent intent = new Intent();
        fragment.onActivityResult(getTargetRequestCode() , Activity.RESULT_OK , intent);
    }

    @Override
    public void onCancelDeleteAllTask() {
        getDialog().cancel();
    }

}
