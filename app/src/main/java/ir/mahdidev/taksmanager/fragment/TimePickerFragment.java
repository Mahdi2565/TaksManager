package ir.mahdidev.taksmanager.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.util.Const;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends Fragment {

    private String timeReceive;
    private Date time  ;
    private TimePicker timePicker;
    private MaterialButton save_btn;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static TimePickerFragment newInstance(String time) {

        Bundle args = new Bundle();
        args.putString(Const.TIME_PICKER_FRAGMENT , time);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            timeReceive = bundle.getString(Const.TIME_PICKER_FRAGMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setTimeToTimePicker();
        sendTimeToDialogFragment();
    }

    private void sendTimeToDialogFragment() {

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar1 = Calendar.getInstance();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    calendar1.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar1.set(Calendar.MINUTE, timePicker.getMinute());
                }
                Date date = calendar1.getTime();

                Intent intent = new Intent();
                intent.putExtra(Const.TIME_PICKER_FRAGMENT_BUNDLE_KEY, date);
                Fragment fragment = getTargetFragment();
                fragment.onActivityResult(getTargetRequestCode() , Activity.RESULT_OK,intent);

                if (getFragmentManager() != null)
                getFragmentManager().popBackStack();
            }
        });

    }

    private void setTimeToTimePicker() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        try {
            time  = format.parse(this.timeReceive);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
        }

    }

    private void initViews(View v) {
        timePicker = v.findViewById(R.id.time_picker);
        save_btn   = v.findViewById(R.id.save_btn_timePicker);
    }
}
