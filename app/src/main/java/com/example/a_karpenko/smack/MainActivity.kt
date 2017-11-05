package com.example.a_karpenko.smack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.annotation.NonNull
import android.support.annotation.StringRes
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.ui.ExtraConstants.EXTRA_IDP_RESPONSE
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    //Firebase
    val TAG : String? = "Main Activity"
    var firebase : FirebaseAuth? = null
    var user : FirebaseUser? = null
    var userName : TextView? = null
    var userEmail : TextView? = null
    var snackBar : Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebase = FirebaseAuth.getInstance()
        user = firebase?.currentUser

        if (user == null){
            startActivity(Intent(this@MainActivity, App::class.java))
            finish()
        }

        //Change Nav Header text to user name/email
        val navView = findViewById<NavigationView>(R.id.nav_header_main)
        val header = navView.getHeaderView(0)
        userName = header.findViewById(R.id.userNameNavHeader)
        userEmail = header.findViewById(R.id.userEmailNavHeader)
        userName?.text = if (TextUtils.isEmpty(user?.displayName.toString())) "No email" else user?.displayName.toString()
        userEmail?.text = if (TextUtils.isEmpty(user?.email.toString())) "No email" else user?.email.toString()


        //Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Drawer
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }

    fun logoutButtonOnClicked(view: View) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener { task ->
                    // user is now signed out
                    if (task.isSuccessful){
                        startActivity(Intent(this@MainActivity, App::class.java))
                        finish()
                    } else {
                        snackBar = Snackbar.make(CoordinatorLayout(this), "Logout failed", Snackbar.LENGTH_SHORT)
                        snackBar?.show()
                    }
                }
    }

    fun addChannelBtnClicked(view: View): ListView {
        val channelList = findViewById<ListView>(R.id.channel_list)
        channelList.addHeaderView(view)
        return channelList
    }


}
