/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notmiyouji.newsapp.java.activity.userlogin;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.activity.CropImageToFireBase;
import com.notmiyouji.newsapp.kotlin.util.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.util.LoadUrlImage;
import com.notmiyouji.newsapp.kotlin.util.NetworkConnection;
import com.notmiyouji.newsapp.kotlin.sharedsettings.GetUserLogin;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadThemeShared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AccountSettings extends AppCompatActivity {
    private TextView fullName, username, chooseTitle;
    private TextView usernameView;
    private TextView birthdayView;
    private TextView genderView;
    private RelativeLayout changeFullName, changeUserName, changeBirthDay, changeGender, showRecoveryCode, changePassword, changeAvatar;
    private LoadFollowLanguageSystem loadFollowLanguageSystem;
    private LoadThemeShared loadThemeShared;
    private GetUserLogin getUserLogin;
    private BottomSheetDialog bottomSheetDialog;
    private MaterialAutoCompleteTextView materialAutoCompleteTextView;
    private TextInputEditText fullNameInput, userNameInput;
    private Button okBtn;
    private Intent intent;
    private String status;
    private TextInputLayout chooseHint;
    private UpdateInformation updateInformation;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();
        //Hooks
        changeFullName = findViewById(R.id.fullname_line);
        changeUserName = findViewById(R.id.username_line);
        changeBirthDay = findViewById(R.id.birthday_line);
        changeGender = findViewById(R.id.gender_line);
        changePassword = findViewById(R.id.change_password_action);
        showRecoveryCode = findViewById(R.id.view_recovery_code);
        changeAvatar = findViewById(R.id.change_avatar_action);
        NetworkConnection networkConnection = new NetworkConnection(this);
        networkConnection.observe(this, isConnected -> {
            if (isConnected) {
                //Do something when connected
                changeFullName.setEnabled(true);
                changeUserName.setEnabled(true);
                changeBirthDay.setEnabled(true);
                changeGender.setEnabled(true);
                changePassword.setEnabled(true);
                showRecoveryCode.setEnabled(true);
                changeAvatar.setEnabled(true);
            } else {
                //Do something when not connected
                changeFullName.setEnabled(false);
                changeUserName.setEnabled(false);
                changeBirthDay.setEnabled(false);
                changeGender.setEnabled(false);
                changePassword.setEnabled(false);
                showRecoveryCode.setEnabled(false);
                changeAvatar.setEnabled(false);
                Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            }
        });

        //Set textView
        fullName = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        //View to edit
        TextView fullNameView = findViewById(R.id.fullname_view);
        usernameView = findViewById(R.id.username_view);
        birthdayView = findViewById(R.id.birth_view);
        genderView = findViewById(R.id.gender_view);
        getUserLogin = new GetUserLogin(this);
        status = getUserLogin.getStatus();
        //////////////////////////////////////////////////////////////
        //Get view from shared preferences
        fullName.setText(getUserLogin.getFullName());
        username.setText("@" + getUserLogin.getUsername());
        fullNameView.setText(getUserLogin.getFullName());
        usernameView.setText("@" + getUserLogin.getUsername());
        birthdayView.setText(getUserLogin.getBirthday());
        genderView.setText(getUserLogin.getGender());
        //////////////////////////////////////////////////////////////
        updateInformation = new UpdateInformation(getUserLogin.getUserID(), this);
        //Load avatar
        ShapeableImageView avatar = findViewById(R.id.avatar_user_logined);
        LoadUrlImage loadUrlImage = new LoadUrlImage(getUserLogin.getAvatar());
        loadUrlImage.loadImageUser(avatar);
        //back button
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Change FullName
        changeFullName.setOnClickListener(v -> {
            //go to change fullName
            if (status.equals("login")) {
                //go to change fullName
                bottomSheetDialog = new BottomSheetDialog(this);
                bottomSheetDialog.setContentView(R.layout.update_fullname);
                bottomSheetDialog.show();
                fullNameInput = bottomSheetDialog.findViewById(R.id.fullname_input);
                chooseTitle = bottomSheetDialog.findViewById(R.id.choose_title);
                chooseHint = bottomSheetDialog.findViewById(R.id.hint_to_choose);
                chooseTitle.setText(R.string.enter_your_new_fullname);
                chooseHint.setHint(R.string.enter_your_new_fullname);
                okBtn = bottomSheetDialog.findViewById(R.id.btnLoad);
                assert okBtn != null;
                okBtn.setOnClickListener(v1 -> {
                    //Update full name
                    String fullname = String.valueOf(fullNameInput.getText());
                    if (fullname.isEmpty()) {
                        Toast.makeText(this, R.string.please_enter_your_new_fullname, Toast.LENGTH_SHORT).show();
                    } else {
                        fullName.setText(fullname);
                        //Update fullName
                        updateInformation.updateFullName(fullname);
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });
        //Change Username
        changeUserName.setOnClickListener(v -> {
            //go to change username
            if (status.equals("login")) {
                //go to change username
                bottomSheetDialog = new BottomSheetDialog(this);
                bottomSheetDialog.setContentView(R.layout.update_username);
                bottomSheetDialog.show();
                userNameInput = bottomSheetDialog.findViewById(R.id.username_input);
                chooseTitle = bottomSheetDialog.findViewById(R.id.choose_title);
                chooseHint = bottomSheetDialog.findViewById(R.id.hint_to_choose);
                chooseTitle.setText(R.string.enter_your_new_username);
                chooseHint.setHint(R.string.enter_your_new_username);
                okBtn = bottomSheetDialog.findViewById(R.id.btnLoad);
                assert okBtn != null;
                okBtn.setOnClickListener(v1 -> {
                    //Update username
                    String userName = String.valueOf(userNameInput.getText());
                    String email = getUserLogin.getEmail();
                    if (userName.isEmpty()) {
                        Toast.makeText(this, R.string.please_enter_your_new_username, Toast.LENGTH_SHORT).show();
                    } else {
                        //Update full name
                        usernameView.setText("@" + username);
                        updateInformation.checkUserName(userName, email);
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });
        //Change Birthday
        changeBirthDay.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            String userBirthday = getUserLogin.getBirthday();
            if (userBirthday != null && userBirthday.equals("not_input")) {
                calendar.setTime(new Date());
            } else {
                calendar.setTime(getDay(userBirthday));
            }
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                //Set Birthday
                //Open Android DatePicker
                @SuppressLint("DefaultLocale")
                String dateString = String.format("%d-%02d-%02d", year, (month + 1), dayOfMonth);
                if (status.equals("login")) {
                    updateInformation.updateBirthday(dateString);
                }
                //Update Birthday
                birthdayView.setText(dateString);
                //Update Birthday to database

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            //Date Picker Dialog show date like textView
            datePickerDialog.show();

        });
        //Change Gender
        changeGender.setOnClickListener(v -> {
            //Open BottomSheetDialog
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.choose_gender);
            bottomSheetDialog.show();
            materialAutoCompleteTextView = bottomSheetDialog.findViewById(R.id.spinner_gender);
            chooseTitle = bottomSheetDialog.findViewById(R.id.choose_title);
            chooseHint = bottomSheetDialog.findViewById(R.id.hint_to_choose);
            chooseTitle.setText(R.string.choose_gender);
            chooseHint.setHint(R.string.select_your_gender);
            materialAutoCompleteTextView.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, genderList()));
            okBtn = bottomSheetDialog.findViewById(R.id.btnLoad);
            assert okBtn != null;
            okBtn.setOnClickListener(v1 -> {
                bottomSheetDialog.dismiss();
                genderView.setText(materialAutoCompleteTextView.getText().toString());
                if (status.equals("login")) {
                    updateInformation.updateGender(materialAutoCompleteTextView.getText().toString());
                }
            });

        });
        //Show Recovery Code
        showRecoveryCode.setOnClickListener(v -> {
            if (status.equals("login")) {
                intent = new Intent(this, ViewRecoveryCode.class);
                startActivity(intent);
            }
        });
        //Change Password
        changePassword.setOnClickListener(v -> {
            if (status.equals("login")) {
                intent = new Intent(this, ChangePassword.class);
                startActivity(intent);
            }
        });
        //Change Avatar
        changeAvatar.setOnClickListener(v -> {
            if (status.equals("login")) {
                //open material dialog to tell user about go gravatar change your avatar
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle(R.string.change_avatar_account);
                builder.setView(R.layout.show_gravatar_text);
                builder.setPositiveButton(R.string.go_gravatar, (dialog, which) -> {
                    //Go to gravatar
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://gravatar.com"));
                    startActivity(intent);
                });
                builder.setNeutralButton(R.string.picture_library, (dialog, which) -> mGetContent.launch("image/*"));
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        });
    }

    //Open Image chooser
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            //Crop Image
            Intent cropIntent = new Intent(this, CropImageToFireBase.class);
            cropIntent.putExtra("imageUri", uri.toString());
            startActivity(cropIntent);
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

    private Date getDay(String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date getDate = null;
        try {
            getDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getDate;
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(AccountSettings.this).toBundle();
            finish();
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }
    @Override
    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        loadThemeShared = new LoadThemeShared(this);
        loadThemeShared.setTheme();
    }
}