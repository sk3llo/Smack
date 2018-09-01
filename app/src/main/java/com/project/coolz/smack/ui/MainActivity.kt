package com.project.coolz.smack.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.project.coolz.smack.R
import com.project.coolz.smack.core.addData.AddOptionsFirestore
import com.project.coolz.smack.core.queryData.MyOptionsChecker
import com.project.coolz.smack.models.firestore.LoginCheckerModel
import com.project.coolz.smack.models.firestore.OnlineChecker
import com.project.coolz.smack.models.firestore.PresenceModel
import com.project.coolz.smack.utils.ConnectionChangeUtil
import com.project.coolz.smack.utils.RealmUtil
import com.project.coolz.smack.utils.chooser_options.GenderChooser
import com.project.coolz.smack.utils.chooser_options.LookingForAgeChooser
import com.project.coolz.smack.utils.chooser_options.MyAgeChooser
import io.realm.Realm
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*


class MainActivity : AppCompatActivity() {

    var context: Context? = null

    //Firebase
    var auth: FirebaseAuth? = null
    var myId: String? = null
    var user: FirebaseUser? = null
    var db: FirebaseFirestore? = null

    //Connecting manager
    var cm: ConnectivityManager? = null
    var ni: NetworkInfo? = null

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

    var currentDate: Date? = null
    //Start
    var star: Button? = null
    //Count online users
    var online: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        //Use to "close the wall" when the user is found
        RealmUtil().addIsUserFound(true)

        //My ID
        myId = RealmUtil().retrieveMyId()

        //FIREBASE
        auth = FirebaseAuth.getInstance()
        user = auth?.currentUser
        db = FirebaseFirestore.getInstance()
        val settings: FirebaseFirestoreSettings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
        db?.firestoreSettings = settings

        //Add Online status to DB
        db?.collection("Online")?.document(myId!!)?.set(OnlineChecker())

        //Realm
        Realm.init(this)
        realm = Realm.getDefaultInstance()

        //Connection
        cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        ni = cm?.activeNetworkInfo

        //Main choose chat person vars
        maleGenderMy = findViewById(R.id.maleGenderMy)
        femaleGenderMy = findViewById(R.id.femaleGenderMy)

        online = find(R.id.online)

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

        currentDate = Calendar.getInstance().time
        star = findViewById(R.id.mainStar)

        //Go to saved chats
        star?.onClick {
            startActivity(Intent(this@MainActivity, SavedChats::class.java))
            finish()
        }

        //Check if user logged in
        if (user == null) {
            startActivity(Intent(this@MainActivity, SplashActivity::class.java))
            finish()
        } else {
            rememberChoice()
        }

            //Click listeners
        //Age Looking for
        under18LookingFor?.setOnClickListener(LookingForAgeChooser())
        from19to22LookingFor?.setOnClickListener(LookingForAgeChooser())
        from23to26LookingFor?.setOnClickListener(LookingForAgeChooser())
        from27to35LookingFor?.setOnClickListener(LookingForAgeChooser())
        over36LookingFor?.setOnClickListener(LookingForAgeChooser())
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
            //Check Internet connection
            val isWifi: Boolean? = ni?.type == ConnectivityManager.TYPE_WIFI
            val isMobile: Boolean? = ni?.type == ConnectivityManager.TYPE_MOBILE
            if (ni != null && ni?.isConnectedOrConnecting!! || isWifi!! || isMobile!!) {
                startChat()
            } else {
                Snackbar.make(findViewById<View>(android.R.id.content), "Please, check your Internet connection", Snackbar.LENGTH_SHORT).show()
            }
        }

        //check online users
        Handler().postDelayed({
            onlineChecker()
        }, 1500)

    }


    fun onlineChecker() {
        if (db?.collection("Online")?.get()?.exception == null) {
            db?.collection("Online")?.get()?.addOnSuccessListener {
                online?.text = "Online: ${it?.size()}"
            }
            db?.collection("Online")?.addSnapshotListener { snapshot, exception ->
                online?.text = "Online: ${snapshot?.size()}"
            }
        }
    }



    fun startChat() {
        //check all optionsMy: false if empty, true if not
        if (MyOptionsChecker(findViewById(android.R.id.content)).checkAndSend()) {
            //Add doc to Firestore with "Online" Action
            db?.collection("WL")?.document("$myId")
                    ?.set(LoginCheckerModel(true, currentDate))?.addOnCompleteListener {
                Log.d("Main Activity***** ", "ADDED TRUE TO WL, USER: $myId")
            }
            //Start Waiting Activity
            startActivity(Intent(this@MainActivity, WaitingActivity::class.java))
            finish()
            //Check and add me to db and my options to Firestore
            AddOptionsFirestore().addChosenOptions()
            //Start searching for chat
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
        if (!WaitingActivity().isDestroyed){
            WaitingActivity().finish()
//         System.exit(0)
        }
        //Delete online presence
        if (db?.collection("Online")?.document("$myId")?.get()?.exception == null){
            db?.collection("Online")?.document("$myId")?.delete()
        }
        finish()
}

    override fun onStart() {
        super.onStart()
        //Stop Searching for new chat
        doAsync {
            db?.collection("Users")?.document(myId.toString())
                    ?.collection("presence")?.document("my")
                    ?.set(PresenceModel("0"))
            db?.collection("WL")?.document(myId.toString())?.delete()
        }
        //Register Broadcast receiver
        this.applicationContext.registerReceiver(ConnectionChangeUtil(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (realm != null){
            realm?.close()
        }
        //Delete online presence
//        if (db?.collection("Online")?.document("$myId") != null){
//            db?.collection("Online")?.document("$myId")?.delete()
//        }
    }

}