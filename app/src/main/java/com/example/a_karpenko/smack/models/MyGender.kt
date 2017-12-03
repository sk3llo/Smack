package com.example.a_karpenko.smack.models

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class MyGender: RealmObject() {

    @PrimaryKey
    @Index
    var id: Int? = 0

    var myGender: Int? = 0
    var lookingForGender: Int? = 0

}