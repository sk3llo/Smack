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
import android.widget.Button
import android.widget.TextView
import android.widget.ListView
import com.example.a_karpenko.smack.utils.CustomTouchListener
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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

    var maleGenderLookingFor: TextView? = null
    var femaleGenderLookingFor: TextView? = null

    //Age
    var under18My: TextView? = null
    var from19to22My: TextView? = null
    var from23to26My: TextView? = null
    var from27to35My: TextView? = null
    var over36My: TextView? = null

    var under18LookingFor: TextView? = null
    var from19to22LookingFor: TextView? = null
    var from23to26LookingFor: TextView? = null
    var from27to35LookingFor: TextView? = null
    var over36LookingFor: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        //FIREBASE
        firebase = FirebaseAuth.getInstance()
        user = firebase?.currentUser
        //Main choose chat person
        maleGenderMy = findViewById<TextView>(R.id.maleGenderMy)
        femaleGenderMy = findViewById<TextView>(R.id.femaleGenderMy)

        maleGenderLookingFor = findViewById<TextView>(R.id.maleGenderLookingFor)
        femaleGenderLookingFor = findViewById<TextView>(R.id.femaleGenderLookingFor)

        under18My = findViewById<TextView>(R.id.under18My)
        from19to22My = findViewById<TextView>(R.id.from19to22My)
        from23to26My = findViewById<TextView>(R.id.from23to26My)
        from27to35My = findViewById<TextView>(R.id.from27to35My)
        over36My = findViewById<TextView>(R.id.over36My)

        under18LookingFor = findViewById<TextView>(R.id.under18LookingFor)
        from19to22LookingFor = findViewById<TextView>(R.id.from19to22LookingFor)
        from23to26LookingFor = findViewById<TextView>(R.id.from23to26LookingFor)
        from27to35LookingFor = findViewById<TextView>(R.id.from27to35LookingFor)
        over36LookingFor = findViewById<TextView>(R.id.over36LookingFor)



        //Check if user logged in
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

        //Start searching for chat person
        var startChat : Button = findViewById(R.id.startChat)
        startChat.setOnClickListener {
            startChat()
        }

        //Click listeners
        under18My?.setOnTouchListener(object: CustomTouchListener(){})
        from19to22My?.setOnTouchListener(object: CustomTouchListener(){})
        from23to26My?.setOnTouchListener(object: CustomTouchListener(){})
        from27to35My?.setOnTouchListener(object: CustomTouchListener(){})
        over36My?.setOnTouchListener(object: CustomTouchListener(){})



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
