package ir.mahdidev.taksmanager.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.model.UserModel;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.model.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private TextInputEditText userNameEdt;
    private TextInputEditText passwordEdt;
    private TextInputEditText emailEdt;
    private TextInputEditText ageEdt;
    private CircularImageView userImage ;
    private CheckBox isAdminCheckBox;
    private Button signUpBtn;
    private boolean isPermisionGranted ;
    private Uri uriImage;
    private Bitmap imageBitmap = null ;
    private TaskRepository repository = TaskRepository.getInstance();


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
        selectUserImage();
        signUpFunction();
    }

    private void selectUserImage() {
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    isPermisionGranted = checkPermision();
                    if (!isPermisionGranted) {
                        Toast.makeText(getActivity() , getResources().getString(R.string.no_permision_image_user) , Toast.LENGTH_SHORT).show();
                    }else {
                        getImageFromGalary();
                    }
                }else {
                    getImageFromGalary();
                }
            }
        });
    }

    private void getImageFromGalary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, Const.GET_IMAGE_REQUEST_CODE);
    }

    private void signUpFunction() {

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
                if (imageBitmap == null){

                   imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.user_png_white);
                }

                boolean isInsertToDb =  repository.insertUserToDb(setUserModel(userName , password
                        , email , age, isAdmin , imageBitmap));
                if (isInsertToDb)
                    if (getFragmentManager() != null ) {
                    getFragmentManager().popBackStack();
                }
            }
        });

    }

    private UserModel setUserModel(String userName, String password, String email, String age, boolean isAdmin, Bitmap imageBitmap) {
        Date date = new Date();
        byte[] image = repository.getBitmapAsByteArray(imageBitmap);
        UserModel userModel = new UserModel();
        userModel.setIsAdmin(isAdmin?1:0);
        userModel.setImageUser(image);
        userModel.setUserName(userName);
        userModel.setPassword(password);
        userModel.setEmail(email);
        userModel.setAge(age);
        userModel.setRegisterDate(date.toString());
        return userModel ;
    }

    private void initViews(View view) {
        userNameEdt = view.findViewById(R.id.username_edt_register);
        passwordEdt = view.findViewById(R.id.password_edt_register);
        emailEdt    = view.findViewById(R.id.email_edt_register);
        ageEdt      = view.findViewById(R.id.age_edt_register);
        isAdminCheckBox = view.findViewById(R.id.is_admin_checkbox_register);
        signUpBtn = view.findViewById(R.id.sign_up_btn_register);
        userImage = view.findViewById(R.id.user_img);
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
        private boolean checkPermision() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.READ_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity() , new String[] {Manifest.permission.READ_EXTERNAL_STORAGE ,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE} , Const.READ_INTERNAL_STORAGE_PERMISION);
                return false;
            }else return true ;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case Const.READ_INTERNAL_STORAGE_PERMISION :{
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity() , "GRANTED" , Toast.LENGTH_SHORT).show();
                    isPermisionGranted = true ;
                }
                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK){

            if (requestCode == Const.GET_IMAGE_REQUEST_CODE){

                if (data != null) uriImage = data.getData();

                String [] filePathArray = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(uriImage , filePathArray , null , null ,null);

                assert cursor != null;
                cursor.moveToNext();

                int columindex = cursor.getColumnIndex(filePathArray[0]);

                String path = cursor.getString(columindex);
                cursor.close();

                userImage.setImageBitmap(BitmapFactory.decodeFile(path));

                imageBitmap = BitmapFactory.decodeFile(path) ;


            }
        }
    }

}
