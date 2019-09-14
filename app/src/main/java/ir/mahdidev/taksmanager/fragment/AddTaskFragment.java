package ir.mahdidev.taksmanager.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment {

    private TextInputEditText titleEdt;
    private TextInputEditText descriptionEdt;
    private MaterialButton dateBtn ;
    private MaterialButton timeBtn ;
    private MaterialButton saveBtn ;
    private MaterialButton cancelBtn ;
    private ChipGroup chipGroup;
    private String status = "";
    private int userId ;
    private AddFragmentInterface addFragmentInterface;

    private TaskRepository repository = TaskRepository.getInstance();
    public AddTaskFragment() {

    }

    public static AddTaskFragment newInstance(int userId) {
        Bundle args = new Bundle();
        args.putInt(Const.ADD_FRAGMENT_USER_ID_BUNDLE_KEY , userId);
        AddTaskFragment fragment = new AddTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
         addFragmentInterface = (AddFragmentInterface) getParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null ){
            userId = bundle.getInt(Const.ADD_FRAGMENT_USER_ID_BUNDLE_KEY , 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setDateAndTime();
        insertDataToDb();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragmentInterface.onCancelClicked();
            }
        });
    }

    private void setDateAndTime() {
        DateFormat date = new SimpleDateFormat("MMM dd yyyy" , Locale.US);
        String dateFormatted = date.format(Calendar.getInstance().getTime());
        DateFormat time = new SimpleDateFormat("hh:mm a" , Locale.US);
        String timeFormatted = time.format(Calendar.getInstance().getTime());
        dateBtn.setText(dateFormatted);
        timeBtn.setText(timeFormatted);
    }

    private void insertDataToDb() {
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                switch (chipGroup.getCheckedChipId()){
                    case R.id.todo_chips :{
                        status = getResources().getString(R.string.todo_chips);
                        break;
                    }
                    case R.id.doing_chips : {
                        status = getResources().getString(R.string.doing_chips);
                        break;
                    }
                    case R.id.done_chips :{
                        status = getResources().getString(R.string.done_chips);
                        break;
                    }
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEdt.getText().toString().trim();
                String description = descriptionEdt.getText().toString().trim();
                String time = timeBtn.getText().toString();
                String date = dateBtn.getText().toString();
                boolean isInsert = false ;

                if (!title.isEmpty() && !description.isEmpty() && !time.isEmpty() && !date.isEmpty() &&
                        !status.isEmpty()){
                    isInsert = repository.insertTask(userId , title , description , status , date , time);
                }else {
                    Toast.makeText(getActivity() , "Please Fill the fields" , Toast.LENGTH_SHORT).show();
                }

                if (isInsert){
                    Toast.makeText(getActivity() , "Task Insert Successfully" , Toast.LENGTH_SHORT).show();
                    addFragmentInterface.onSaveClicked(true);
                }

            }
        });


    }

    private void initViews(View v) {
        titleEdt = v.findViewById(R.id.title_edt);
        descriptionEdt = v.findViewById(R.id.description_edt);
        dateBtn = v.findViewById(R.id.date_btn);
        timeBtn = v.findViewById(R.id.time_btn);
        saveBtn = v.findViewById(R.id.save_btn);
        cancelBtn = v.findViewById(R.id.cancel_btn);
        chipGroup = v.findViewById(R.id.chip_group);
    }

    public interface AddFragmentInterface{
        void onSaveClicked(boolean issaved);
        void onCancelClicked () ;
    }

}
