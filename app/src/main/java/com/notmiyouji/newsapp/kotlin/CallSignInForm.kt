package com.notmiyouji.newsapp.kotlin

import android.app.ActivityOptions
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.notmiyouji.newsapp.R
import com.notmiyouji.newsapp.java.global.SignInForm

class CallSignInForm(var navigationView: NavigationView, var activity: AppCompatActivity) {

    fun callSignInForm() {
        val headerView = navigationView.getHeaderView(0)
        val loginButton: Button = headerView.findViewById(R.id.SignInHeader)
        loginButton.setOnClickListener {
            val intent = Intent(activity, SignInForm::class.java)
            activity.startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
            )
        }
    }
}