package com.project.coolz.smack.core.queryData

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.project.coolz.smack.App
import com.project.coolz.smack.models.firestore.PresenceModel
import com.project.coolz.smack.utils.RealmUtil
import com.vanniktech.emoji.EmojiEditText

open class PresenceChecker(foundUser: String?,
                      private var typeView: TextView?,
                      private var editText: EmojiEditText?,
                      private var emojiButton: Button?) {

    private val presenceMy = FirebaseFirestore.getInstance()
            .collection("Users")
            .document(RealmUtil().retrieveMyId()!!)
            .collection("presence")
            .document("my")

    private val presenceLF = FirebaseFirestore.getInstance()
            .collection("Users")
            .document("$foundUser")
            .collection("presence")
            .document("my")

    fun getIn() = presenceMy.set(PresenceModel("1"))
//    set(PresenceModel(true))

    fun getOut() = presenceMy.set(PresenceModel("0"))
//            set(PresenceModel(false))

    fun checkLfPresence() = presenceLF.addSnapshotListener { snapshot, exception ->
        if (exception != null){
            Log.d("PresenceChecker**** ", "Failed to check LF presence")
        }

        if (snapshot!!.exists() && snapshot.get("presence")?.toString() == "0"){
            typeView?.text = "User has left the chat."
            typeView?.visibility = View.VISIBLE
            editText?.isEnabled = false
            editText?.isFocusable = false
            editText?.hint = "User has left the chat."
            emojiButton?.isEnabled = false
            return@addSnapshotListener
        }
        //Check bottom one
//        else if (snapshot.exists() && snapshot["presence"] == true){
//            typeView?.visibility = View.GONE
//            editText?.isEnabled = true
//            editText?.isFocusable = true
//        }
    }
}