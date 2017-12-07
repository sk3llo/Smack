package com.example.a_karpenko.smack.models.firestore

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

open class LoginCheckerModel {

    @ServerTimestamp
    var timestamp: Date? = null
    var isLoggedIn: Boolean? = false

    constructor() //For Firebase
    constructor(isLoggedIn: Boolean?, timestamp: Date?) {
        this.isLoggedIn = isLoggedIn
        this.timestamp = timestamp
    }
}