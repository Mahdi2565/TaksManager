package ir.mahdidev.taksmanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.fragment.EditProfileFragment;
import ir.mahdidev.taksmanager.fragment.MainFragment;
import ir.mahdidev.taksmanager.fragment.UserProfileListFragment;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.ConnectivityReceiver;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.EventBusSearchEvent;
import ir.mahdidev.taksmanager.util.G;
import ir.mahdidev.taksmanager.model.TaskRepository;

public class TaskActivity extends SingleFragmentActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener , MainFragment.MainFragmentInterface {

    private Toolbar toolbar;
    private TextView titleToolbar;
    private TaskRepository repository = TaskRepository.getInstance();
    private UserModel userModel ;
    private MaterialCardView searchCardView;
    private TextInputEditText searchEdt;


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
        initToolbar();
        changeStatusBarColor();
        searchFunction();
    }

    private void searchFunction() {
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                EventBus.getDefault().post(new EventBusSearchEvent(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        String titleToolbarTxt = getResources().getString(R.string.title_toolbar)+ " " + userModel.getUserName();
        titleToolbar.setText(titleToolbarTxt);
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
        searchCardView = findViewById(R.id.search_cardview);
        searchEdt = findViewById(R.id.search_edt);
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
    public void onEditProfileClicked() {
        EditProfileFragment editProfile = EditProfileFragment.newInstance(userModel.getId());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout , editProfile)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_btn).getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            return true;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(userModel.getIsAdmin()==1) {
            invalidateOptionsMenu();
            menu.findItem(R.id.users_profile).setVisible(true);
        }else menu.findItem(R.id.users_profile).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out : {
                logoutFunction();
                break;
            }
            case R.id.users_profile :{
                UserProfileListFragment userProfileListFragment = UserProfileListFragment.newInstance(userModel.getId());
                if (getSupportFragmentManager().findFragmentByTag(Const.USER_PROFILE_LIST_TAG) != userProfileListFragment){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout
                            ,userProfileListFragment , Const.USER_PROFILE_LIST_TAG)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            }
            case R.id.search_btn : {
                Animation animation = AnimationUtils.loadAnimation(TaskActivity.this , android.R.anim.slide_in_left);
                searchCardView.setVisibility(View.VISIBLE);
                searchCardView.startAnimation(animation);
                titleToolbar.setVisibility(View.GONE);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchCardView.getVisibility()==View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(TaskActivity.this , android.R.anim.slide_out_right);
            searchCardView.setVisibility(View.GONE);
            searchCardView.startAnimation(animation);
            searchEdt.setText("");
            titleToolbar.setVisibility(View.VISIBLE);
        } else
            super.onBackPressed();

    }
}
