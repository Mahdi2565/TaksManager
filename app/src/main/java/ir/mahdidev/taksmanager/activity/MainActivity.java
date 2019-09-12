package ir.mahdidev.taksmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.fragment.LoginFragment;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.TaskRepository;

public class MainActivity extends SingleFragmentActivity {

    @Override
    public Fragment fragment() {
        return new LoginFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TaskRepository repository = TaskRepository.getInstance();
        UserModel userModel = repository.UserLoggedIn();
        if (userModel != null){
            Intent intent = TaskActivity.newIntent(this , userModel);
            startActivity(intent);
            finish();
        }

    }
}
