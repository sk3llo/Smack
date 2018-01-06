package com.example.a_karpenko.smack.models.saved_chats

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

class SavedMessagesModel: RealmObject() {

    @Required @PrimaryKey
    var id: Int? = 0

    var from: String? = ""
    var messageMy: String? = ""
    var messageLF: String? = ""
    var time: Date? = null
}