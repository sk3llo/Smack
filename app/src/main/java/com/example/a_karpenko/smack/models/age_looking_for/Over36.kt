package com.example.a_karpenko.smack.models.age_looking_for

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class Over36 : RealmObject() {

    @PrimaryKey @Index
    var id: Int? = 0

    var over36: Int? = 0
}