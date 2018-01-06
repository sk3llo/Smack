
package com.example.a_karpenko.smack.models.firestore

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

open class ChatModel {

    var from: String = ""
    var message: String? = null
    @ServerTimestamp
    var timeStamp: Date? = null

    constructor()  // Needed for Firebase
    constructor(uidMy:String, message:String, timeStamp: Date?) {
//        this.uid = uid
        this.from = uidMy
        this.message = message
        this.timeStamp = timeStamp
    }
}