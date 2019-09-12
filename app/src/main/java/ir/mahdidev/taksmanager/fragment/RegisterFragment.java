package ir.mahdidev.taksmanager.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.util.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private TextInputEditText userNameEdt;
    private TextInputEditText passwordEdt;
    private TextInputEditText emailEdt;
    private TextInputEditText ageEdt;
    private CheckBox isAdminCheckBox;
    private Button signUpBtn;


    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        signUpFunction();
    }

    private void signUpFunction() {
        final TaskRepository repository = TaskRepository.getInstance();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String userName = userNameEdt.getText().toString().trim();
            String password = passwordEdt.getText().toString().trim();
            String email    = emailEdt.getText().toString().trim();
            String age      = ageEdt.getText().toString().trim();
            boolean isAdmin = isAdminCheckBox.isChecked();

                if (!(userName.length()> 6)){
                    Toast.makeText(getActivity() , "Invalid user !" , Toast.LENGTH_SHORT ).show();
                    return;
                }

                if (!(password.length() > 6)){

                    Toast.makeText(getActivity() , "Invalid password !" , Toast.LENGTH_SHORT ).show();

                    return;
                }

                if (!EMailValidation(email)){

                    Toast.makeText(getActivity() , "Invalid Email !" , Toast.LENGTH_SHORT ).show();
                    return;
                }

                if (age.isEmpty()){
                    Toast.makeText(getActivity() , "Invalid Age !" , Toast.LENGTH_SHORT ).show();

                    return;
                }

                if (repository.checkUserExists(userName)){
                    Toast.makeText(getActivity() , "This userName exist" , Toast.LENGTH_SHORT ).show();
                    return;
                }

                boolean isInsertToDb =  repository.insertUserToDb(userName , password
                , email , age, isAdmin);
                if (isInsertToDb)
                    if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

    }

    private void initViews(View view) {
        userNameEdt = view.findViewById(R.id.username_edt_register);
        passwordEdt = view.findViewById(R.id.password_edt_register);
        emailEdt    = view.findViewById(R.id.email_edt_register);
        ageEdt      = view.findViewById(R.id.age_edt_register);
        isAdminCheckBox = view.findViewById(R.id.is_admin_checkbox_register);
        signUpBtn = view.findViewById(R.id.sign_up_btn_register);
    }

    private boolean EMailValidation(String emailstring) {
        if (null == emailstring || emailstring.length() == 0) {
            return false;
        }
        Pattern emailPattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(emailstring);
        return emailMatcher.matches();
    }

}
