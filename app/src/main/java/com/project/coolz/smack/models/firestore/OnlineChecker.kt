package com.project.coolz.smack.models.firestore

import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class OnlineChecker: RealmModel{
    var uid: String? = ""

    constructor()
    constructor(uid: String?){

        this.uid = uid
    }
}