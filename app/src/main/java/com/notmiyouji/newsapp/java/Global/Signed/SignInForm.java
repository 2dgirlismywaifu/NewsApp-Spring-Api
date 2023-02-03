package com.notmiyouji.newsapp.java.Global.Signed;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.notmiyouji.newsapp.R;
import com.notmiyouji.newsapp.java.Retrofit.NewsAPPAPI;
import com.notmiyouji.newsapp.kotlin.ApplicationFlags;
import com.notmiyouji.newsapp.kotlin.LoginedModel.CountSSO;
import com.notmiyouji.newsapp.kotlin.LoginedModel.SSO;
import com.notmiyouji.newsapp.kotlin.LoginedModel.SignIn;
import com.notmiyouji.newsapp.kotlin.RetrofitInterface.NewsAPPInterface;
import com.notmiyouji.newsapp.kotlin.SharedSettings.LoadFollowLanguageSystem;
import com.notmiyouji.newsapp.kotlin.SharedSettings.SaveUserLogined;

import retrofit2.Call;
import retrofit2.Response;

public class SignInForm extends AppCompatActivity {

    Button SignInBtn, SignUpBtn, forgotpassbtn;
    LinearLayout GoogleSSO;
    Intent intent;
    TextInputEditText account, password;
    LoadFollowLanguageSystem loadFollowLanguageSystem;
    NewsAPPInterface newsAPPInterface = NewsAPPAPI.getAPIClient().create(NewsAPPInterface.class);
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_form);
        ApplicationFlags applicationFlags = new ApplicationFlags(this);
        applicationFlags.setFlag();

        account = findViewById(R.id.username_code);
        password = findViewById(R.id.password_input);
        SignUpBtn = findViewById(R.id.SignUpBtn);
        SignUpBtn.setOnClickListener(v -> {
            intent = new Intent(this, SignUpForm.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        //Forgot Password form only access from Sign In form
        forgotpassbtn = findViewById(R.id.ForgotPasswordBtn);
        forgotpassbtn.setOnClickListener(v -> {
            intent = new Intent(this, ForgotPasswordForm.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        ImageButton backButton = findViewById(R.id.BackPressed);
        backButton.setOnClickListener(v -> {
            onBackPressed();
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            finish();
        });
        //Sign In button
        SignInBtn = findViewById(R.id.SignInBtn);
        SignInBtn.setOnClickListener(v -> {
            SignInBtn.setEnabled(false);
            SignUpBtn.setEnabled(false);
            if (account.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                account.setError("Please enter your account");
                password.setError("Please enter your password");
                SignInBtn.setEnabled(true);
                SignUpBtn.setEnabled(true);
            } else {
                SignInMethod(account.getText().toString(), password.getText().toString());
            }
        });
        //Google SSO
        GoogleSSO = findViewById(R.id.Google_signin);
        //Sign in with Google use Firebase
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Initialize sign in intent
        GoogleSignInClient googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso);
        GoogleSSO.setOnClickListener(v -> {
            // Start activity for result use ActivityResultLauncher
            intent =googleSignInClient.getSignInIntent();
            activityResultLauncher.launch(intent);
        });
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Toast.makeText(SignInForm.this, "Google Sign In failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    private void firebaseAuthWithGoogle(String idToken) {
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        mAuth.fetchSignInMethodsForEmail(user.getEmail()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                SignInMethodQueryResult result = task1.getResult();
                                if (result.getSignInMethods().isEmpty()) {
                                    String fullname = user.getDisplayName();
                                    String email = user.getEmail();
                                    String password = user.getUid();
                                    //String username = "Google SSO";
                                    String avatar = new FixBlurryGoogleImage(user.getPhotoUrl()).fixURL();
                                    //User is not registered, save to database
                                    //Always save to database with Google SSO Sign In
                                    //Google Avatar Image default so terrible, this line will fix it
                                    CheckSSOAccount(user.getDisplayName(),
                                            user.getEmail(), user.getUid(),
                                            user.getDisplayName(),
                                            avatar);

                                    Toast.makeText(SignInForm.this, R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                                    //If user login successfully, go to main activity
                                    SignInBtn.setEnabled(true);
                                    SignUpBtn.setEnabled(true);
                                    SignInForm.this.finish();
                                    //restart application
                                    Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } else {
                                    String fullname = user.getDisplayName();
                                    String email = user.getEmail();
                                    String password = user.getUid(); //also is UID
                                    //String username = "Google SSO";
                                    String avatar = new FixBlurryGoogleImage(user.getPhotoUrl()).fixURL();
                                    //Before save to shared settings, we need update user information
                                    //User is already registered, save to shared settings
                                    SaveUserLogined saveUserLogined = new SaveUserLogined(this);
                                    saveUserLogined.saveUserLogined(password, fullname, email, password, fullname, avatar,"google");
                                    Toast.makeText(SignInForm.this, R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                                    //If user login successfully, go to main activity
                                    SignInBtn.setEnabled(true);
                                    SignUpBtn.setEnabled(true);
                                    SignInForm.this.finish();
                                    //restart application
                                    Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignInForm.this, R.string.Error_login, Toast.LENGTH_SHORT).show();
                        SignInBtn.setEnabled(true);
                        SignUpBtn.setEnabled(true);
                    }
                });
    }

    private void CheckSSOAccount(String displayName, String email, String uid, String username, String url) {
        Call<CountSSO> callCountSSO = newsAPPInterface.ssoCount(email);
        callCountSSO.enqueue(new retrofit2.Callback<CountSSO>() {
            @Override
            public void onResponse(Call<CountSSO> call, Response<CountSSO> response) {
                if (response.isSuccessful()) {
                    CountSSO countSSO = response.body();
                    if (countSSO.getStatus().equals("fail")) {
                        //Save to database
                        SavedToDatabase(displayName, email, uid, username, url);
                    }
                    else {
                        //User is already registered, save to shared settings
                        SaveUserLogined saveUserLogined = new SaveUserLogined(SignInForm.this);
                        saveUserLogined.saveUserLogined(uid, displayName, email, uid, username, url,"google");
                    }
                }
            }
            @Override
            public void onFailure(Call<CountSSO> call, Throwable t) {
                //if return 500, it means that account not in database
            }
        });
    }

    private void SavedToDatabase(String fullname, String email, String password, String username, String avatar) {
        //First, save it to database
        Call<SSO> callSSO = newsAPPInterface.sso(fullname, email, username, avatar);
        callSSO.enqueue(new retrofit2.Callback<SSO>() {
            @Override
            public void onResponse(Call<SSO> call, Response<SSO> response) {
                //Save successfully,
                if (response.isSuccessful()) {
                    //Save user logined
                    SSO sso = response.body();
                    SaveUserLogined saveUserLogined = new SaveUserLogined(SignInForm.this);
                    saveUserLogined.saveUserLogined(sso.getEmail(), fullname, email, password, username, avatar,"google");
                }
            }
            @Override
            public void onFailure(Call<SSO> call, Throwable t) {
            }
        });
    }

    private void SignInMethod(String account, String password) {
        //Retrofit call signin request
        Call<SignIn> call = newsAPPInterface.signIn(account, password);
        call.enqueue(new retrofit2.Callback<SignIn>() {
            @Override
            public void onResponse(Call<SignIn> call, retrofit2.Response<SignIn> response) {
                if (response.isSuccessful()) {
                    SignIn signIn = response.body();
                    if (signIn.getStatus().equals("pass")) {
                        //Check Account Verify or not, if not go to verify page to continue
                        if (signIn.getVerify().equals("true")) {
                            //Firebase Sign In
                            mAuth = FirebaseAuth.getInstance();
                            mAuth.signInWithEmailAndPassword(signIn.getEmail(), password);
                            //Save user data to Shared Preferences
                            SaveUserLogined saveUserLogined = new SaveUserLogined(SignInForm.this);
                            saveUserLogined.saveUserLogined(signIn.getUserId(), signIn.getName(), signIn.getEmail(), password, signIn.getNickname(), signIn.getAvatar(),"login");
                            //If account verify, go to main page
                            Toast.makeText(SignInForm.this, R.string.sign_in_success, Toast.LENGTH_SHORT).show();
                            SignInBtn.setEnabled(true);
                            SignUpBtn.setEnabled(true);
                            SignInForm.this.finish();
                            //restart application
                            intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SignInForm.this, VerifyAccountForm.class);
                            intent.putExtra("email", signIn.getEmail());
                            intent.putExtra("password", password);
                            intent.putExtra("username", signIn.getNickname());
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignInForm.this).toBundle());
                            SignInBtn.setEnabled(true);
                            SignUpBtn.setEnabled(true);
                        }

                    } else if (signIn.getStatus().equals("fail")) {
                        Toast.makeText(SignInForm.this, R.string.Error_login, Toast.LENGTH_SHORT).show();
                        SignInBtn.setEnabled(true);
                        SignUpBtn.setEnabled(true);
                    }
                } else {
                    Toast.makeText(SignInForm.this, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show();
                    SignInBtn.setEnabled(true);
                    SignUpBtn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<SignIn> call, Throwable t) {
                SignInBtn.setEnabled(true);
                SignUpBtn.setEnabled(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        finish();
    }

    public void onResume() {
        super.onResume();
        loadFollowLanguageSystem = new LoadFollowLanguageSystem(this);
        loadFollowLanguageSystem.loadLanguage();
    }
}