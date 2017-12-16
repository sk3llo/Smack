package com.example.a_karpenko.smack.core.queryData

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Handler
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.widget.Toast
import com.example.a_karpenko.smack.core.EditTextWatcher
import com.example.a_karpenko.smack.models.age_looking_for.Under18
import com.example.a_karpenko.smack.ui.ChatActivity
import com.example.a_karpenko.smack.ui.MainActivity
import com.example.a_karpenko.smack.ui.WaitingActivity
import com.example.a_karpenko.smack.utils.RealmUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.properties.Delegates

//                  Main class for querying Firestore and find suitable person for chat

open class WaitingListQuery(context: Context, activity: WaitingActivity) {

    //Need to finish WaitingActivity
    var activity: WaitingActivity? = null
    var context: Context? = null
    init {
        this.context = context
        this.activity = activity
    }

    //Current user gender and age
    //Taken from Realm
    private var MeMaleGenderMy = RealmUtil().maleGenderMy()
    private var MeFemaleGenderMy = RealmUtil().femaleGenderMy()

    private var MeMaleGenderLF = RealmUtil().maleGenderLookingFor()
    private var MeFemaleGenderLF = RealmUtil().femaleGenderLookingFor()

    //Age
    private var MeUnder18My = RealmUtil().under18My()
    private var MeFrom19to22My = RealmUtil().from19to22My()
    private var MeFrom23to26My = RealmUtil().from23to26My()
    private var MeFrom27to35My = RealmUtil().from27to35My()
    private var MeOver36My = RealmUtil().over36My()

    private var MeUnder18LF = RealmUtil().under18LookingFor()
    private var MeFrom19to22LF = RealmUtil().from19to22LookingFor()
    private var MeFrom23to26LF = RealmUtil().from23to26LookingFor()
    private var MeFrom27to35LF = RealmUtil().from27to35LookingFor()
    private var MeOver36LF = RealmUtil().over36LookingFor()


    private val currentDate = Calendar.getInstance().time
    private val uidMy = FirebaseAuth.getInstance().currentUser?.uid

    //Find user to chat

    //WL
    private val WL = FirebaseFirestore.getInstance().collection("WL")



