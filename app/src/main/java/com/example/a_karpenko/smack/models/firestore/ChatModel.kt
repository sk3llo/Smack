
package com.example.a_karpenko.smack.models.firestore

import com.google.firebase.firestore.ServerTimestamp
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmModule
import java.util.*

@RealmClass
open class ChatModel: RealmModel {

    var id: Int? = null

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