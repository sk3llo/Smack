package com.example.a_karpenko.smack.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.core.addData.AddOptionsFirestore
import com.example.a_karpenko.smack.core.queryData.WaitingListQuery
import com.example.a_karpenko.smack.models.firestore.LoginCheckerModel
import com.example.a_karpenko.smack.utils.RealmUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class WaitingActivity: AppCompatActivity() {

    val context: Context? = this@WaitingActivity
    var currentDate: Date? = null
    var uidMy: String? = null
    var WL: CollectionReference? = null
    var optionsMy: DocumentReference? = null
    var optionsLF: DocumentReference? = null

    var WPB: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.waiting_screen)

        WPB = findViewById<ProgressBar>(R.id.waitingProgressBar)
        currentDate = Calendar.getInstance().time
        uidMy = FirebaseAuth.getInstance().currentUser?.uid
        WL = FirebaseFirestore.getInstance().collection("WL")


        //Start comparing options and searching for chat
        if (RealmUtil().retrySearchForChat()!!){
            WaitingListQuery(this,this).checkWL()
            checkWL()
        }

     }
    //WL Listener
    fun checkWL() = WL?.addSnapshotListener { snapshot, exception ->
        if (!snapshot.isEmpty && snapshot.documents.last() != null) {
            WL?.document("$uidMy")?.set(LoginCheckerModel(true, currentDate))
            val lastDoc = snapshot.documentChanges.last().document
            if (lastDoc.exists() && lastDoc["waitingListOn"] == true && lastDoc.reference.id != uidMy) {
                WaitingListQuery(this, this).checkOptions(lastDoc.reference)
                Log.d("WaitingActivity***** ", "USER GOT ON WL : ${lastDoc.reference.id}}")
            }
        }
    }


    fun stopSearch(view: View?){
        WL?.document("$uidMy")?.delete()
        checkWL()?.remove()
        RealmUtil().retrySearch(false)
//        AddOptionsFirestore().waitingOff()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        WL?.document("$uidMy")?.delete()
        super.onBackPressed()
        checkWL()?.remove()
        RealmUtil().retrySearch(false)
        startActivity(Intent(this, MainActivity::class.java))
//        AddOptionsFirestore().waitingOff()
        finish()
    }

    override fun onDestroy() {
        WL?.document("$uidMy")?.delete()
        super.onDestroy()
        checkWL()?.remove()
        RealmUtil().retrySearch(false)
//        AddOptionsFirestore().waitingOff()
    }
}