package com.example.a_karpenko.smack.models.chat

import com.example.a_karpenko.smack.models.firestore.ChatModel
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class StartMessagesSize : RealmObject() {

    //Save list size at the beginning and the end
    //and when user save the chat afterward i use this values to query it
    @Index
    var startMessagesSize: Int? = 0

}