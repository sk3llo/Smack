package com.example.a_karpenko.smack.models.age_looking_for

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class From19to22 : RealmObject() {

    @PrimaryKey @Index
    var id : Int? = 0

    var from19to22: Int? = 0
}