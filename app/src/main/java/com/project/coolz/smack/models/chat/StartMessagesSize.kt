package com.project.coolz.smack.models.chat

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class StartMessagesSize : RealmObject() {

    @PrimaryKey
    var id: Int? = null

    //Save list size at the beginning and the end
    //and when user save the chat afterward i use this values to query it
    @Index
    var startMessagesSize: Int? = 0

}