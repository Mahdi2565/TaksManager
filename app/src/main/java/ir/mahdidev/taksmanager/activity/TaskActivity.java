package ir.mahdidev.taksmanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.adapter.ViewPagerAdapter;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.TaskRepository;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {

    private UserModel userModel;
    private TextView titleToolbar;
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Toolbar toolbar;
    private TaskRepository repository = TaskRepository.getInstance();
    private ImageView logOut;
    private ExtendedFloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initToolbar();
        TaskRepository repository = TaskRepository.getInstance();
        repository.insertTestData();
        changeStatusBarColor();
        getDataFromLogin();
        initViews();
        initViewPagerAndTablayout();
    }
    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusBar));
        }

    }

    private void initViewPagerAndTablayout() {
        String titleToolbarTxt = getResources().getString(R.string.title_toolbar)+ " " + userModel.getUserName();
        titleToolbar.setText(titleToolbarTxt);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager() , userModel.getId());
        viewPager.setAdapter(viewPagerAdapter);
        tableLayout.setupWithViewPager(viewPager);
    }

    private void initViews() {
        titleToolbar = findViewById(R.id.title_toolbar);
        tableLayout  = findViewById(R.id.tablayout);
        viewPager    = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolbar);
        logOut  = findViewById(R.id.log_out);
        fab     = findViewById(R.id.fab);
        logOut.setOnClickListener(this);
    }

    private void getDataFromLogin() {
        userModel  = (UserModel) getIntent().getSerializableExtra(Const.USER_MODEL_LOGGED_IN_INTENT_KEY);
    }

    public static Intent newIntent(Context context , UserModel userModel){
        Intent intent = new Intent(context , TaskActivity.class);
        intent.putExtra(Const.USER_MODEL_LOGGED_IN_INTENT_KEY , userModel);
        return intent ;
    }

    private void logoutFunction() {
        repository.updateLoggedIn(userModel.getUserName() , 0);
        Intent intent = MainActivity.newIntent(TaskActivity.this);
        startActivity(intent);
        finish();
    }
    private void initFab(){
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_out : {
                logoutFunction();
                break;
            }
        }
    }
}
