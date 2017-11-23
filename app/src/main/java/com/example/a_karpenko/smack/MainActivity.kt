package com.example.a_karpenko.smack

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.ListView
import android.widget.TextView
import com.example.a_karpenko.smack.R.color.lightBlue
import com.example.a_karpenko.smack.R.drawable.main_background_shape_blue
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    //Firebase
    val TAG : String? = "Main Activity"
    var firebase : FirebaseAuth? = null
    var user : FirebaseUser? = null
    var userName : TextView? = null
    var userEmail : TextView? = null
    var snackBar : Snackbar? = null

    //Gender
    var maleGenderMy: TextView? = null
    var femaleGenderMy: TextView? = null

    var maleGenderLookingFor: CheckedTextView? = null
    var femaleGenderLookingFor: CheckedTextView? = null

    //Age
    var under18My: CheckedTextView? = null
    var from19to22My: CheckedTextView? = null
    var from23to26My: CheckedTextView? = null
    var from27to35My: CheckedTextView? = null
    var over36My: CheckedTextView? = null

    var under18LookingFor: CheckedTextView? = null
    var from19to22LookingFor: CheckedTextView? = null
    var from23to26LookingFor: CheckedTextView? = null
    var from27to35LookingFor: CheckedTextView? = null
    var over36LookingFor: CheckedTextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        firebase = FirebaseAuth.getInstance()
        user = firebase?.currentUser

        maleGenderMy = findViewById<TextView>(R.id.maleGenderMy)
        femaleGenderMy = findViewById<TextView>(R.id.femaleGenderMy)

        maleGenderLookingFor = findViewById<CheckedTextView>(R.id.maleGenderLookingFor)
        femaleGenderLookingFor = findViewById<CheckedTextView>(R.id.femaleGenderLookingFor)

        under18My = findViewById<CheckedTextView>(R.id.under18My)
        from19to22My = findViewById<CheckedTextView>(R.id.from19to22My)
        from23to26My = findViewById<CheckedTextView>(R.id.from23to26My)
        from27to35My = findViewById<CheckedTextView>(R.id.from27to35My)
        over36My = findViewById<CheckedTextView>(R.id.over36My)

        under18LookingFor = findViewById<CheckedTextView>(R.id.under18LookingFor)
        from19to22LookingFor = findViewById<CheckedTextView>(R.id.from19to22LookingFor)
        from23to26LookingFor = findViewById<CheckedTextView>(R.id.from23to26LookingFor)
        from27to35LookingFor = findViewById<CheckedTextView>(R.id.from27to35LookingFor)
        over36LookingFor = findViewById<CheckedTextView>(R.id.over36LookingFor)




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


        var startChat : Button = findViewById(R.id.startChat)
        startChat.setOnClickListener {
            startChat()
        }


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

    fun startChat() {

        startActivity(Intent(this@MainActivity, ChatActivity::class.java))
        finish()
    }


    fun myGenderMale(view: View) {
        maleGenderMy?.setBackgroundResource(R.drawable.main_background_shape_blue)
        femaleGenderMy?.setBackgroundResource(R.drawable.main_background_shape_white)
    }

    fun myGenderFemale(view: View) {
        femaleGenderMy?.setBackgroundResource(R.drawable.main_background_shape_blue)
        maleGenderMy?.setBackgroundResource(R.drawable.main_background_shape_white)
    }


}
