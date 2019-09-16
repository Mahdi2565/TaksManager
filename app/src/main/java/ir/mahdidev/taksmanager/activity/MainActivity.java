package ir.mahdidev.taksmanager.activity;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.fragment.LoginFragment;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.ConnectivityReceiver;
import ir.mahdidev.taksmanager.util.G;
import ir.mahdidev.taksmanager.util.TaskRepository;

public class MainActivity extends SingleFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    @Override
    public Fragment fragment() {
        return new LoginFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeStatusBarColor();
        TaskRepository repository = TaskRepository.getInstance();
        UserModel userModel = repository.UserLoggedIn();
        if (userModel != null){
            Intent intent = TaskActivity.newIntent(this , userModel);
            startActivity(intent);
            finish();
        }
    }

    public static Intent newIntent(Context context ){
        return new Intent(context , MainActivity.class);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.statusBar));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        G.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.e("TAG4" , "MAin activity " + isConnected);
    }
}
