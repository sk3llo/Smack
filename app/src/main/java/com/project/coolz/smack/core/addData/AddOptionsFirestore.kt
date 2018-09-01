package com.project.coolz.smack.core.addData

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.project.coolz.smack.models.firestore.OptionsLFModel
import com.project.coolz.smack.models.firestore.OptionsMyModel
import com.project.coolz.smack.utils.RealmUtil
import java.util.*

class AddOptionsFirestore {

    val TAG = "Firestore Util"

    val currentDate: Date? = Calendar.getInstance().time
    val uidMy = RealmUtil().retrieveMyId()

    //Firestore refs
    val wl = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")

    val optionsMy = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")
            .collection("options").document("optionsMy")

    val optionsLF = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")
            .collection("options").document("optionsLF")


//    //Log in
//    fun waitingOn(): Task<Void> {
//        return wl.set(LoginCheckerModel(true, currentDate)).addOnCompleteListener { complete ->
//            if (complete.isSuccessful) {
//                Log.d("App", "Entering db successful")
//            } else {
//                Log.d("App", "Failed to enter db")
//            }
//        }
//    }
//
//    //Log out
//    fun waitingOff(): Task<Void> {
//        return wl.set(LoginCheckerModel(false, currentDate)).addOnCompleteListener { complete ->
//            if (complete.isSuccessful) {
//                Log.d(TAG, "Exit db successful")
//            } else {
//                Log.d(TAG, "Failed to exit db")
//            }
//        }
//    }
    //Add optionsMy to Firestore under users uid (Users/$uid/optionsMy/$timestamp/$optionsMy)
    fun addChosenOptions() {
        //Add my gender and age to Firestore
        optionsMy.set(OptionsMyModel(
                RealmUtil().maleGenderMy(),
                RealmUtil().femaleGenderMy(),
                RealmUtil().under18My(),
                RealmUtil().from19to22My(),
                RealmUtil().from23to26My(),
                RealmUtil().from27to35My(),
                RealmUtil().over36My()
        )).addOnCompleteListener { listener ->
            if (listener.isSuccessful) {
                Log.d(TAG, "OptionsMy were added successfully")
            } else {
                Log.d(TAG, "Problem with adding optionsMy")
            }
        }
        //Add Looking For options to Firestore
        optionsLF.set(OptionsLFModel(
                RealmUtil().maleGenderLookingFor(),
                RealmUtil().femaleGenderLookingFor(),
                RealmUtil().under18LookingFor(),
                RealmUtil().from19to22LookingFor(),
                RealmUtil().from23to26LookingFor(),
                RealmUtil().from27to35LookingFor(),
                RealmUtil().over36LookingFor()
        )).addOnCompleteListener { listener ->
            if (listener.isSuccessful) {
                Log.d(TAG, "OptionsLF were added successfully")
            } else {
                Log.d(TAG, "Problem with adding optionsLF")
            }
        }

    }

    fun searchFor(){



    }
}