package ir.mahdidev.taksmanager.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ir.mahdidev.taksmanager.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    public abstract Fragment fragment();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout ,fragment())
                .commit();
    }
}
