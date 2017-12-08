package com.example.a_karpenko.smack.core.addData

import android.util.Log
import com.example.a_karpenko.smack.models.firestore.OptionsMyModel
import com.example.a_karpenko.smack.models.firestore.LoginCheckerModel
import com.example.a_karpenko.smack.models.firestore.OptionsLFModel
import com.example.a_karpenko.smack.ui.WaitingActivity
import com.example.a_karpenko.smack.utils.RealmUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddOptionsFirestore {

    val TAG = "Firestore Util"

    val currentDate: Date? = Calendar.getInstance().time
    val uidMy = FirebaseAuth.getInstance().currentUser?.uid
    val currentUser = FirebaseAuth.getInstance().currentUser

    //Firestore refs
    val status = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")
            .collection("waiting_list")
    val optionsMy = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")
            .collection("options").document("optionsMy")

    val optionsLF = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")
            .collection("options").document("optionsLF")


    //Log in
    fun waitingOn(): Task<Void> {
        return status.document("enter").set(LoginCheckerModel(true, currentDate)).addOnCompleteListener { complete ->
            if (complete.isSuccessful) {
                //TODO: Add checker for options on Firestore
                Log.d("App", "Entering WL successful")
            } else {
                Log.d("App", "Failed to enter WL")
            }
        }
    }

    //Log out
    fun waitingOff(): Task<Void> {
        return status.document("enter").set(LoginCheckerModel(false, currentDate)).addOnCompleteListener { complete ->
            if (complete.isSuccessful) {
                Log.d(TAG, "Exit WL successful")
            } else {
                Log.d(TAG, "Failed to exit WL")
            }
        }
    }
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