package ir.mahdidev.taksmanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.tabs.TabLayout;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.adapter.ViewPagerAdapter;
import ir.mahdidev.taksmanager.fragment.TaskDialogFragment;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.ConnectivityReceiver;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.G;
import ir.mahdidev.taksmanager.util.TaskRepository;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener, TaskDialogFragment.TaskDialogInterface, ConnectivityReceiver.ConnectivityReceiverListener {

    private UserModel userModel;
    private TextView titleToolbar;
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Toolbar toolbar;
    private TaskRepository repository = TaskRepository.getInstance();
    private ImageView logOut;
    private com.github.clans.fab.FloatingActionButton addTask ;
    private com.github.clans.fab.FloatingActionButton removeTasks ;
    private com.github.clans.fab.FloatingActionButton editProfile ;
    private FloatingActionMenu fab_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initToolbar();
        changeStatusBarColor();
        getDataFromLogin();
        initViews();
        initViewPagerAndTablayout();
        initFab();
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
        viewPager.setOffscreenPageLimit(3);
        tableLayout.setupWithViewPager(viewPager);
    }

    private void initViews() {
        titleToolbar = findViewById(R.id.title_toolbar);
        tableLayout  = findViewById(R.id.tablayout);
        viewPager    = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolbar);
        logOut  = findViewById(R.id.log_out);
        addTask     = findViewById(R.id.add_task);
        removeTasks = findViewById(R.id.remove_tasks);
        editProfile = findViewById(R.id.edit_profile);
        fab_menu    = findViewById(R.id.fab_menu);
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
    addTask.setOnClickListener(this);
    removeTasks.setOnClickListener(this);
    editProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_out : {
                logoutFunction();
                break;
            }
            case R.id.add_task :{
                TaskDialogFragment dialogFragment = TaskDialogFragment.newInstance(Const.Add_TASK_MODE , Const.TASK_DIALOG_DEFAULT_TASK_ID
                , userModel.getId());
                dialogFragment.show(getSupportFragmentManager() , Const.ADD_DIALOG_FRAGMENT_TAG);
                fab_menu.close(true);
                break;
            }
            case R.id.remove_tasks : {
                TaskDialogFragment dialogFragment = TaskDialogFragment.newInstance(Const.DELETE_TASK_MODE , Const.TASK_DIALOG_DEFAULT_TASK_ID
                        , userModel.getId());
                dialogFragment.show(getSupportFragmentManager() , Const.DELETE_ALL_TASK_DIALOG_FRAGMENT_TAG);
                fab_menu.close(true);
                break;
            }
            case R.id.edit_profile : {

                fab_menu.close(true);
                break;
            }
        }
    }

    @Override
    public void onSavedClicked() {
        viewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        G.getInstance().setConnectivityListener(this);

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.e("TAG4" , "TASK activity " + isConnected);
    }


}
