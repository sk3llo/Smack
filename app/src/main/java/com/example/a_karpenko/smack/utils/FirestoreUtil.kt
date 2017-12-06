package com.example.a_karpenko.smack.utils

import android.util.Log
import com.example.a_karpenko.smack.models.ChosenOptionsModel
import com.example.a_karpenko.smack.models.LoginModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

open class FirestoreUtil {

    val TAG = "FirestoreUtil"

    val timestamp = Calendar.getInstance().time
    val uidMy = FirebaseAuth.getInstance().currentUser?.uid
    val currentUser = FirebaseAuth.getInstance().currentUser
    val status = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")
            .collection("status")
    val options = FirebaseFirestore.getInstance()
            .collection("Users").document("$uidMy")
            .collection("options")

    //Log in checker
    fun logIn(): Task<DocumentReference> {
        //Adds random doc id with status
        return status.add(LoginModel(true, "$timestamp")).addOnCompleteListener { complete ->
            if (complete.isSuccessful){
                Log.d(TAG, "Successfully logged in")
            } else {
                Log.d(TAG, "Problems with logging in")
            }
        }
    }
    //Log out checker
    fun logOut(): Task<DocumentReference> {
        //Adds random doc id with status
        return status.add(LoginModel(false, "$timestamp")).addOnCompleteListener { complete ->
            if (complete.isSuccessful){
                Log.d(TAG, "Successfully logged out")
            } else {
                Log.d(TAG, "Problems with logging out")
            }
        }
    }

    fun addChosenOptions(): Task<Void> {
        return options.document("$timestamp").set(ChosenOptionsModel(
                timestamp,
                RealmUtil().maleGenderMy(),
                RealmUtil().femaleGenderMy(),
                RealmUtil().maleGenderLookingFor(),
                RealmUtil().femaleGenderLookingFor(),
                RealmUtil().under18My(),
                RealmUtil().from19to22My(),
                RealmUtil().from23to26My(),
                RealmUtil().from27to35My(),
                RealmUtil().over36My(),
                RealmUtil().under18LookingFor(),
                RealmUtil().from19to22LookingFor(),
                RealmUtil().from23to26LookingFor(),
                RealmUtil().from27to35LookingFor(),
                RealmUtil().over36LookingFor()
        )).addOnCompleteListener { listener ->
            if (listener.isSuccessful){
                Log.d(TAG, "Options were added successfully")
            } else {
                Log.d(TAG, "Problem with adding options")
            }
        }
    }

}