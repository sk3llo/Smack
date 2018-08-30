package com.project.coolz.smack.models.firestore

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

open class InputModel {

    var input: Boolean? = false

    @ServerTimestamp
    var timeStamp: Date? = null

    constructor()
    constructor(input: Boolean?, timeStamp: Date?){
        this.input = input
        this.timeStamp = timeStamp
    }
}