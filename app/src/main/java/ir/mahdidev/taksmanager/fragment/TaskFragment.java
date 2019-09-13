package ir.mahdidev.taksmanager.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.model.TaskModel;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    private String status;
    private int userId;
    private ArrayList<TaskModel> taskList ;
    private TaskRepository repository;
    public TaskFragment() {
    }

    public static TaskFragment newInstance(String status , int userId) {
        Bundle args = new Bundle();
        args.putString(Const.STATUS_BUNDLE_KEY , status);
        args.putInt(Const.USER_ID_BUNDLE_KEY , userId);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getString(Const.STATUS_BUNDLE_KEY);
            userId = bundle.getInt(Const.USER_ID_BUNDLE_KEY);
        }
        readDatabase();
    }

    private void readDatabase() {
        taskList = new ArrayList<>();
        repository = TaskRepository.getInstance();
        taskList = repository.readTask(status , userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
