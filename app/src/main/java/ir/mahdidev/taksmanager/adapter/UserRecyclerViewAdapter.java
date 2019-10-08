package ir.mahdidev.taksmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.model.TaskRepository;


public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    ArrayList<UserModel> userList ;
    Context context ;
    public UserRecyclerViewAdapter(ArrayList<UserModel> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }
    private TaskRepository repository = TaskRepository.getInstance();
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
        public MaterialCardView viewBackground ;
        public MaterialCardView viewForeground ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userUsername = itemView.findViewById(R.id.user_username);
            userDate     = itemView.findViewById(R.id.date_user);
            countUser    = itemView.findViewById(R.id.task_count);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }

        public void onBind(int position) {
            userUsername.setText(userList.get(position).getUserName());
            userDate.setText(userList.get(position).getRegisterDate());
            countUser.setText(String.valueOf(repository.getProfilesCount(userList.get(position).getId())));
        }
    }
    public void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(UserModel userModel, int position) {
        userList.add(position, userModel);
        notifyItemInserted(position);
    }
}
