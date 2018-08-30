package com.project.coolz.smack.models.firestore

open class PresenceModel {

    var presence: String? = ""

    constructor()
    constructor(presence: String?){
        this.presence = presence
    }
}