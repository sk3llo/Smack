package com.project.coolz.smack.models.chat

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

//Class that is used to "close the wall" when the user is found
@RealmClass
open class ChatWall: RealmObject(){

    @PrimaryKey
    var id: Int? = null

    var isUserFound: Boolean? = false
}