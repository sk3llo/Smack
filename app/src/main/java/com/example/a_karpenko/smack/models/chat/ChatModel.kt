
package com.example.a_karpenko.smack.models.chat

import com.firebase.ui.firestore.FirestoreArray
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

open class ChatModel {

    var uid: String = ""
    var name: String = ""
    var message: String = ""
    @ServerTimestamp
    var timeStamp: Date? = null

    constructor()  // Needed for Firebase
    constructor(name:String, message:String, uid:String, timeStamp: Date?) {
        this.uid = uid
        this.name = name
        this.message = message
        this.timeStamp = timeStamp
    }

}