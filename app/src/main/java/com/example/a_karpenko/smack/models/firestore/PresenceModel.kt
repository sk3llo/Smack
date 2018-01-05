package com.example.a_karpenko.smack.models.firestore

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

open class PresenceModel {

    var presence: Boolean? = false

    constructor()
    constructor(presence: Boolean?){
        this.presence = presence
    }
}