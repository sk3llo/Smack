package com.project.coolz.smack.models.chat

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.RealmClass

@RealmClass
open class MessageID: RealmObject() {
    @Index
    var id: Long? = 0
}