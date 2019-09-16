package ir.mahdidev.taksmanager.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.activity.TaskActivity;
import ir.mahdidev.taksmanager.adapter.TaskRecyclerViewAdapter;
import ir.mahdidev.taksmanager.model.TaskModel;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {

    //Change task fragment !!!!!!!
    private String status;
    private int userId;
    private ArrayList<TaskModel> taskList ;
    private TaskRepository repository = TaskRepository.getInstance();
    private RecyclerView taskFragmentRecyclerView;
    private TaskRecyclerViewAdapter recyclerViewAdapter;
    private LinearLayout noTaskImage;
    private TextView noTaskText;
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
        getDataFromBundle();
        readDatabase();

    }

    private void checkExistTask(){
        if (taskList.isEmpty()){
            noTaskText.setVisibility(View.VISIBLE);
            noTaskImage.setVisibility(View.VISIBLE);
        }else {
            noTaskText.setVisibility(View.GONE);
            noTaskImage.setVisibility(View.GONE);
        }
    }
    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getString(Const.STATUS_BUNDLE_KEY);
            userId = bundle.getInt(Const.USER_ID_BUNDLE_KEY);
        }
    }

    private void readDatabase() {
        taskList = new ArrayList<>();
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
        initViews(view);
        initRecyclerView();
        checkExistTask();
    }

    private void initRecyclerView() {
        recyclerViewAdapter = new TaskRecyclerViewAdapter(taskList , getActivity());
        taskFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskFragmentRecyclerView.setAdapter(recyclerViewAdapter);
        taskFragmentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                //TODO: pass arrived bottom to activity for hide fab
                }else {
                }
            }
        });
        recyclerViewAdapter.setTaskRecyclerViewInterface(new TaskRecyclerViewAdapter.TaskRecyclerViewInterface() {
            @Override
            public void onReceive(int taskId , int UserId) {
                TaskDialogFragment dialogFragment = TaskDialogFragment.newInstance(Const.EDIT_TASK_MODE , taskId , userId);
                dialogFragment.setTargetFragment(TaskFragment.this , Const.TARGET_REQUSET_CODE_EDIT_FRAGMENT_FRAGMENT);
                dialogFragment.show(getFragmentManager() , Const.EDIT_DIALOG_FRAGMENT_TAG);
            }
        });
    }


    private void initViews(View view) {
        taskFragmentRecyclerView = view.findViewById(R.id.task_recyclerView);
        noTaskImage = view.findViewById(R.id.linear_no_task_image);
        noTaskText  = view.findViewById(R.id.no_task_txt);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!(resultCode == Activity.RESULT_OK)){
            return;
        }
        if (requestCode == Const.TARGET_REQUSET_CODE_EDIT_FRAGMENT_FRAGMENT){
            readDatabase();
            recyclerViewAdapter.updateList(taskList);
            recyclerViewAdapter.notifyDataSetChanged();
            checkExistTask();
        }
    }
}
