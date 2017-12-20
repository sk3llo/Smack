package com.example.a_karpenko.smack.models.chat

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class StartedActivityPoJo: RealmObject() {

    @PrimaryKey @Index
    var id: Int? = 0
    //Supressed plus value
    var started: Int? = 0
}