
package com.example.a_karpenko.smack.models.chat

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

open class ChatModel {

    var from: String = ""
    var to: String? = ""
    var message: String = ""
    @ServerTimestamp
    var timeStamp: Date? = null

    constructor()  // Needed for Firebase
    constructor(uidMy:String, uidLF: String, message:String, timeStamp: Date?) {
//        this.uid = uid
        this.from = uidMy
        this.to = uidLF
        this.message = message
        this.timeStamp = timeStamp
    }

}