package com.notmiyouji.newsapp.java.Signed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoadImageURL;
import com.notmiyouji.newsapp.kotlin.SharedSettings.GetUserLogined;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AccountSettings extends AppCompatActivity {
    TextView fullName, username, chooseTitle;
    TextView fullnameView,usernameView, birthdayView, genderView;
    RelativeLayout changeFullName, changeUserName, changeBirthDay, changeGender, showRecoveryCode, changePassword, changeAvatar;
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    GetUserLogined getUserLogined;
    BottomSheetDialog bottomSheetDialog;
    MaterialAutoCompleteTextView materialAutoCompleteTextView;
    Button okbtn;
    Intent intent;
    RelativeLayout photoLibrary, camera;
    String status;
    TextInputLayout chooseHint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

        //Set textView
        fullName = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        //View to edit
        fullnameView = findViewById(R.id.fullname_view);
        usernameView = findViewById(R.id.username_view);
        birthdayView = findViewById(R.id.birth_view);
        genderView = findViewById(R.id.gender_view);
        getUserLogined = new GetUserLogined(this);
        status = getUserLogined.getStatus();
        //////////////////////////////////////////////////////////////
        //Get view from shared preferences
        fullName.setText(getUserLogined.getFullname());
        username.setText("@" + getUserLogined.getUsername());
        fullnameView.setText(getUserLogined.getFullname());
        usernameView.setText("@" + getUserLogined.getUsername());
        birthdayView.setText(getUserLogined.getBirthday());
        genderView.setText(getUserLogined.getGender());
        //////////////////////////////////////////////////////////////
        //Load avatar
        ShapeableImageView avatar = findViewById(R.id.avatar_user_logined);
        LoadImageURL loadImageURL = new LoadImageURL(getUserLogined.getAvatar());
        loadImageURL.loadImageUser(avatar);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Change Fullname
        changeFullName = findViewById(R.id.fullname_line);
        changeFullName.setOnClickListener(v -> {
            //go to change fullname
            if (status.equals("login")) {
                //go to change fullname
            } else {
                //SSO can't edit
                Toast.makeText(this, R.string.single_sign_on_can_not_edit, Toast.LENGTH_SHORT).show();
            }
        });
        //Change Username
        changeUserName = findViewById(R.id.username_line);
        changeUserName.setOnClickListener(v -> {
            //go to change username
            if (status.equals("login")) {
                //go to change fullname
            } else {
                //SSO can't edit
                Toast.makeText(this, R.string.single_sign_on_can_not_edit, Toast.LENGTH_SHORT).show();
            }
        });
        //Change Birthday
        changeBirthDay = findViewById(R.id.birthday_line);
        changeBirthDay.setOnClickListener(v -> {
            if (status.equals("login")) {
                //Open Android DatePicker
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(getDay(getUserLogined.getBirthday()));
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                    //Set Birthday
                    @SuppressLint("DefaultLocale")
                    String dateString = String.format("%d-%02d-%02d", year, month, dayOfMonth);
                    //Update Birthday
                    birthdayView.setText(dateString);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                //Date Picker Dialog show date like textView
                datePickerDialog.show();
            } else {
                //SSO can't edit
                Toast.makeText(this, R.string.single_sign_on_can_not_edit, Toast.LENGTH_SHORT).show();
            }

        });
        //Change Gender
        changeGender = findViewById(R.id.gender_line);
        changeGender.setOnClickListener(v-> {
            if (status.equals("login")) {
                //Open BottomSheetDialog
                bottomSheetDialog = new BottomSheetDialog(this);
                bottomSheetDialog.setContentView(R.layout.choose_gender);
                bottomSheetDialog.show();
                materialAutoCompleteTextView = bottomSheetDialog.findViewById(R.id.spinner_gender);
                chooseTitle = bottomSheetDialog.findViewById(R.id.choose_title);
                chooseHint = bottomSheetDialog.findViewById(R.id.hint_to_choose);
                chooseTitle.setText(R.string.choose_gender);
                chooseHint.setHint(R.string.select_your_gender);
                materialAutoCompleteTextView.setAdapter(new ArrayAdapter<>(this,
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, genderList()));
                okbtn = bottomSheetDialog.findViewById(R.id.btnLoad);
                okbtn.setOnClickListener(v1 -> {

                });
            } else {
                //SSO can't edit
                Toast.makeText(this, R.string.single_sign_on_can_not_edit, Toast.LENGTH_SHORT).show();
            }
        });
        //Show Recovery Code
        showRecoveryCode = findViewById(R.id.view_recovery_code);
        showRecoveryCode.setOnClickListener(v -> {
            if (status.equals("login")) {
                //go to change fullname
            } else {
                //SSO can't edit
                Toast.makeText(this, R.string.single_sign_on_can_not_edit, Toast.LENGTH_SHORT).show();
            }

        });
        //Change Password
        changePassword = findViewById(R.id.change_password_action);
        changePassword.setOnClickListener(v -> {
            if (status.equals("login")) {

            } else {
                //SSO can't edit
                Toast.makeText(this, R.string.single_sign_on_can_not_edit, Toast.LENGTH_SHORT).show();
            }
        });
        //Change Avatar
        changeAvatar = findViewById(R.id.change_avatar_action);
        changeAvatar.setOnClickListener(v -> {
            if (status.equals("login")) {
                //open bottom sheet dialog with 2 option: Photo Library and Camera
                bottomSheetDialog = new BottomSheetDialog(this);
                bottomSheetDialog.setContentView(R.layout.choose_avatar);
                bottomSheetDialog.show();
                //Photo Library
                photoLibrary = bottomSheetDialog.findViewById(R.id.openphotolibrary);
                photoLibrary.setOnClickListener(v1 -> {
                    //open photo library
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    mGetContent.launch(intent);
                });
                //Camera
                camera = bottomSheetDialog.findViewById(R.id.opencamera);
                camera.setOnClickListener(v1 -> {
                    //CameraX is a new camera API from Google
                    //in future, we will use CameraX to open camera
                    //but now, we will use old camera API
                    //open camera
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        openCamera.launch(intent);
                    }
                    else {
                        Toast.makeText(this, R.string.cannot_open_default_camera, Toast.LENGTH_SHORT).show();
                    }


                });

            } else {
                //SSO can't edit
                Toast.makeText(this, R.string.single_sign_on_can_not_edit, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //This is a photo library launcher
    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            //Get image from photo library
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> openCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            //Get image from camera
                            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                            //set image to image view
                            //Upload image to server

                        }
                    }
                }
            });
    //create list gender
    private List<String> genderList() {
        List<String> gender = new ArrayList<>();
        gender.add(getString(R.string.male));
        gender.add(getString(R.string.female));
        gender.add(getString(R.string.other_gender));
        return gender;
    }

    //get default camera package
    private String getDefaultCameraPackage() {
        String defaultCameraPackage = null;
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (int n=0;n<list.size();n++) {
            if((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM)==1)
            {

                if(list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
                    defaultCameraPackage = list.get(n).packageName;
                    break;
                }
            }
        }
        return defaultCameraPackage;
    }
    private Date getDay(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date getDate = null;
        try {
            getDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getDate;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }
}