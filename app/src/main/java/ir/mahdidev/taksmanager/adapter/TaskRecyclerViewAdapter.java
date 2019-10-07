package ir.mahdidev.taksmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.model.TaskModel;
import ir.mahdidev.taksmanager.model.TaskRepository;
import ir.mahdidev.taksmanager.model.UserModel;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

   private ArrayList<TaskModel> taskList ;
   private Context context;

    public TaskRecyclerViewAdapter(ArrayList<TaskModel> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.task_item , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTask;
        private TextView dateTask ;
        private TextView firstCharacterTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTask = itemView.findViewById(R.id.title_task);
            dateTask = itemView.findViewById(R.id.date_task);
            firstCharacterTitle = itemView.findViewById(R.id.first_character_title);
        }
        public void onBind(final int position){
            String time = taskList.get(position).getDate() + " " + taskList.get(position).getTime();
            titleTask.setText(taskList.get(position).getTitle());
            dateTask.setText(time);
            firstCharacterTitle.setText(taskList.get(position).getTitle().substring(0 , 1).toUpperCase());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskRecyclerViewInterface.onReceive(taskList.get(position).getId() , taskList.get(position).getUserId());
                }
            });

        }
    }
    public TaskRecyclerViewInterface taskRecyclerViewInterface;
    public interface TaskRecyclerViewInterface{
        void onReceive(long taskId , long userId);
    }
    public void setTaskRecyclerViewInterface (TaskRecyclerViewInterface taskRecyclerViewInterface){
        this.taskRecyclerViewInterface = taskRecyclerViewInterface;
    }

    public void updateList(ArrayList<TaskModel> taskList){
        this.taskList = new ArrayList<>();
        this.taskList.addAll(taskList);
    }

}
