package com.example.a_karpenko.smack.models.chat

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class IncrementValue: RealmObject() {

    @PrimaryKey @Index
    var id: Int? = 0
    //Supressed plus value
    var plus1: Int? = 0
}