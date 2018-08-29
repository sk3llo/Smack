package com.example.a_karpenko.smack.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.core.queryData.WaitingListQuery
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

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

        Handler().postDelayed({searchingUsers()}, 1500)
    }


    //db Listener
    fun checkWListener() = db?.collection("WL")?.addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Snackbar.make(findViewById(android.R.id.content), "Please, check your Internet connection", Snackbar.LENGTH_SHORT)
                db?.collection("WL")?.document("$uidMy")?.delete()
            }
            else {

                val last = snapshot!!.documentChanges

                last.forEach {
                    if (it.type == DocumentChange.Type.MODIFIED) {
                        if (it.document.id != uidMy && it.document["waitingListOn"] == true && snapshotList?.size!! <= 0 && snapshotList!!.isEmpty()) {
                            WaitingListQuery(this@WaitingActivity, this@WaitingActivity).checkOptions(it.document.reference)
                        }
                    }
                    if (it.type == DocumentChange.Type.ADDED) {
                        if (it.document.id != uidMy && it.document["waitingListOn"] == true && snapshotList?.size!! <= 0 && snapshotList!!.isEmpty()) {
                            WaitingListQuery(this@WaitingActivity, this@WaitingActivity).checkOptions(it.document.reference)
                        }
                    }
                }
            }
      }

    fun searchingUsers(){
        //Searching users on WL list
        doAsync {

            var searchingText: TextView? = find(R.id.searchingText)
            var db = FirebaseFirestore.getInstance()?.collection("WL")
            if (db.get().exception == null) {
                db.get().addOnCompleteListener {
                    runOnUiThread { searchingText?.text = "Searching: ${it.result.size()}" }
                }
                db.addSnapshotListener { snapshot, exception ->
                    if (exception == null) {
                        runOnUiThread { searchingText?.text = "Searching: ${snapshot?.size()}" }
                    }
                }
            }

        }
    }


    fun stopSearch(view: View?){
        checkWListener()?.remove()
        db?.collection("WL")?.document("$uidMy")?.delete()
        startActivity(Intent(this@WaitingActivity, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
        finish()
    }

    override fun onBackPressed() {
        checkWListener()?.remove()
        db?.collection("WL")?.document("$uidMy")?.delete()
        startActivity(Intent(this@WaitingActivity, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left)
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