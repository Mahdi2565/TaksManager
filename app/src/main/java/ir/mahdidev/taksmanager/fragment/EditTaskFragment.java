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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.model.TaskModel;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.model.TaskRepository;
import ir.mahdidev.taksmanager.util.G;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTaskFragment extends Fragment {

    private TextInputEditText titleEdt;
    private TextInputEditText descriptionEdt;
    private MaterialButton dateBtn;
    private MaterialButton timeBtn;
    private MaterialButton saveBtn;
    private MaterialButton deleteBtn;
    private MaterialButton editBtn;
    private ChipGroup chipGroup;
    private Chip todoChip;
    private Chip doingChip;
    private Chip doneChip;
    private String status;
    private ImageView shareTask;
    private ImageView chooseImage;
    private TaskRepository repository = TaskRepository.getInstance();
    private TaskModel taskModel;
    private Long userId;
    private Long taskId;
    private boolean isAdmin;
    private Date dateReceive;
    private Date timeReceive;
    private boolean isEditable = false;
    private String imagePath;

    public EditTaskFragment() {
    }

    public static EditTaskFragment newInstance(long taskId, long userId, boolean isAdmin) {

        Bundle args = new Bundle();
        args.putLong(Const.EDIT_FRAGMENT_TASK_ID_BUNDLE_KEY, taskId);
        args.putLong(Const.EDIT_FRAGMENT_USER_ID_BUNDLE_KEY, userId);
        args.putBoolean(Const.IS_ADMIN_BUNDLE_KEY, isAdmin);
        EditTaskFragment fragment = new EditTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        editFragmentInterface = (EditFragmentInterface) getParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getLong(Const.EDIT_FRAGMENT_USER_ID_BUNDLE_KEY);
            taskId = bundle.getLong(Const.EDIT_FRAGMENT_TASK_ID_BUNDLE_KEY);
            isAdmin = bundle.getBoolean(Const.IS_ADMIN_BUNDLE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        readTaskFromDb();
        setDataToViews();
        editBtnFunction();
        timeBtnFunction();
        setReceiveTime();
        dateBtnFunction();
        setReceiveDate();
        saveBtnFunction();
        deleteBtnFunction();
        shareTaskImgFunction();
        chooseImageFunction();
    }

    private void chooseImageFunction() {

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseTaskImageFragment chooseTaskImageFragment =
                        ChooseTaskImageFragment.newInstance(taskModel.getUuid());
                chooseTaskImageFragment.setTargetFragment(EditTaskFragment.this,
                        Const.TARGET_REQUSET_CODE_CCHOOSE_IMAGE_FRAGMENT);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, chooseTaskImageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void shareTaskImgFunction() {
        shareTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "Title: " + taskModel.getTitle() + "\n" +
                        "Description: " + taskModel.getDescription() + "\n" +
                        "Date: " + taskModel.getDate() + "\n" +
                        "Time: " + taskModel.getTime() + "\n" +
                        "Status: " + taskModel.getStatus();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    intent = Intent.createChooser(intent, "Send via");
                    startActivity(intent);
                }
            }
        });
    }

    private void deleteBtnFunction() {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteFragment deleteFragment = DeleteFragment.newInstance(taskId, userId);
                deleteFragment.setTargetFragment(EditTaskFragment.this, Const.TARGET_REQUSET_CODE_DELETE_FRAGMENT_FRAGMENT);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, deleteFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void saveBtnFunction() {
        status = "";

        switch (chipGroup.getCheckedChipId()) {
            case R.id.todo_chips: {
                status = getResources().getString(R.string.todo_chips);
                break;
            }
            case R.id.doing_chips: {
                status = getResources().getString(R.string.doing_chips);
                break;
            }
            case R.id.done_chips: {
                status = getResources().getString(R.string.done_chips);
                break;
            }
        }

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                switch (chipGroup.getCheckedChipId()) {
                    case R.id.todo_chips: {
                        status = getResources().getString(R.string.todo_chips);
                        break;
                    }
                    case R.id.doing_chips: {
                        status = getResources().getString(R.string.doing_chips);
                        break;
                    }
                    case R.id.done_chips: {
                        status = getResources().getString(R.string.done_chips);
                        break;
                    }
                }
                if (chipGroup.getCheckedChipId() == -1) {
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
                boolean isUpdate = false;

                if (!title.isEmpty() && !description.isEmpty() && !time.isEmpty() && !date.isEmpty() &&
                        !status.isEmpty()) {
                    isUpdate = repository.updateTask(setTaskModel(userId, taskId, title, description, status, date, time));
                } else {
                    Toast.makeText(getActivity(), "Please Fill the fields", Toast.LENGTH_SHORT).show();
                }

                if (isUpdate) {
                    Toast.makeText(getActivity(), "Task Update Successfully", Toast.LENGTH_SHORT).show();
                    editFragmentInterface.onEditTaskClicked();
                }

            }
        });


    }

    private void setReceiveDate() {
        if (dateReceive != null) {
            DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.US);
            String dateFormatted = dateFormat.format(dateReceive);
            dateBtn.setText(dateFormatted);
        }
    }

    private void dateBtnFunction() {
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(taskModel.getDate());
                datePickerFragment.setTargetFragment(EditTaskFragment.this, Const.TARGET_REQUSET_CODE_DATE_PICKER_FRAGMENT);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, datePickerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setReceiveTime() {

        if (timeReceive != null) {
            DateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            String timeFormatted = timeFormat.format(timeReceive);
            timeBtn.setText(timeFormatted);
        }

    }

    private void timeBtnFunction() {

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(taskModel.getTime());
                timePickerFragment.setTargetFragment(EditTaskFragment.this, Const.TARGET_REQUSET_CODE_TIME_PICKER_FRAGMENT);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, timePickerFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

    private void editBtnFunction() {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableViews();
            }
        });

    }

    private void enableViews() {
        titleEdt.setEnabled(true);
        descriptionEdt.setEnabled(true);
        dateBtn.setEnabled(true);
        timeBtn.setEnabled(true);
        saveBtn.setEnabled(true);
        saveBtn.setVisibility(View.VISIBLE);
        todoChip.setEnabled(true);
        doingChip.setEnabled(true);
        doneChip.setEnabled(true);
        editBtn.setVisibility(View.GONE);
    }

    private void setDataToViews() {
        titleEdt.setText(taskModel.getTitle());
        descriptionEdt.setText(taskModel.getDescription());
        dateBtn.setText(taskModel.getDate());
        timeBtn.setText(taskModel.getTime());
        if (taskModel.getStatus().equals(getResources().getString(R.string.todo_chips))) {
            todoChip.setChecked(true);
        } else if (taskModel.getStatus().equals(getResources().getString(R.string.doing_chips))) {
            doingChip.setChecked(true);
        } else if (taskModel.getStatus().equals(getResources().getString(R.string.done_chips))) {
            doneChip.setChecked(true);
        }

    }

    private void readTaskFromDb() {
        if (isAdmin) {
            taskModel = repository.readTask(taskId);
        } else {
            taskModel = repository.readTask(userId, taskId);
        }
    }

    private void initViews(View v) {

        titleEdt = v.findViewById(R.id.title_edt);
        descriptionEdt = v.findViewById(R.id.description_edt);
        dateBtn = v.findViewById(R.id.date_btn);
        timeBtn = v.findViewById(R.id.time_btn);
        saveBtn = v.findViewById(R.id.save_btn);
        deleteBtn = v.findViewById(R.id.delete_btn);
        editBtn = v.findViewById(R.id.edit_btn);
        chipGroup = v.findViewById(R.id.chip_group);
        todoChip = v.findViewById(R.id.todo_chips);
        doingChip = v.findViewById(R.id.doing_chips);
        doneChip = v.findViewById(R.id.done_chips);
        shareTask = v.findViewById(R.id.share_task_img);
        chooseImage = v.findViewById(R.id.choose_image_img);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!(resultCode == Activity.RESULT_OK)) {
            return;
        }
        if (requestCode == Const.TARGET_REQUSET_CODE_DATE_PICKER_FRAGMENT) {
            dateReceive = (Date) data.getSerializableExtra(Const.DATE_PICKER_FRAGMENT_BUNDLE_KEY);
            isEditable = true;
        } else if (requestCode == Const.TARGET_REQUSET_CODE_TIME_PICKER_FRAGMENT) {
            timeReceive = (Date) data.getSerializableExtra(Const.TIME_PICKER_FRAGMENT_BUNDLE_KEY);
            isEditable = true;
        } else if (requestCode == Const.TARGET_REQUSET_CODE_DELETE_FRAGMENT_FRAGMENT) {
            editFragmentInterface.onEditTaskClicked();
        } else if (requestCode == Const.TARGET_REQUSET_CODE_CCHOOSE_IMAGE_FRAGMENT) {
            imagePath = data.getStringExtra(Const.CHOOSE_IMAGE_FRAGMENT_IMAGE_PATH_INTENT_KEY);
            repository.updateTask(setTaskModel(userId, taskId, titleEdt.getText().toString()
                    , descriptionEdt.getText().toString(), status, dateBtn.getText().toString()
                    , timeBtn.getText().toString()));
        }
    }

    private TaskModel setTaskModel(Long userId, Long taskId, String title, String description, String status, String date, String time) {
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setUserId(userId);
        taskModel.setTitle(title);
        taskModel.setDescription(description);
        taskModel.setStatus(status);
        taskModel.setDate(date);
        taskModel.setTime(time);
        taskModel.setUuid(this.taskModel.getUuid());
        if (imagePath !=null) taskModel.setImagePath(imagePath);
        return taskModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isEditable) enableViews();
    }

    public EditFragmentInterface editFragmentInterface;

    public interface EditFragmentInterface {
        void onEditTaskClicked();
    }
}