    //Check if all users on waiting list
    fun checkWL() {
        AsyncTask.execute {
        WL.get().addOnCompleteListener { snap ->
            snap.result.documents.all { all ->
                val searchUsers = all["waitingListOn"]
                val ref = all.reference
                when {
                    ref.id == uidMy -> { Log.d("WaitingListQuery_-*-_ ", "MY ID : ${ref.id} retry: ${RealmUtil().retrySearchForChat()!!}") }
                    searchUsers == true && ref.id != uidMy -> {
                        checkOptions(ref.id)
                        Log.d("WaitingListQuery_-*-_ ", "CHECKED ONLINE USERS: ${ref.id} retry: ${RealmUtil().retrySearchForChat()!!}")
                    }
                    searchUsers == false -> {
                        Log.d("WaitingListQuery_-*-_ ", "CHECKED OFFLINE USERS: ${ref.id} retry: ${RealmUtil().retrySearchForChat()!!}")
                    }
                    else -> {
                    }
                }
                return@all false
            }
        }
    }
}


//    Check options for user who's true on waiting list
    fun checkOptions(foundUser: String) {

    //Network
    val cm: ConnectivityManager? = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val ni: NetworkInfo? = cm?.activeNetworkInfo
    val isWifi: Boolean? = ni?.type == ConnectivityManager.TYPE_WIFI
    val isMobile: Boolean? = ni?.type == ConnectivityManager.TYPE_MOBILE

    val optionsLF = FirebaseFirestore.getInstance()
            .collection("Users").document("$foundUser")
            .collection("options").document("optionsLF")

    val optionsMY = FirebaseFirestore.getInstance()
            .collection("Users").document("$foundUser")
            .collection("options").document("optionsMy")

        optionsMY.get().addOnCompleteListener { optMy ->
                        optionsLF.get().addOnCompleteListener { optLF ->
                            if (ni != null && ni?.isConnectedOrConnecting!! && isWifi!! || isMobile!!) {
                                if (optMy.result.exists() && optLF.result.exists()) {

                                    //Found user's refs
                                    val maleGenderMy = optMy.result["maleGenderMy"]
                                    val femaleGenderMy = optMy.result["femaleGenderMy"]
                                    val under18My = optMy.result["under18My"]
                                    val from19to22My = optMy.result["from19to22My"]
                                    val from23to26My = optMy.result["from23to26My"]
                                    val from27to35My = optMy.result["from27to35My"]
                                    val over36My = optMy.result["over36My"]

                                    //LF refs
                                    val maleGenderLF = optLF.result["maleGenderLookingFor"]
                                    val femaleGenderLF = optLF.result["femaleGenderLookingFor"]
                                    val under18LF = optLF.result["under18LookingFor"]
                                    val from19to22LF = optLF.result["from19to22LookingFor"]
                                    val from23to26LF = optLF.result["from23to26LookingFor"]
                                    val from27to35LF = optLF.result["from27to35LookingFor"]
                                    val over36LF = optLF.result["over36LookingFor"]

                                    //                                            ***Start checking options here***

                                    //                      If I'm male LF MALE
                                    //Weird, but LF male <18
                                    if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26LF == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 19 to 22 male LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 23 to 26 male LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 27 to 35 male LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //Over 36 male LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                    }

                                    //ME MALE LF FEM check
                                    //LF Fem <18
                                    else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26LF == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 19 to 22 female LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 23 to 26 female LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 27 to 35 female LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //Over 36 female LF
                                    } else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeMaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                    }

                                    //                  ***ME FEMALE LF MALE
                                    //LF male <18
                                    else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26LF == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                            && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 19 to 22 male LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && maleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 23 to 26 male LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && maleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 27 to 35 male LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && maleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //Over 36 male LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeMaleGenderLF == 1 && MeOver36LF == 1
                                            && maleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                    }

                                    //                   *** ME FEMALE LF MALE check ***
                                    //LF Fem <18
                                    else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26LF == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                            && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 19 to 22 female LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom19to22LF == 1
                                            && femaleGenderMy.toString() == "1" && from19to22My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 23 to 26 female LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom23to26LF == 1
                                            && femaleGenderMy.toString() == "1" && from23to26My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //From 27 to 35 female LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeFrom27to35LF == 1
                                            && femaleGenderMy.toString() == "1" && from27to35My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                        //Over 36 female LF
                                    } else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom19to22My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from19to22LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom23to26My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from23to26LF.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeFrom27to35My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && from27to35My.toString() == "1") {
                                        checkOut(foundUser)
                                    } else if (MeFemaleGenderMy == 1 && MeOver36My == 1 && MeFemaleGenderLF == 1 && MeOver36LF == 1
                                            && femaleGenderMy.toString() == "1" && over36My.toString() == "1"
                                            && femaleGenderLF.toString() == "1" && over36LF.toString() == "1") {
                                        checkOut(foundUser)
                                    }
                                }

                                //Check if user was found
//                                    else {
//                                        checkRetry()
//                                    }

                            } else {
                                activity?.startActivity(Intent(activity?.context, MainActivity::class.java))
                                Toast.makeText(activity?.context, "Please, check your Internet connection", Toast.LENGTH_SHORT).show()
                                activity?.finish()
                            }
                        }
                }
}

    //Check if found a user to chat or if stopped searching
//    private fun checkRetry(){
//        if (RealmUtil().retrySearchForChat()!!){
//            checkWListener()
//        }
//    }


    //Start Chat activity
    private fun checkOut(foundUser: String) {
        //Stop searching (took from Realm)
        RealmUtil().retrySearch(false)
        RealmUtil().addFounduserUid(foundUser)
        //Start chat activity
        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("foundUser", foundUser)
        context?.startActivity(intent)
        activity?.finish()
    }
}