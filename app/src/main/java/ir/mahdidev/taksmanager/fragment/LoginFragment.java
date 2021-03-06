package ir.mahdidev.taksmanager.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.activity.TaskActivity;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.model.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText userNameEdt;
    private TextInputEditText passwordEdt;
    private ImageButton registerBtn;
    private Button loginBtn ;
    private TaskRepository repository;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        registerAndLoginFunction();
    }

    private void registerAndLoginFunction() {
        repository = TaskRepository.getInstance();
        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

    }


    private void initViews(View view){
        registerBtn = view.findViewById(R.id.register_btn_login);
        userNameEdt = view.findViewById(R.id.username_edt_login);
        passwordEdt = view.findViewById(R.id.password_edt_login);
        loginBtn    = view.findViewById(R.id.login_btn_login);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn_login: {
//                int canUseFingurePrint = BiometricUtils.isHardwareSupported(getActivity());
//                Log.e("TAG4" , canUseFingurePrint + "");
//                switch (canUseFingurePrint){
//                    case BiometricManager.BIOMETRIC_SUCCESS : {
//
//                        BiometricPrompt.PromptInfo promt = new BiometricPrompt.PromptInfo.Builder()
//                                .setTitle("Login")
//                                .setDescription("Please touch the sensor")
//                                .setDeviceCredentialAllowed(true)
//                                .build();
//
//                        BiometricPrompt biometricPrompt = new BiometricPrompt(getActivity(),
//                                executor, new BiometricPrompt.AuthenticationCallback() {
//                            @Override
//                            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
//                                super.onAuthenticationError(errorCode, errString);
//                                Log.e("TAG4" , "Login error");
//
//                            }
//
//                            @Override
//                            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
//                                super.onAuthenticationSucceeded(result);
//                                Log.e("TAG4" , "Login success");
//
//                            }
//
//                            @Override
//                            public void onAuthenticationFailed() {
//                                super.onAuthenticationFailed();
//                                Log.e("TAG4" , "Login fail");
//
//                            }
//                        });
//                        biometricPrompt.authenticate(promt);
//                        break;
//                    }
//                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE : {
//                        Log.e("TAG4" , "there is no fingerprint");
//
//                        break;
//                    }
//                }
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, new RegisterFragment()
                        , Const.REGISTER_FRAGMENT_TAG)
                        .addToBackStack(null)
                        .commit();

                break;
            }
            case R.id.login_btn_login: {
//                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                InputMethodSubtype ims = imm.getCurrentInputMethodSubtype();
//                String locale = ims.getLocale();
//                Log.e("TAG4" , locale);
                UserModel userModel ;
                String userName = userNameEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                if (userName.isEmpty() && password.isEmpty()){
                    Toast.makeText(getActivity() , "Please Fill Username and Password" , Toast.LENGTH_SHORT).show();
                    return ;
                }
                 userModel = repository.signIn(userName , password);
                if (userModel != null){
                    Intent intent = TaskActivity.newIntent(getActivity() , userModel);
                    startActivity(intent);
                    getActivity().finish();
                }else Toast.makeText(getActivity() , "uncorrect username or password" , Toast.LENGTH_SHORT).show();


                break;
            }
        }
    }
    private Handler handler = new Handler();

    private Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            handler.post(command);
            Log.e("TAG4" , "Login executer");

        }
    };
}
