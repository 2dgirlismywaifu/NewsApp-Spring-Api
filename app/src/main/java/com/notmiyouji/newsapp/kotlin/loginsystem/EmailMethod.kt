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

import android.app.ActivityOptions
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.java.activity.userlogin.VerifyAccountForm
import com.notmiyouji.newsapp.kotlin.model.user.SignIn
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppApi.apiClient
import com.notmiyouji.newsapp.kotlin.retrofit.NewsAppInterface
import com.notmiyouji.newsapp.kotlin.sharedsettings.SaveUserLogin
import com.notmiyouji.newsapp.kotlin.util.AppUtils.encodeToBase64
import com.notmiyouji.newsapp.kotlin.util.GravatarImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailMethod(
    private val activity: AppCompatActivity,
    private val signInButton: Button,
    private val signUpButton: Button
) {
    var newsAPPInterface: NewsAppInterface = apiClient!!.create(NewsAppInterface::class.java)
    private var mAuth: FirebaseAuth
    private var intent: Intent? = null

    init {
        mAuth = FirebaseAuth.getInstance()
    }

    private fun encodeData(data: String): String {
        return encodeToBase64(data)
    }

    fun signInMethod(account: String, password: String) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(account, password)
            .addOnCompleteListener(activity) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    //Retrofit call sign in request
                    val userToken = mAuth.currentUser?.uid
                    if (userToken != null) {
                        verifyWithDatabase(account, password, userToken)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(activity, R.string.Error_login, Toast.LENGTH_SHORT).show()
                    signInButton.isEnabled = true
                    signUpButton.isEnabled = true
                }
            }
    }

    private fun verifyWithDatabase(account: String, password: String, userToken: String) {
        val call = newsAPPInterface.signIn(encodeToBase64(account), encodeToBase64(userToken))!!
        call.enqueue(object : Callback<SignIn?> {
            override fun onResponse(call: Call<SignIn?>, response: Response<SignIn?>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val signIn = response.body()
                        if (signIn!!.status == "success") {
                            //Check Account Verify or not, if not go to verify page to continue
                            if (signIn.verify == "true") {
                                //Save user data to Shared Preferences
                                val avatar: String? = if (signIn.avatar != null) {
                                    if (signIn.avatar!!.isEmpty()) {
                                        val gravatarImage = signIn.email?.let { GravatarImage(it) }
                                        gravatarImage?.gravatarURL
                                    } else {
                                        signIn.avatar
                                    }
                                } else {
                                    val gravatarImage = signIn.email?.let { GravatarImage(it) }
                                    gravatarImage?.gravatarURL
                                }
                                val saveUserLogin = SaveUserLogin(activity)
                                saveUserLogin.saveUserLogin(
                                    signIn.userId,
                                    signIn.fullName,
                                    signIn.email,
                                    encodeData(password),
                                    signIn.nickName,
                                    avatar,
                                    "login"
                                )
                                saveUserLogin.saveBirthday(signIn.birthday)
                                saveUserLogin.saveGender(signIn.gender)
                                //If account verify, go to main page
                                Toast.makeText(
                                    activity,
                                    R.string.sign_in_success,
                                    Toast.LENGTH_SHORT
                                ).show()
                                signInButton.isEnabled = true
                                signUpButton.isEnabled = true
                                activity.finish()
                                //restart application
                                intent =
                                    activity.baseContext.packageManager.getLaunchIntentForPackage(
                                        activity.baseContext.packageName
                                    )
                                assert(intent != null)
                                intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                activity.startActivity(intent)
                            } else {
                                val verifyForm = Intent(activity, VerifyAccountForm::class.java)
                                verifyForm.putExtra("email", signIn.email)
                                verifyForm.putExtra("password", password)
                                verifyForm.putExtra("username", signIn.nickName)
                                activity.startActivity(
                                    verifyForm, ActivityOptions.makeSceneTransitionAnimation(
                                        activity
                                    ).toBundle()
                                )
                                signInButton.isEnabled = true
                                signUpButton.isEnabled = true
                            }
                        } else if (signIn.status == "fail") {
                            Toast.makeText(activity, R.string.Error_login, Toast.LENGTH_SHORT)
                                .show()
                            signInButton.isEnabled = true
                            signUpButton.isEnabled = true
                        }
                    }
                } else {
                    Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT)
                        .show()
                    signInButton.isEnabled = true
                    signUpButton.isEnabled = true
                }
            }

            override fun onFailure(call: Call<SignIn?>, t: Throwable) {
                Toast.makeText(activity, R.string.Some_things_went_wrong, Toast.LENGTH_SHORT).show()
                signInButton.isEnabled = true
                signUpButton.isEnabled = true
            }
        })
    }
}
