package ir.mahdidev.taksmanager.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.model.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteAllTasksFragment extends Fragment {

    private MaterialButton deleteBtn;
    private MaterialButton cancelBtn;
    private long userId ;
    private TaskRepository repository = TaskRepository.getInstance();
    public DeleteAllTasksFragment() {
        // Required empty public constructor
    }

    public static DeleteAllTasksFragment newInstance(long userId) {
        Bundle args = new Bundle();
        args.putLong(Const.DELETE_ALL_TASK_FRAGMENT_USER_ID_BUNDLE_KEY , userId);
        DeleteAllTasksFragment fragment = new DeleteAllTasksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        deleteAllTaskFragmentInterface = (DeleteAllTaskFragmentInterface) getParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getLong(Const.DELETE_ALL_TASK_FRAGMENT_USER_ID_BUNDLE_KEY);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_all_tasks, container, false);
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
                    deleteAllTaskFragmentInterface.onCancelDeleteAllTask();
            }
        });
    }

    private void deleteBtnFunction() {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isDeleted = repository.deleteTask(userId);
                if (isDeleted){
                    deleteAllTaskFragmentInterface.onDeleteAllTaskClicked();
                }else {
                    Toast.makeText(getActivity() , "There is no task for delete !" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews(View v) {
        deleteBtn = v.findViewById(R.id.delete_btn);
        cancelBtn = v.findViewById(R.id.cancel_btn);
    }
    public DeleteAllTaskFragmentInterface deleteAllTaskFragmentInterface;
    public interface DeleteAllTaskFragmentInterface{
        void onDeleteAllTaskClicked();
        void onCancelDeleteAllTask();
    }
}
