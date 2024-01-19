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
package com.notmiyouji.newsapp.kotlin.loginsystem

import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.java.activity.userlogin.FixBlurryGoogleImage
import com.notmiyouji.newsapp.kotlin.model.user.SignIn
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppApi.apiClient
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppInterface
import com.notmiyouji.newsapp.kotlin.sharedsettings.SaveUserLogin
import com.notmiyouji.newsapp.kotlin.util.AppUtils.encodeToBase64
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleMethod(
    private val activity: AppCompatActivity,
    private val signInButton: Button,
    private val signUpButton: Button
) {
    var newsAPPInterface: NewsAppInterface = apiClient!!.create(
        NewsAppInterface::class.java
    )
    var saveUserLogin: SaveUserLogin
    private var mAuth: FirebaseAuth
    private var user: FirebaseUser?

    init {
        mAuth = FirebaseAuth.getInstance()
        user = mAuth.currentUser
        saveUserLogin = SaveUserLogin(activity)
    }

    private val gso: GoogleSignInOptions
        get() =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id)) //if this line error, ignore it
                .requestEmail()
                .build()
    val googleSignInClient: GoogleSignInClient
        get() = GoogleSignIn.getClient(activity, gso)

    fun firebaseAuthWithGoogle(
        idToken: String?, email: String,
        displayName: String?, avatarURL: Uri?
    ) {
        mAuth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    mAuth.currentUser!!
                        .updateProfile(
                            UserProfileChangeRequest.Builder().setDisplayName(displayName)
                                .setPhotoUri(avatarURL)
                                .build()
                        )
                        .addOnCompleteListener { task1: Task<Void?> ->
                            if (task1.isSuccessful) {
                                user = mAuth.currentUser
                                assert(user != null)
                                val fullName = user!!.displayName
                                val userToken = user!!.uid
                                //String username = "Google SSO";
                                //Google Avatar Image default so terrible, this line will fix it
                                val avatar = FixBlurryGoogleImage(user!!.photoUrl).fixURL()
                                //User is not registered, save to database
                                //Always save to database with Google SSO Sign In
                                savedToDatabase(fullName, email, userToken, fullName, avatar)
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(activity, R.string.Error_login, Toast.LENGTH_SHORT)
                                    .show()
                                signInButton.isEnabled = true
                                signUpButton.isEnabled = true
                            }
                        }
                }
            }
    }

    private fun savedToDatabase(
        fullName: String?,
        email: String,
        userToken: String,
        userName: String?,
        avatar: String
    ) {
        //First, save it to database
        val callSSO = newsAPPInterface.signInWithGoogle(
            encodeToBase64(fullName!!),
            encodeToBase64(email),
            encodeToBase64(userToken),
            encodeToBase64(userName!!),
            encodeToBase64(avatar)
        )!!
        callSSO.enqueue(object : Callback<SignIn?> {
            override fun onResponse(call: Call<SignIn?>, response: Response<SignIn?>) {
                //Save successfully,
                //Now sign in
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        saveUserLogin.saveUserLogin(
                            response.body()!!.userId,
                            response.body()!!.fullName, response.body()!!.email, userToken,
                            response.body()!!.nickName, response.body()!!.avatar, "login"
                        )
                        //save birthday
                        if (response.body()!!.birthday != null) {
                            saveUserLogin.saveBirthday(response.body()!!.birthday)
                        }
                        //save Gender
                        saveUserLogin.saveGender(response.body()!!.gender)
                        Toast.makeText(activity, R.string.sign_in_success, Toast.LENGTH_SHORT)
                            .show()
                        //If user login successfully, go to main activity
                        signInButton.isEnabled = true
                        signUpButton.isEnabled = true
                        activity.finish()
                        //restart application
                        val intent = activity.packageManager.getLaunchIntentForPackage(
                            activity.baseContext.packageName
                        )!!
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        activity.startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<SignIn?>, t: Throwable) {}
        })
    }
}
