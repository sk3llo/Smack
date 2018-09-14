package com.project.coolz.smack.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.project.coolz.smack.R
import com.project.coolz.smack.models.chat.MyUniqueId
import com.project.coolz.smack.utils.IfAppIsKilledService
import com.project.coolz.smack.utils.RealmUtil
import io.realm.Realm
import org.jetbrains.anko.find


class SplashActivity : AppCompatActivity() {

    var SPLASH_TIME_OUT: Int? = 2000
    val user = FirebaseAuth.getInstance().currentUser
    var splashImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.splash_activity)

        splashImage = find(R.id.splashImage)
        checkTheme()

        //Sign in anonymously or if signed in log in
        Handler().postDelayed({
            if (user == null) {
                FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener {
                    //Check if user's ID is in DB
                    if (Realm.getDefaultInstance()?.where(MyUniqueId::class.java)?.findAll()?.count() == 0){
                        RealmUtil().saveMyId(myId())
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    } else {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()
                    }
                }
            } else {
                //Check if user's ID is in DB
                if (Realm.getDefaultInstance()?.where(MyUniqueId::class.java)?.findAll()?.count() == 0){
                    RealmUtil().saveMyId(myId())
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
            }
        }, SPLASH_TIME_OUT?.toLong()!!)

        //Start checking if the app is being killed
        startService(Intent(baseContext, IfAppIsKilledService().javaClass))

    }

    //Check what theme user has and update UI accordingly
    fun checkTheme() = when (RealmUtil().getStyle()){
        1 -> splashImage?.setImageDrawable(ContextCompat.getDrawable(this@SplashActivity, R.drawable.splash_purple))
        2 -> splashImage?.setImageDrawable(ContextCompat.getDrawable(this@SplashActivity, R.drawable.splash_blue))
        else -> {splashImage?.setImageDrawable(ContextCompat.getDrawable(this@SplashActivity, R.drawable.splash_green))}
    }

    //Pseudo anonymous ID
    fun myId():String {
        return "35" + //we make this look like a valid IMEI
                Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                Build.USER.length % 10 + Build.DEVICE.length % 10 +
                Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
                Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
                Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                Build.USER.length % 10
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

