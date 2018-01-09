package com.example.a_karpenko.smack.models.chat

import com.example.a_karpenko.smack.models.firestore.ChatModel
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class SavedChatId: RealmObject() {

    @PrimaryKey
    var id: Int? = null

}