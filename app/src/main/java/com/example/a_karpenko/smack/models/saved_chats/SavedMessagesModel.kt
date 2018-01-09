package com.example.a_karpenko.smack.models.saved_chats

import com.example.a_karpenko.smack.models.firestore.ChatModel
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmModule
import io.realm.annotations.Required
import java.util.*
import kotlin.collections.ArrayList

open class SavedMessagesModel: RealmObject() {

//    @Required @PrimaryKey
//    var id: Int? = 0

    var from: String? = ""
    var messageMy: String? = ""
    var messageLF: String? = ""
    var time: Date? = null
}