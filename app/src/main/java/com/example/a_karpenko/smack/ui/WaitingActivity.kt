package com.example.a_karpenko.smack.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.utils.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class WaitingActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.waiting_screen)

//        val currentDate: Date? = Calendar.getInstance().time
//        val uidMy = FirebaseAuth.getInstance().currentUser?.uid
//
//        //Firestore refs
//        val status = FirebaseFirestore.getInstance()
//                .collection("Users").document("$uidMy")
//                .collection("status")
//        val optionsMy = FirebaseFirestore.getInstance()
//                .collection("Users").document("$uidMy")
//                .collection("options").document("optionsMy")
//
//        val optionsLF = FirebaseFirestore.getInstance()
//                .collection("Users").document("$uidMy")
//                .collection("options").document("optionsLF")

    }



    fun stopSearch(view: View?){
        startActivity(Intent(this, MainActivity::class.java))
        FirestoreUtil().waitingOff()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        FirestoreUtil().waitingOff()
        finish()
    }
}