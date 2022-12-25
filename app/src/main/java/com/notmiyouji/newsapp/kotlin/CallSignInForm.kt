package com.notmiyouji.newsapp.kotlin

import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.view.View
import com.notmiyouji.newsapp.R
import android.content.Intent
import com.notmiyouji.newsapp.java.global.SignInForm
import android.app.ActivityOptions

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