package com.example.a_karpenko.smack.models.firestore

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

open class LoginCheckerModel {

    @ServerTimestamp
    var timestamp: Date? = null
    var waitingListOn: Boolean? = true

    constructor() //For Firebase
    constructor(isLoggedIn: Boolean?, timestamp: Date?) {
        this.waitingListOn = isLoggedIn
        this.timestamp = timestamp
    }
}