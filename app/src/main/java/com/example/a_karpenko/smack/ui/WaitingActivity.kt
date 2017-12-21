package com.example.a_karpenko.smack.ui

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.core.queryData.PresenceChecker
import com.example.a_karpenko.smack.core.queryData.WaitingListQuery
import com.example.a_karpenko.smack.utils.RealmUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import java.util.*

class WaitingActivity: AppCompatActivity() {

    val context: Context? = this@WaitingActivity
    var currentDate: Date? = Calendar.getInstance().time
    var uidMy: String? = null
    var db: FirebaseFirestore? = null
    var optionsMy: DocumentReference? = null
    var optionsLF: DocumentReference? = null

    var WPB: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.waiting_screen)

        WPB = findViewById<ProgressBar>(R.id.waitingProgressBar)
        uidMy = FirebaseAuth.getInstance().currentUser?.uid
        db = FirebaseFirestore.getInstance()


        //Start comparing options and searching for chat
//        WaitingListQuery(this,this).checkWL()
        checkWListener()

     }


    //db Listener
    fun checkWListener() = db?.collection("WL")?.addSnapshotListener { snapshot, exception ->
        doAsync {
            if (exception != null){
                Snackbar.make(findViewById(android.R.id.content), "Please, check your Internet connection", Snackbar.LENGTH_SHORT)
            }
            else {
                var last = snapshot.documentChanges.last()
                    if (last.document["waitingListOn"] == true && last.document.reference.id != uidMy) {
                        WaitingListQuery(this@WaitingActivity, this@WaitingActivity).checkOptions(last.document.reference)
                        Log.d("WaitingActivity***** ", "COMPARING OPTIONS OF USER : ${last.document.reference.id}}")
                    }
            }
        }
    }


    fun stopSearch(view: View?){
        checkWListener()?.remove()
        db?.collection("WL")?.document("$uidMy")?.delete()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        checkWListener()?.remove()
        db?.collection("WL")?.document("$uidMy")?.delete()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onStop() {
        super.onStop()
        checkWListener()?.remove()
        db?.collection("WL")?.document("$uidMy")?.delete()
    }

    override fun onDestroy() {
        super.onDestroy()
        checkWListener()?.remove()
        db?.collection("WL")?.document("$uidMy")?.delete()
    }
}