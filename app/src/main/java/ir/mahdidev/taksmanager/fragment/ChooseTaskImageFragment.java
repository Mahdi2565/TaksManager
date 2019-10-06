package ir.mahdidev.taksmanager.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import ir.mahdidev.taksmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseTaskImageFragment extends Fragment {

    private MaterialButton saveBtn;
    private MaterialButton cancelBtn;
    private MaterialButton captureBtn;
    private MaterialButton galleryBtn;

    public ChooseTaskImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_task_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        cancelBtnFunction();
        
    }

    private void cancelBtnFunction() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void initViews(View v) {
        saveBtn = v.findViewById(R.id.save_btn);
        cancelBtn = v.findViewById(R.id.cancel_btn);
        captureBtn= v.findViewById(R.id.capture_btn);
        galleryBtn = v.findViewById(R.id.galary_btn);
    }
}
