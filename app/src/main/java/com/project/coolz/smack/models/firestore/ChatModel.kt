
package com.project.coolz.smack.models.firestore

import com.google.firebase.firestore.ServerTimestamp
import io.realm.RealmModel
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class ChatModel: RealmModel {

    var id: Long? = null

    var from: String = ""
    var message: String? = null
    var time: String? = ""
    @ServerTimestamp
    var timeStamp: Date? = null

    constructor()  // Needed for Firebase
    constructor(id: Long, uidMy:String, message:String, time: String, timeStamp: Date?) {
//        this.uid = uid
        this.id = id
        this.from = uidMy
        this.message = message
        this.time = time
        this.timeStamp = timeStamp
    }
}