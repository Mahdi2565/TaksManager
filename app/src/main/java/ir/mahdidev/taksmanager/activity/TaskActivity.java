package ir.mahdidev.taksmanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.Const;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Intent intent = getIntent();
        UserModel userModel = (UserModel) intent.getSerializableExtra(Const.USER_MODEL_LOGGED_IN_INTENT_KEY);
        Log.e("TAG4" ,"WELCOME " + userModel.getUserName());

    }

    public static Intent newIntent(Context context , UserModel userModel){
        Intent intent = new Intent(context , TaskActivity.class);
        intent.putExtra(Const.USER_MODEL_LOGGED_IN_INTENT_KEY , userModel);
        return intent ;
    }
}
