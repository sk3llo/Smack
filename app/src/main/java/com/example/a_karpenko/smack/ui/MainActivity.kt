package com.example.a_karpenko.smack.ui

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
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.core.OptionsChecker
import com.example.a_karpenko.smack.core.addData.AddOptionsFirestore
import com.example.a_karpenko.smack.utils.chooser_options.LookingForAgeChooser
import com.example.a_karpenko.smack.utils.chooser_options.GenderChooser
import com.example.a_karpenko.smack.utils.chooser_options.MyAgeChooser
import com.example.a_karpenko.smack.utils.RealmUtil
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.realm.Realm

class MainActivity : AppCompatActivity() {

    //Firebase
    val TAG: String? = "Main Activity"
    var firebase: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var userName: TextView? = null
    var userEmail: TextView? = null
    var snackBar: Snackbar? = null

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

    //Realm
    var realm : Realm? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        //FIREBASE
        firebase = FirebaseAuth.getInstance()
        user = firebase?.currentUser

        //Realm
        Realm.init(this)
        realm = Realm.getDefaultInstance()


        //Main choose chat person vars
        maleGenderMy = findViewById(R.id.maleGenderMy)
        femaleGenderMy = findViewById(R.id.femaleGenderMy)

        maleGenderLookingFor = findViewById(R.id.maleGenderLookingFor)
        femaleGenderLookingFor = findViewById(R.id.femaleGenderLookingFor)

        under18My = findViewById(R.id.under18My)
        from19to22My = findViewById(R.id.from19to22My)
        from23to26My = findViewById(R.id.from23to26My)
        from27to35My = findViewById(R.id.from27to35My)
        over36My = findViewById(R.id.over36My)

        under18LookingFor = findViewById(R.id.under18LookingFor)
        from19to22LookingFor = findViewById(R.id.from19to22LookingFor)
        from23to26LookingFor = findViewById(R.id.from23to26LookingFor)
        from27to35LookingFor = findViewById(R.id.from27to35LookingFor)
        over36LookingFor = findViewById(R.id.over36LookingFor)


        //Check if user logged in
        if (user == null) {
            startActivity(Intent(this@MainActivity, Login::class.java))
            finish()
        } else {
            rememberChoice()
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

            //Click listeners
        //Age Looking for
        under18LookingFor?.setOnTouchListener(LookingForAgeChooser())
        from19to22LookingFor?.setOnTouchListener(LookingForAgeChooser())
        from23to26LookingFor?.setOnTouchListener(LookingForAgeChooser())
        from27to35LookingFor?.setOnTouchListener(LookingForAgeChooser())
        over36LookingFor?.setOnTouchListener(LookingForAgeChooser())
        //MyAge
        under18My?.setOnClickListener(MyAgeChooser())
        from19to22My?.setOnClickListener(MyAgeChooser())
        from23to26My?.setOnClickListener(MyAgeChooser())
        from27to35My?.setOnClickListener(MyAgeChooser())
        over36My?.setOnClickListener(MyAgeChooser())
        //Choose gender
        maleGenderMy?.setOnClickListener (GenderChooser())
        femaleGenderMy?.setOnClickListener (GenderChooser())
        maleGenderLookingFor?.setOnClickListener (GenderChooser())
        femaleGenderLookingFor?.setOnClickListener(GenderChooser())



        //Start searching for chat person
        val startChat: Button = findViewById(R.id.startChat)
        startChat.setOnClickListener {
            startChat()
        }

    }

    fun logoutButtonOnClicked(view: View) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener { task ->
                    // user is now signed out
                    if (task.isSuccessful) {
                        startActivity(Intent(this@MainActivity, Login::class.java))
                        finish()
                    } else {
                        snackBar = Snackbar.make(CoordinatorLayout(this), "Logout failed", Snackbar.LENGTH_SHORT)
                        snackBar?.show()
                    }
                }
    }

//    fun addChannelBtnClicked(view: View): ListView {
//        val channelList = findViewById<ListView>(R.id.channel_list)
//        channelList.addHeaderView(view)
//        return channelList
//    }

    fun startChat() {
        //check all optionsMy if empty
        if (OptionsChecker(findViewById(android.R.id.content)).checkAndSend()) {
            AddOptionsFirestore().addChosenOptions()
            AddOptionsFirestore().waitingOn()
            startActivity(Intent(this@MainActivity, WaitingActivity::class.java))
            finish()
        } else{
            return
        }
    }



    //On Start
    //Remember chosen option and set color to it
    fun rememberChoice() {

        try {
            // Age Looking For change color based on chosen option
            if (RealmUtil().under18LookingFor() == 1) {
                under18LookingFor?.setBackgroundResource(R.drawable.main_background_shape_blue)
            }

            if (RealmUtil().from19to22LookingFor() == 1) {
                from19to22LookingFor?.setBackgroundResource(R.drawable.main_background_shape_blue)
            }
            if (RealmUtil().from23to26LookingFor() == 1) {
                from23to26LookingFor?.setBackgroundResource(R.drawable.main_background_shape_blue)
            }

            if (RealmUtil().from27to35LookingFor() == 1) {
                from27to35LookingFor?.setBackgroundResource(R.drawable.main_background_shape_blue)
            }

            if (RealmUtil().over36LookingFor() == 1) {
                over36LookingFor?.setBackgroundResource(R.drawable.main_background_shape_blue)
            }

            // My Age change color based on chosen option

            if (RealmUtil().under18My() == 1) {
                under18My?.setBackgroundResource(R.drawable.main_background_shape_blue)
            }

            if (RealmUtil().from19to22My() == 1) {
                from19to22My?.setBackgroundResource(R.drawable.main_background_shape_blue)
            }
            if (RealmUtil().from23to26My() == 1) {
                from23to26My?.setBackgroundResource(R.drawable.main_background_shape_blue)
            }
            if (RealmUtil().from27to35My() == 1) {
                from27to35My?.setBackgroundResource(R.drawable.main_background_shape_blue)
            }
            if (RealmUtil().over36My() == 1) {
                over36My?.setBackgroundResource(R.drawable.main_background_shape_blue)
            }

            //My gender change color based on chosen option

            if (RealmUtil().maleGenderMy() == 1){
                maleGenderMy?.setBackgroundResource(R.drawable.main_background_shape_blue)
                Log.d("maleGenderMy = ", "${RealmUtil().maleGenderMy()}")
            }
            if (RealmUtil().femaleGenderMy() == 1){
                femaleGenderMy?.setBackgroundResource(R.drawable.main_background_shape_blue)
                Log.d("femaleGenderMy = ", "${RealmUtil().femaleGenderMy()}")
            }
            if (RealmUtil().maleGenderLookingFor() == 1){
                maleGenderLookingFor?.setBackgroundResource(R.drawable.main_background_shape_blue)
                Log.d("maleGenderLF = ", "${RealmUtil().maleGenderLookingFor()}")
            }
            if (RealmUtil().femaleGenderLookingFor() == 1){
                femaleGenderLookingFor?.setBackgroundResource(R.drawable.main_background_shape_blue)
                Log.d("femaleGenderLF = ", "${RealmUtil().femaleGenderLookingFor()}")
            }

        } finally {
            return
        }

    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            AddOptionsFirestore().waitingOff()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
//        AddOptionsFirestore().waitingOn()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (realm != null){
            realm?.close()
        }
    }

}