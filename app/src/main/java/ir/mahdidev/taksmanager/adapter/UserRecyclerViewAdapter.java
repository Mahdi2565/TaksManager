package ir.mahdidev.taksmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.TaskRepository;


public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    ArrayList<UserModel> userList ;
    Context context ;
    private TaskRepository repository = TaskRepository.getInstance();
    public UserRecyclerViewAdapter(ArrayList<UserModel> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_item , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView userUsername;
        private TextView userDate ;
        private TextView countUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userUsername = itemView.findViewById(R.id.user_username);
            userDate     = itemView.findViewById(R.id.date_user);
            countUser    = itemView.findViewById(R.id.task_count);
        }

        public void onBind(int position) {
            userUsername.setText(userList.get(position).getUserName());
            userDate.setText(userList.get(position).getRegisterDate());
            countUser.setText(String.valueOf(repository.getProfilesCount(position+1)));
        }
    }
}
