package ir.mahdidev.taksmanager.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.activity.MainActivity;
import ir.mahdidev.taksmanager.adapter.UserRecyclerViewAdapter;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.RecyclerItemTouchHelper;
import ir.mahdidev.taksmanager.model.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileListFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private ArrayList<UserModel> userList ;
    private UserRecyclerViewAdapter userRecyclerViewAdapter;
    private TaskRepository repository = TaskRepository.getInstance();
    private long userId ;
    public UserProfileListFragment() {
    }
    public static UserProfileListFragment newInstance(long userId) {
        Bundle args = new Bundle();
        args.putLong(Const.USER_PROFILE_LIST_BUNDLE_KEY , userId);
        UserProfileListFragment fragment = new UserProfileListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            userId = bundle.getLong(Const.USER_PROFILE_LIST_BUNDLE_KEY);
        }
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
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(userRecyclerViewAdapter);

    }

    private void readDataFromDb() {
        userList = new ArrayList<>();
        userList = repository.readListUserProfile();
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.users_recyclerView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof UserRecyclerViewAdapter.ViewHolder) {
            String name = userList.get(viewHolder.getAdapterPosition()).getUserName();

            final UserModel deletedItem = userList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            userRecyclerViewAdapter.removeItem(viewHolder.getAdapterPosition());
            repository.deleteUser(deletedItem.getId()) ;
            if (deletedItem.getId() == userId){
                logoutFunction(deletedItem);
            }
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content), name + " removed from user!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    userRecyclerViewAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.BLUE);
            snackbar.show();
        }
    }


    private void logoutFunction(UserModel userModel) {
        repository.updateLoggedIn(userModel.getUserName() , 0);
        Intent intent = MainActivity.newIntent(getActivity());
        startActivity(intent);
        getActivity().finish();
    }

}
