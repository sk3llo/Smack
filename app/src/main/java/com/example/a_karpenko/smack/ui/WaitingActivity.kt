package com.example.a_karpenko.smack.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.core.addData.AddOptionsFirestore
import com.example.a_karpenko.smack.core.queryData.WaitingListQuery
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.*

class WaitingActivity: AppCompatActivity() {

    val context: Context? = this@WaitingActivity

    var currentDate: Date? = null
    var uidMy: String? = null
    var status: CollectionReference? = null
    var optionsMy: DocumentReference? = null
    var optionsLF: DocumentReference? = null

    var WPB: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.waiting_screen)

        WPB = findViewById<ProgressBar>(R.id.waitingProgressBar)
        currentDate = Calendar.getInstance().time
        uidMy = FirebaseAuth.getInstance().currentUser?.uid
        status = FirebaseFirestore.getInstance()
                .collection("Users").document("$uidMy")
                .collection("wl")

        optionsMy = FirebaseFirestore.getInstance()
                .collection("Users").document("$uidMy")
                .collection("options").document("optionsMy")

        optionsLF = FirebaseFirestore.getInstance()
                .collection("Users").document("$uidMy")
                .collection("options").document("optionsLF")

        WaitingListQuery(this, this@WaitingActivity).checkWL()

     }


    fun stopSearch(view: View?){
        AddOptionsFirestore().waitingOff()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        AddOptionsFirestore().waitingOff()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        AddOptionsFirestore().waitingOff()
    }
}