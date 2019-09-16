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
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.util.Const;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends Fragment {

    private String dateReceive;
    private Date date  ;
    private DatePicker datePicker;
    private MaterialButton save_btn;
    public DatePickerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            dateReceive = bundle.getString(Const.DATE_PICKER_FRAGMENT);
        }
    }

    public static DatePickerFragment newInstance(String date) {

        Bundle args = new Bundle();
        args.putString(Const.DATE_PICKER_FRAGMENT , date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setDateToDatePicker();
        sendDateToDialogFragment();
    }

    private void sendDateToDialogFragment() {

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               int year = datePicker.getYear();
               int month = datePicker.getMonth();
               int day   = datePicker.getDayOfMonth();
                GregorianCalendar gregorianCalendar = new GregorianCalendar(year , month , day);
                Date date = gregorianCalendar.getTime();
                Fragment fragment= getTargetFragment();
                Intent intent = new Intent();
                intent.putExtra(Const.DATE_PICKER_FRAGMENT_BUNDLE_KEY , date);
                fragment.onActivityResult(getTargetRequestCode() , Activity.RESULT_OK , intent);

                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

    }

    private void setDateToDatePicker() {

        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy", Locale.US);
        try {
           date  = format.parse(this.dateReceive);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        datePicker.init(calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH) , calendar.get(Calendar.DAY_OF_MONTH) , null);
    }

    private void initViews(View v) {
        datePicker = v.findViewById(R.id.date_picker);
        save_btn   = v.findViewById(R.id.save_btn);
    }

}
