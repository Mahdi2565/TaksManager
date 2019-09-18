package ir.mahdidev.taksmanager.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.adapter.UserRecyclerViewAdapter;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<UserModel> userList ;
    private UserRecyclerViewAdapter userRecyclerViewAdapter;
    private TaskRepository repository = TaskRepository.getInstance();
    public UserProfileListFragment() {
    }
    public static UserProfileListFragment newInstance() {
        Bundle args = new Bundle();
        UserProfileListFragment fragment = new UserProfileListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        readDataFromDb();
        initRecyclerView();
    }

    private void initRecyclerView() {
        userRecyclerViewAdapter = new UserRecyclerViewAdapter(userList , getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(userRecyclerViewAdapter);
    }

    private void readDataFromDb() {
        userList = new ArrayList<>();
        userList = repository.readListUserProfile();
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.users_recyclerView);
    }
}
