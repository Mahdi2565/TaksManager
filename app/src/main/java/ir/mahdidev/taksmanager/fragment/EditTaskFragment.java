package ir.mahdidev.taksmanager.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.util.Const;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTaskFragment extends Fragment {


    public EditTaskFragment() {
    }

    public static EditTaskFragment newInstance(int taskId , int userId) {

        Bundle args = new Bundle();
        args.putInt(Const.EDIT_FRAGMENT_TASK_ID_BUNDLE_KEY , taskId);
        args.putInt(Const.EDIT_FRAGMENT_USER_ID_BUNDLE_KEY , userId);
        EditTaskFragment fragment = new EditTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_task, container, false);
    }

}
