package com.example.a_karpenko.smack.models.firestore

open class PresenceModel {

    var presence: String? = ""

    constructor()
    constructor(presence: String?){
        this.presence = presence
    }
}