package com.example.a_karpenko.smack.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.core.queryData.WaitingListQuery
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

class WaitingActivity: AppCompatActivity() {

    val context: Context? = this@WaitingActivity
    var uidMy: String? = null
    var db: FirebaseFirestore? = null

    var snapshotList: MutableList<DocumentReference>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.waiting_activity)

        uidMy = FirebaseAuth.getInstance().currentUser?.uid
        db = FirebaseFirestore.getInstance()

        snapshotList = ArrayList()

        //Start comparing options and searching for chat
        WaitingListQuery(this,this).checkWL()
        checkWListener()

    }


    //db Listener
    fun checkWListener() = db?.collection("WL")?.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Snackbar.make(findViewById(android.R.id.content), "Please, check your Internet connection", Snackbar.LENGTH_SHORT)
            }
            else {
                val last = snapshot.documentChanges

                last.forEach {
                    if (it.type == DocumentChange.Type.MODIFIED) {
                        if (it.document.id != uidMy && it.document["waitingListOn"] == true && snapshotList?.size!! <= 0) {
                            WaitingListQuery(this@WaitingActivity, this@WaitingActivity).checkOptions(it.document.reference)
                        }
                    }
                    if (it.type == DocumentChange.Type.ADDED) {
                        if (it.document.id != uidMy && it.document["waitingListOn"] == true && snapshotList?.size!! <= 0) {
                            WaitingListQuery(this@WaitingActivity, this@WaitingActivity).checkOptions(it.document.reference)
                        }
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