package ir.mahdidev.taksmanager.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.List;
import java.util.UUID;

import ir.mahdidev.taksmanager.R;
import ir.mahdidev.taksmanager.model.TaskModel;
import ir.mahdidev.taksmanager.model.TaskRepository;
import ir.mahdidev.taksmanager.util.Const;
import ir.mahdidev.taksmanager.util.PictureUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseTaskImageFragment extends Fragment {

    private MaterialButton saveBtn;
    private MaterialButton cancelBtn;
    private MaterialButton captureBtn;
    private MaterialButton galleryBtn;
    private ImageView taskImage;
    private File imageFile;
    private Uri imageUri;
    private String imagePath;
    private TaskRepository taskRepository = TaskRepository.getInstance();
    private UUID uuid;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 3;
    private static final String FILE_PROVIDER_AUTHORITY = "ir.mahdidev.taksmanager.fileProvider";
    private Intent takePictureIntent ;
    private TaskModel taskModel;
    public ChooseTaskImageFragment() {
    }

    public static ChooseTaskImageFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable(Const.CHOOSE_IMAGE_FRAGMENT_UUID_BUNDLE_KEY , uuid);
        ChooseTaskImageFragment fragment = new ChooseTaskImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments() ;
        if (bundle != null){
            uuid = (UUID) bundle.getSerializable(Const.CHOOSE_IMAGE_FRAGMENT_UUID_BUNDLE_KEY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_task_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        readTaskFromDataBase();
        cancelBtnFunction();
        captureBtnFunction();
        galleryBtnFunction();
        saveBtnFunction();
    }

    private void readTaskFromDataBase() {
        taskModel = taskRepository.readTask(uuid);
        if (taskModel!=null && taskModel.getImagePath() != null){
            imagePath = taskModel.getImagePath();
            updateImageView();
        }
    }

    private void saveBtnFunction() {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagePath == null){
                    Toast.makeText(getActivity() , getActivity().getResources()
                                    .getString(R.string.select_image)
                            , Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent();
                    Fragment fragment = getTargetFragment();
                    intent.putExtra(Const.CHOOSE_IMAGE_FRAGMENT_IMAGE_PATH_INTENT_KEY , imagePath);
                    fragment.onActivityResult(getTargetRequestCode() , Activity.RESULT_OK , intent);
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                }
            }
        });
    }

    private void galleryBtnFunction() {
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    String [] mimeType = {"image/jpeg" , "image/png"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES , mimeType);
                    startActivityForResult(intent , REQUEST_IMAGE_GALLERY);
                }
            }
        });
    }
    private boolean checkPermission (){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) return true;
    if (ActivityCompat.checkSelfPermission(getActivity() , Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
        ,REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
    return false;
    }else return true;
    }
    private void captureBtnFunction() {
        imageFile = taskRepository.getImageFile(uuid);
        imageUri  = FileProvider.getUriForFile(getActivity() , FILE_PROVIDER_AUTHORITY ,
                imageFile);
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT , imageUri);
                grantCameraPermission(imageUri);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager())!= null){
                startActivityForResult(takePictureIntent , REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    private void cancelBtnFunction() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void initViews(View v) {
        saveBtn = v.findViewById(R.id.save_btn);
        cancelBtn = v.findViewById(R.id.cancel_btn);
        captureBtn= v.findViewById(R.id.capture_btn);
        galleryBtn = v.findViewById(R.id.galary_btn);
        taskImage  = v.findViewById(R.id.task_image);
    }
    private void grantCameraPermission(Uri photoUri) {
        List<ResolveInfo> cameraActivities = getActivity().getPackageManager()
                .queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo activity: cameraActivities) {
            getActivity().grantUriPermission(activity.activityInfo.packageName,
                    photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_IMAGE_CAPTURE){
            getActivity().revokeUriPermission(imageUri , Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            imagePath = imageFile.getAbsolutePath();
            updateImageView();
        }else if (requestCode == REQUEST_IMAGE_GALLERY){
            Uri imageUri = data.getData();
            String [] index ={MediaStore.Images.Media.DATA};
           Cursor cursor = getActivity().getContentResolver().query(imageUri , index , null
           , null , null);

           cursor.moveToFirst();
           imagePath = cursor.getString(cursor.getColumnIndex(index[0]));
           updateImageView();
        }
    }

    private void updateImageView() {
            Bitmap bitmap = PictureUtils.getScaledBitmap(imagePath ,getActivity());
            taskImage.setImageBitmap(bitmap);
    }
}
