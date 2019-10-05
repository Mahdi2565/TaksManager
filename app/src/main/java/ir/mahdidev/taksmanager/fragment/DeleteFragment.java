package ir.mahdidev.taksmanager.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.model.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteFragment extends Fragment {

    private MaterialButton deleteBtn;
    private MaterialButton cancelBtn;
    private long taskId ;
    private long userId ;
    private TaskRepository repository = TaskRepository.getInstance();

    public DeleteFragment() {
    }

    public static DeleteFragment newInstance(long taskId, long userId) {

        Bundle args = new Bundle();
        args.putLong(Const.DELETE_FRAGMENT_TASK_ID_BUNDLE_KEY , taskId);
        args.putLong(Const.DELETE_FRAGMENT_USER_ID_BUNDLE_KEY , userId);
        DeleteFragment fragment = new DeleteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            taskId = bundle.getLong(Const.DELETE_FRAGMENT_TASK_ID_BUNDLE_KEY);
            userId = bundle.getLong(Const.DELETE_FRAGMENT_USER_ID_BUNDLE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        deleteBtnFunction();
        cancelBtnFunction();
    }

    private void cancelBtnFunction() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager() != null)
            getFragmentManager().popBackStack();
            }
        });
    }

    private void deleteBtnFunction() {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isDeleted = repository.deleteTask(taskId , userId);
                if (isDeleted){
                    Fragment fragment= getTargetFragment();
                    Intent intent = new Intent();
                    fragment.onActivityResult(getTargetRequestCode() , Activity.RESULT_OK , intent);
                }
            }
        });
    }

    private void initViews(View v) {
        deleteBtn = v.findViewById(R.id.delete_btn);
        cancelBtn = v.findViewById(R.id.cancel_btn);
    }
}
