package com.project.coolz.smack.models.chat

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class SavedChatsTime : RealmObject() {

    @PrimaryKey
    @Index
    var id: Int? = null

    var time: String? = ""
}