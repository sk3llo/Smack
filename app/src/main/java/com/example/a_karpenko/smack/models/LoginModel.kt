package com.example.a_karpenko.smack.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class LoginModel {

    var loggedIn: Boolean? = false
    @ServerTimestamp
    var timestamp: String? = null

    constructor() //For Firebase
    constructor(loggedIn: Boolean?, timestamp: String){
        this.loggedIn = loggedIn
        this.timestamp = timestamp
    }
}