package com.example.a_karpenko.smack.core.queryData

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.a_karpenko.smack.ui.ChatActivity
import com.example.a_karpenko.smack.ui.WaitingActivity
import com.example.a_karpenko.smack.utils.RealmUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

//Main class for querying Firestore and find suitable person for chat
class WaitingListQuery(context: Context, activity: WaitingActivity) {

    //Need to finish WaitingActivity
    var activity: WaitingActivity? = null
    var context: Context? = null
    init {
        this.context = context
        this.activity = activity
    }

    //Current user gender and age
    //Taken from Realm
    var MeMaleGenderMy = RealmUtil().maleGenderMy()
    var MeFemaleGenderMy = RealmUtil().femaleGenderMy()

    var MeMaleGenderLF = RealmUtil().maleGenderLookingFor()
    var MeFemaleGenderLF = RealmUtil().femaleGenderLookingFor()

    //Age
    var MeUnder18My = RealmUtil().under18My()
    var MeFrom19to22My = RealmUtil().from19to22My()
    var MeFrom23to26My = RealmUtil().from23to26My()
    var MeFrom27to35My = RealmUtil().from27to35My()
    var MeOver36My = RealmUtil().over36My()

    var MeUnder18LF = RealmUtil().under18LookingFor()
    var MeFrom19to22LF = RealmUtil().from19to22LookingFor()
    var MeFrom23to26LF = RealmUtil().from23to26LookingFor()
    var MeFrom27to35LF = RealmUtil().from27to35LookingFor()
    var MeOver36LF = RealmUtil().over36LookingFor()


    private val currentDate = Calendar.getInstance().time
    private val uidMy = FirebaseAuth.getInstance().currentUser?.uid

    //Find user to chat
    private val users = FirebaseFirestore.getInstance()
            .collection("Users")

    // My waiting list
    private val wl = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")

    //My options
    private val optionsMyF = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")
            .collection("options").document("optionsMy")

    //Options I'm looking for
    private val optionsLF = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")
            .collection("options").document("optionsLF")


    //Check if all users on waiting list
    fun checkWL() {
        users.get().addOnCompleteListener { snap ->
            snap.result.forEach { forEach ->
                val searchUsers = forEach.data["waitingListOn"]
                //Hueva, nada listener
                when {
                    forEach.reference.id == uidMy -> return@forEach
                    searchUsers == false -> {
                        checkWL()
                        Log.d("WaitingListQuery_-*-_ ", "CHECKED: ID ${forEach.reference.id} == $searchUsers")
                    }
                    else -> {
                        checkOptions(forEach.reference)
                        Log.d("WaitingListQuery_-*-_ ", "CHECKED: ID ${forEach.reference.id} == $searchUsers")
                    }
                }
            }
        }
    }

//    Check options for user who's true on waiting list
    fun checkOptions(foundUser: DocumentReference) {
        foundUser.collection("options").document("optionsMy").get()
                .addOnCompleteListener { optMy ->
                    foundUser.collection("options").document("optionsLF").get()
                            .addOnCompleteListener { optLF ->

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

                                //Start checking options here
                                //If I'm male 
                                if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeFemaleGenderLF == 1 && MeUnder18LF == 1
                                        && femaleGenderMy.toString() == "1" && under18My.toString() == "1"
                                        && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                    checkOut()

                                }  else if (MeMaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
                                        && maleGenderMy.toString() == "1" && under18My.toString() == "1"
                                        && maleGenderLF.toString() == "1" && under18LF.toString() == "1") {
                                    checkOut()
                                }

                                //Fem check
                                //If I'm female under18 and LF male under18
//                                else if (MeFemaleGenderMy == 1 && MeUnder18My == 1 && MeMaleGenderLF == 1 && MeUnder18LF == 1
//                                        && maleGenderMy.toString() == "1" && under18My.toString() == "1"
//                                        && femaleGenderLF.toString() == "1" && under18LF.toString() == "1") {
//                                    checkOut()
//                                    //ME MALE LF MALE UNDER18
//                                }


                        }
                }
}

    //Start Chat activity
    fun checkOut() {
        val intent = Intent(context, ChatActivity::class.java)
        context?.startActivity(intent)
        activity?.finish()
    }

}