package ir.mahdidev.taksmanager.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.model.TaskModel;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.model.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment  {

    private TextInputEditText titleEdt;
    private TextInputEditText descriptionEdt;
    private MaterialButton dateBtn ;
    private MaterialButton timeBtn ;
    private MaterialButton saveBtn ;
    private MaterialButton cancelBtn ;
    private MaterialButton chooseImage;
    private ChipGroup chipGroup;
    private String status = "";
    private Long userId ;
    private UUID uuid;
    private String imagePath;
    private AddFragmentInterface addFragmentInterface;
    private Date dateReceive = null;
    private Date timeReceive = null;

    private TaskRepository repository = TaskRepository.getInstance();
    public AddTaskFragment() {

    }

    public static AddTaskFragment newInstance(long userId) {
        Bundle args = new Bundle();
        args.putLong(Const.ADD_FRAGMENT_USER_ID_BUNDLE_KEY , userId);
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
        uuid = UUID.randomUUID();
        Bundle bundle = getArguments();
        if (bundle != null ){
            userId = bundle.getLong(Const.ADD_FRAGMENT_USER_ID_BUNDLE_KEY , 0);
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
        setDate();
        setTime();
        dateBtnFunction();
        timeBtnFunction();
        insertDataToDb();
        cancelBtnFunction();
        chooseImageFunction();
    }

    private void cancelBtnFunction() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragmentInterface.onCancelAddTaskClicked();
            }
        });
    }

    private void chooseImageFunction() {
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseTaskImageFragment chooseTaskImageFragment = ChooseTaskImageFragment.newInstance(uuid);
                chooseTaskImageFragment.setTargetFragment(AddTaskFragment.this ,
                        Const.TARGET_REQUSET_CODE_CCHOOSE_IMAGE_FRAGMENT);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout
                        , chooseTaskImageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setTime() {
        if (timeReceive == null){
            DateFormat timeFormat = new SimpleDateFormat("hh:mm a" , Locale.US);
            String timeFormatted = timeFormat.format(Calendar.getInstance().getTime());
            timeBtn.setText(timeFormatted);
        }else {
            DateFormat timeFormat = new SimpleDateFormat("hh:mm a" , Locale.US);
            String timeFormatted = timeFormat.format(timeReceive);
            timeBtn.setText(timeFormatted);
        }
    }

    private void timeBtnFunction() {

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(timeBtn.getText().toString());
                timePickerFragment.setTargetFragment(AddTaskFragment.this , Const.TARGET_REQUSET_CODE_TIME_PICKER_FRAGMENT);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout , timePickerFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    private void dateBtnFunction() {
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(dateBtn.getText().toString());
                datePickerFragment.setTargetFragment(AddTaskFragment.this , Const.TARGET_REQUSET_CODE_DATE_PICKER_FRAGMENT);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout , datePickerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setDate() {
        if (dateReceive == null){
            DateFormat date = new SimpleDateFormat("MMM dd yyyy" , Locale.US);
            String dateFormatted = date.format(Calendar.getInstance().getTime());
            dateBtn.setText(dateFormatted);
        }else {
            DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy" , Locale.US);
            String dateFormatted = dateFormat.format(dateReceive);
            dateBtn.setText(dateFormatted);

        }
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
                if (chipGroup.getCheckedChipId() == -1){
                    status = "";
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
                    isInsert = repository.insertTask(setTaskModel(userId , title , description , status , date , time));
                }else {
                    Toast.makeText(getActivity() , "Please Fill the fields" , Toast.LENGTH_SHORT).show();
                }

                if (isInsert){
                    Toast.makeText(getActivity() , "Task Insert Successfully" , Toast.LENGTH_SHORT).show();
                    addFragmentInterface.onAddTaskClicked();
                }

            }
        });


    }

    private TaskModel setTaskModel(long userId, String title, String description, String status, String date, String time) {
        TaskModel taskModel = new TaskModel();
        taskModel.setUserId(userId);
        taskModel.setTitle(title);
        taskModel.setDescription(description);
        taskModel.setStatus(status);
        taskModel.setDate(date);
        taskModel.setTime(time);
        taskModel.setUuid(uuid);
        if (imagePath !=null) taskModel.setImagePath(imagePath);
        return taskModel;
    }

    private void initViews(View v) {
        titleEdt = v.findViewById(R.id.title_edt);
        descriptionEdt = v.findViewById(R.id.description_edt);
        dateBtn = v.findViewById(R.id.date_btn);
        timeBtn = v.findViewById(R.id.time_btn);
        saveBtn = v.findViewById(R.id.save_btn);
        cancelBtn = v.findViewById(R.id.cancel_btn);
        chipGroup = v.findViewById(R.id.chip_group);
        chooseImage = v.findViewById(R.id.choose_image_btn);
    }

    public interface AddFragmentInterface{
        void onAddTaskClicked();
        void onCancelAddTaskClicked() ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!(resultCode == Activity.RESULT_OK)){
            return;
        }
        if (requestCode == Const.TARGET_REQUSET_CODE_DATE_PICKER_FRAGMENT){
            dateReceive = (Date) data.getSerializableExtra(Const.DATE_PICKER_FRAGMENT_BUNDLE_KEY);
        }else if (requestCode == Const.TARGET_REQUSET_CODE_TIME_PICKER_FRAGMENT){
            timeReceive = (Date) data.getSerializableExtra(Const.TIME_PICKER_FRAGMENT_BUNDLE_KEY) ;
        }else if (requestCode == Const.TARGET_REQUSET_CODE_CCHOOSE_IMAGE_FRAGMENT){
            imagePath = data.getStringExtra(Const.CHOOSE_IMAGE_FRAGMENT_IMAGE_PATH_INTENT_KEY);
        }
    }
}
