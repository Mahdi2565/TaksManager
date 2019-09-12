package ir.mahdidev.taksmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

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
        hideStatusBar();
        TaskRepository repository = TaskRepository.getInstance();
        UserModel userModel = repository.UserLoggedIn();
        if (userModel != null){
            Intent intent = TaskActivity.newIntent(this , userModel);
            startActivity(intent);
            finish();
        }
    }
    private void hideStatusBar() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

}
