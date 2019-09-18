package ir.mahdidev.taksmanager.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.fragment.EditProfileFragment;
import ir.mahdidev.taksmanager.fragment.MainFragment;
import ir.mahdidev.taksmanager.fragment.UserProfileListFragment;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.ConnectivityReceiver;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.G;
import ir.mahdidev.taksmanager.util.TaskRepository;

public class TaskActivity extends SingleFragmentActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener , MainFragment.MainFragmentInterface {

    private Toolbar toolbar;
    private TextView titleToolbar;
    private TaskRepository repository = TaskRepository.getInstance();
    private ImageView logOut;
    private UserModel userModel ;
    private ImageView users_profile ;


    @Override
    public Fragment fragment() {
        userModel  = (UserModel) getIntent().getSerializableExtra(Const.USER_MODEL_LOGGED_IN_INTENT_KEY);
        return MainFragment.newInstance(userModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initViews();
        setToolbarTitle();
        initToolbar();
        changeStatusBarColor();
    }
    private void initToolbar() {
        setSupportActionBar(toolbar);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutFunction();
            }
        });
        if (userModel.getIsAdmin()==1){
            users_profile.setVisibility(View.VISIBLE);
            users_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserProfileListFragment userProfileListFragment = UserProfileListFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout ,userProfileListFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusBar));
        }

    }


    private void initViews() {
        toolbar      = findViewById(R.id.toolbar);
        titleToolbar = findViewById(R.id.title_toolbar);
        logOut  = findViewById(R.id.log_out);
        users_profile = findViewById(R.id.users_profile);
    }

    private void setToolbarTitle() {
        String titleToolbarTxt = getResources().getString(R.string.title_toolbar)+ " " + userModel.getUserName();
        titleToolbar.setText(titleToolbarTxt);
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

    @Override
    protected void onResume() {
        super.onResume();
        G.getInstance().setConnectivityListener(this);

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.e("TAG4" , "TASK activity " + isConnected);
    }


    @Override
    public void onReceive() {
        EditProfileFragment editProfile = EditProfileFragment.newInstance(userModel.getId());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout , editProfile)
                .addToBackStack(null)
                .commit();

    }
}
