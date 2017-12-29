package com.example.a_karpenko.smack.core.queryData

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.a_karpenko.smack.R
import com.example.a_karpenko.smack.models.firestore.PresenceModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vanniktech.emoji.EmojiEditText
import java.util.*

class PresenceChecker(foundUser: String?,
                      private var typeView: TextView?,
                      private var editText: EmojiEditText?,
                      private var emojiButton: Button?) {

    private val presenceMy = FirebaseFirestore.getInstance()
            .collection("Users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .collection("presence")
            .document("my")

    private val presenceLF = FirebaseFirestore.getInstance()
            .collection("Users")
            .document("$foundUser")
            .collection("presence")
            .document("my")

    fun getIn() = presenceMy.set(PresenceModel(true))

    fun getOut() = presenceMy.set(PresenceModel(false))

    fun checkLfPresence() = presenceLF.addSnapshotListener { snapshot, exception ->
        if (exception != null){
            Log.d("PresenceChecker**** ", "Failed to check LF presence")
        }

        if (snapshot.exists() && snapshot["presence"] == false){
            typeView?.text = "User has left the chat."
            typeView?.visibility = View.VISIBLE
            editText?.isEnabled = false
            editText?.isFocusable = false
            editText?.hint = "User has left the chat."
            emojiButton?.isEnabled = false
        }
        //Check bottom one
//        else if (snapshot.exists() && snapshot["presence"] == true){
//            typeView?.visibility = View.GONE
//            editText?.isEnabled = true
//            editText?.isFocusable = true
//        }
    }
}