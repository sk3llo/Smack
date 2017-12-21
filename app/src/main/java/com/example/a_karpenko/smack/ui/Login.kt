package com.example.a_karpenko.smack.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.example.a_karpenko.smack.R
import com.google.firebase.auth.FirebaseAuth


class Login : AppCompatActivity() {

    var SPLASH_TIME_OUT: Int? = 2000
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        Handler().postDelayed({
            if (user == null) {
                FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener {
                    startActivity(Intent(this@Login, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
            } else {
                startActivity(Intent(this@Login, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }, SPLASH_TIME_OUT?.toLong()!!)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

