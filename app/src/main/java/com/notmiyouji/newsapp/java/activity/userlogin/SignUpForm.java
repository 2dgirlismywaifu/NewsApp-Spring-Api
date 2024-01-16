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
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.retrofit.NewsAppApi;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.NewsAppInterface;
import com.notmiyouji.newsapp.kotlin.Utils;
import com.notmiyouji.newsapp.kotlin.model.SignUp;
import com.notmiyouji.newsapp.kotlin.model.VerifyNickNameEmail;
import com.notmiyouji.newsapp.kotlin.sharedsettings.LoadFollowLanguageSystem;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class SignUpForm extends AppCompatActivity {

    private Button signUpBtn;
    private TextInputEditText fullName, email, password, confirmPassword, username;
    private LoadFollowLanguageSystem loadFollowLanguageSystem;
    private final NewsAppInterface newsAPPInterface = NewsAppApi.getAPIClient().create(NewsAppInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_form);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

        fullName = findViewById(R.id.fullname_input);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);
        confirmPassword = findViewById(R.id.Repassword_input);
        username = findViewById(R.id.recovey_code);
        Button signInBtn = findViewById(R.id.ResendCodeBtn);
        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Sign Up form only access from Sign In form
        signInBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //sign up button
        signUpBtn = findViewById(R.id.VerifiedButton);
        signUpBtn.setOnClickListener(v -> {
            //disable button
            signUpBtn.setEnabled(false);
            //check input
            checkInput();
            //if all input is not null, sign up
            if (!String.valueOf(fullName.getText()).isEmpty()
                    && !String.valueOf(email.getText()).isEmpty()
                    && !String.valueOf(password.getText()).isEmpty()
                    && !String.valueOf(confirmPassword.getText()).isEmpty()
                    && !String.valueOf(username.getText()).isEmpty()) {
                //if password and confirm password is not same, show error
                if (!String.valueOf(password.getText()).equals(String.valueOf(confirmPassword.getText()))) {
                    password.setError(getString(R.string.password_is_not_same));
                    confirmPassword.setError(getString(R.string.password_is_not_same));
                    Toast.makeText(this, R.string.password_is_not_same, Toast.LENGTH_SHORT).show();
                    signUpBtn.setEnabled(true);
                }
                //regex email
                else if (!String.valueOf(email.getText()).matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    email.setError(getString(R.string.email_is_not_valid));
                    Toast.makeText(this, R.string.email_is_not_valid, Toast.LENGTH_SHORT).show();
                    signUpBtn.setEnabled(true);
                }
                //if password not contain al least 6 character, show error
                else if (String.valueOf(password.getText()).length() < 6) {
                    password.setError(getString(R.string.password_must_be_at_least_6_character));
                    Toast.makeText(this, R.string.password_must_be_at_least_6_character, Toast.LENGTH_SHORT).show();
                    signUpBtn.setEnabled(true);
                }
                //password must contain at least 1 number, 1 uppercase, 1 special character
                else if (!String.valueOf(password.getText()).matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$")) {
                    password.setError(getString(R.string.password_must_contain_at_least_1_number_1_uppercase_1_special_character));
                    Toast.makeText(this, R.string.password_must_contain_at_least_1_number_1_uppercase_1_special_character, Toast.LENGTH_SHORT).show();
                    signUpBtn.setEnabled(true);
                } else {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle(R.string.sign_up);
                    builder.setMessage(R.string.make_sure_email_correct);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                        //check nickname
                        checkNicknameAndEmail(String.valueOf(username.getText()), String.valueOf(email.getText()));
                    });
                    builder.setNegativeButton(R.string.no, (dialog, which) -> {
                        dialog.dismiss();
                        signUpBtn.setEnabled(true);
                    });
                    builder.show();
                }
            }
        });
    }
    OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            ActivityOptions.makeSceneTransitionAnimation(SignUpForm.this).toBundle();
            finish();
        }
    };

    public OnBackPressedCallback getCallback() {
        return callback;
    }

    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }

    private void checkInput() {
        //if all input is null, text input layout will show error
        if (String.valueOf(fullName.getText()).isEmpty()) {
            fullName.setError("Full name is required");
            signUpBtn.setEnabled(true);
        }
        if (String.valueOf(email.getText()).isEmpty()) {
            email.setError("Email is required");
            signUpBtn.setEnabled(true);
        }
        if (String.valueOf(password.getText()).isEmpty()) {
            password.setError("Password is required");
            signUpBtn.setEnabled(true);
        }
        if (String.valueOf(confirmPassword.getText()).isEmpty()) {
            confirmPassword.setError("Confirm password is required");
            signUpBtn.setEnabled(true);
        }
        if (String.valueOf(username.getText()).isEmpty()) {
            username.setError("Username is required");
            signUpBtn.setEnabled(true);
        }
    }

    private void checkNicknameAndEmail(String userNameInput, String emailInput) {
        //Use Retrofit to check nickname
        Call<VerifyNickNameEmail> call = newsAPPInterface.verifyNickNameAndEmail(Utils.encodeToBase64(userNameInput), Utils.encodeToBase64(emailInput));
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<VerifyNickNameEmail> call, @NonNull Response<VerifyNickNameEmail> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (Objects.equals(response.body().getEmail(), emailInput)
                            && Objects.equals(response.body().getStatus(), "used")) {
                        email.setError(getString(R.string.email_is_already_used));
                        signUpBtn.setEnabled(true);
                    }
                    if (Objects.equals(response.body().getNickname(), userNameInput)
                            && Objects.equals(response.body().getStatus(), "used")) {
                        username.setError(getString(R.string.nickname_is_already_used));
                        signUpBtn.setEnabled(true);
                    }
                    if (Objects.equals(response.body().getStatus(), "empty")) {
                        RegisterAccount(String.valueOf(fullName.getText()),
                                String.valueOf(email.getText()).toLowerCase(),
                                String.valueOf(password.getText()),
                                String.valueOf(username.getText()));
                    }
                } else  {
                    Toast.makeText(SignUpForm.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    signUpBtn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyNickNameEmail> call, @NonNull Throwable t) {
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void RegisterAccount(String fullName, String email, String password, String username) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                //Send email verification
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                assert firebaseUser != null;
                firebaseUser.sendEmailVerification().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(SignUpForm.this, "NewsApp")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(getString(R.string.sign_up))
                                .setContentText(getString(R.string.a_confirmation_email_has_been_sent_to_your_mailbox))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        Toast.makeText(SignUpForm.this, R.string.a_confirmation_email_has_been_sent_to_your_mailbox, Toast.LENGTH_SHORT).show();
                        //Create notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(SignUpForm.this);
                        notificationManager.notify(1, builder.build());
                        String userToken = firebaseUser.getUid();
                        gotoVerifyEmail(fullName, email, userToken, password, username);
                    } else {
                        Toast.makeText(SignUpForm.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void gotoVerifyEmail(String fullName, String email, String userToken, String password, String username) {
        //First, save it to database
        Call<SignUp> call = newsAPPInterface.signUpAnAccount(Utils.encodeToBase64(fullName), Utils.encodeToBase64(email), Utils.encodeToBase64(userToken), Utils.encodeToBase64(username));
        assert call != null;
        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<SignUp> call, @NonNull Response<SignUp> response) {
                //Save successfully, go to verify account activity
                if (response.isSuccessful()) {
                    SignUp signUp = response.body();
                    if (signUp != null) {
                        //go to verify account activity
                        Intent intent = new Intent(SignUpForm.this, VerifyAccountForm.class);
                        intent.putExtra("userId", signUp.getUserId());
                        intent.putExtra("fullName", fullName);
                        intent.putExtra("email", email);
                        intent.putExtra("userToken", userToken);
                        intent.putExtra("password", password);
                        intent.putExtra("username", username);
                        ActivityOptions.makeSceneTransitionAnimation(SignUpForm.this).toBundle();
                        SignUpForm.this.finish();
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SignUp> call, @NonNull Throwable t) {
            }
        });
    }
}